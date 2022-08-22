from difflib import restore
import os

from config import Config

opt = Config('training.yml')

gpus = ','.join([str(i) for i in opt.GPU])
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = gpus

import torch

torch.backends.cudnn.benchmark = True

import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader

import random
import time
import numpy as np

import utils
from data_RGB import get_training_data, get_validation_data
from LTEVSFSMBI import centerEsti, F17_N9, F26_N9, F35_N8
import losses
from warmup_scheduler import GradualWarmupScheduler
from tqdm import tqdm
# import argparse
#
# parser = argparse.ArgumentParser(description='Train LTEVSFSMBI')
# parser.add_argument("--model", type=int, default=1,
#                     help="1: centerEsti, 2: F17_N9, 3: F26_N9, 4: F35_N8")
# args = parser.parse_args()

if __name__ == '__main__':

    device_ids = [i for i in range(torch.cuda.device_count())]
    if torch.cuda.device_count() > 1:
        print("\n\nLet's use", torch.cuda.device_count(), "GPUs!\n\n")

    ######### Set Seeds ###########
    random.seed(1234)
    np.random.seed(1234)
    torch.manual_seed(1234)
    torch.cuda.manual_seed_all(1234)

    start_epoch = 1
    mode = opt.MODEL.MODE
    session = opt.MODEL.SESSION

    result_dir = os.path.join(opt.TRAINING.SAVE_DIR, mode, 'results', session)
    model_dir = os.path.join(opt.TRAINING.SAVE_DIR, mode, 'models', session)

    utils.mkdir(result_dir)
    utils.mkdir(model_dir)

    train_dir = opt.TRAINING.TRAIN_DIR
    val_dir = opt.TRAINING.VAL_DIR

    ######### Model ###########
    if mode == "centerEsti":
        model_restoration = centerEsti()
    elif mode == "F17_N9":
        model_restoration = F17_N9
    elif mode == "F26_N9":
        model_restoration = F26_N9
    else:
        model_restoration = F35_N8
    model_restoration.cuda()


    ######### Loss ###########
    # criterion_char = losses.CharbonnierLoss()
    # criterion_edge = losses.EdgeLoss()
    if mode == "centerEsti":
        criterion = losses.CenterEstiLoss()
    elif mode == "F17_N9":
        criterion = losses.F17_N9Loss()
    elif mode == "F26_N9":
        criterion = losses.F26_N9Loss()
    else:
        criterion = losses.F35_N8Loss()

    new_lr = opt.OPTIM.LR_INITIAL

    optimizer = optim.Adam(model_restoration.parameters(), lr=new_lr, betas=(0.9, 0.999), eps=1e-8)

    ######### Scheduler ###########
    warmup_epochs = 3
    scheduler_cosine = optim.lr_scheduler.CosineAnnealingLR(optimizer, opt.OPTIM.NUM_EPOCHS - warmup_epochs,
                                                            eta_min=opt.OPTIM.LR_MIN)
    scheduler = GradualWarmupScheduler(optimizer, multiplier=1, total_epoch=warmup_epochs,
                                       after_scheduler=scheduler_cosine)
    scheduler.step()

    ######### Resume ###########
    if opt.TRAINING.RESUME:
        path_chk_rest = utils.get_last_path(model_dir, "_" + mode + '_latest.pth')
        utils.load_checkpoint(model_restoration, path_chk_rest)
        start_epoch = utils.load_start_epoch(path_chk_rest) + 1
        utils.load_optim(optimizer, path_chk_rest)

        for i in range(1, start_epoch):
            scheduler.step()
        new_lr = scheduler.get_lr()[0]
        print('------------------------------------------------------------------------------')
        print("==> Resuming Training with learning rate:", new_lr)
        print('------------------------------------------------------------------------------')

    if len(device_ids) > 1:
        model_restoration = nn.DataParallel(model_restoration, device_ids=device_ids)

    ######### DataLoaders ###########

    if mode == "centerEsti":
        train_dataset = get_training_data(train_dir, {'patch_size': opt.TRAINING.TRAIN_PS}, pic_index=3, group=7)
        val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS}, pic_index=3, group=7)
    elif mode == "F17_N9":
        criterion = losses.F17_N9Loss()
    elif mode == "F26_N9":
        criterion = losses.F26_N9Loss()
    else:
        criterion = losses.F35_N8Loss()
    # train_dataset = get_training_data(train_dir, {'patch_size': opt.TRAINING.TRAIN_PS})
    # val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS})

    train_loader = DataLoader(dataset=train_dataset, batch_size=opt.OPTIM.BATCH_SIZE, shuffle=True, num_workers=16,
                              drop_last=False, pin_memory=True)
    val_loader = DataLoader(dataset=val_dataset, batch_size=16, shuffle=False, num_workers=8, drop_last=False,
                            pin_memory=True)

    print('===> Start Epoch {} End Epoch {}'.format(start_epoch, opt.OPTIM.NUM_EPOCHS + 1))
    print('===> Loading datasets')

    best_psnr = 0
    best_epoch = 0

    for epoch in range(start_epoch, opt.OPTIM.NUM_EPOCHS + 1):
        epoch_start_time = time.time()
        epoch_loss = 0
        train_id = 1

        model_restoration.train()
        for i, data in enumerate(tqdm(train_loader), 0):

            # zero_grad
            for param in model_restoration.parameters():
                param.grad = None

            target = data[0].cuda()
            input_ = data[1].cuda()

            restored = model_restoration(input_)
            # print("!!! size of restored:", restored.size())

            # Compute loss at each stage

            # loss_char = sum([criterion_char(restored[j], target) for j in range(len(restored))])
            # loss_edge = sum([criterion_edge(restored[j], target) for j in range(len(restored))])
            # loss = loss_char + (0.05 * loss_edge)
            loss = criterion(restored, target)
            # print("!!! type loss:", type(loss), loss)
            # loss = loss.numpy().tolist()
            
            loss.backward()
            optimizer.step()
            epoch_loss += loss.item()

        #### Evaluation ####
        if epoch % opt.TRAINING.VAL_AFTER_EVERY == 0:
            model_restoration.eval()
            psnr_val_rgb = []
            for ii, data_val in enumerate((val_loader), 0):
                target = data_val[0].cuda()
                input_ = data_val[1].cuda()

                with torch.no_grad():
                    restored = model_restoration(input_)
                restored = restored[0]

                for res, tar in zip(restored, target):
                    psnr_val_rgb.append(utils.torchPSNR(res, tar))

            psnr_val_rgb = torch.stack(psnr_val_rgb).mean().item()

            if psnr_val_rgb > best_psnr:
                best_psnr = psnr_val_rgb
                best_epoch = epoch
                torch.save({'epoch': epoch,
                            'state_dict': model_restoration.state_dict(),
                            'optimizer': optimizer.state_dict()
                            }, os.path.join(model_dir, mode + "_model_best.pth"))

            print(
                "[epoch %d PSNR: %.4f --- best_epoch %d Best_PSNR %.4f]" % (epoch, psnr_val_rgb, best_epoch, best_psnr))

            torch.save({'epoch': epoch,
                        'state_dict': model_restoration.state_dict(),
                        'optimizer': optimizer.state_dict()
                        }, os.path.join(model_dir, f"{mode}_model_epoch_{epoch}.pth"))

        scheduler.step()

        print("------------------------------------------------------------------")
        print("Epoch: {}\tTime: {:.4f}\tLoss: {:.4f}\tLearningRate {:.6f}".format(epoch, time.time() - epoch_start_time,
                                                                                  epoch_loss, scheduler.get_lr()[0]))
        print("------------------------------------------------------------------")

        torch.save({'epoch': epoch,
                    'state_dict': model_restoration.state_dict(),
                    'optimizer': optimizer.state_dict()
                    }, os.path.join(model_dir, mode + "_model_latest.pth"))
