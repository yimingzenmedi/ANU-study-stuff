import os

from config import Config

opt = Config('./training_35.yml')

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
from data_RGB_2 import get_training_data, get_validation_data
from LTEVSFSMBI import centerEsti, F35_N8
import losses
from warmup_scheduler import GradualWarmupScheduler
from tqdm import tqdm

if __name__ == '__main__':

    ######### Set Seeds ###########
    random.seed(1234)
    np.random.seed(1234)
    torch.manual_seed(1234)
    torch.cuda.manual_seed_all(1234)

    start_epoch = 1
    mode = opt.MODEL.MODE
    session = opt.MODEL.SESSION

    index3 = 2
    index5 = 4
    pic_index = [index3, index5]

    model_dir = os.path.join(opt.TRAINING.SAVE_DIR, session, f"{mode}")
    utils.mkdir(model_dir)

    train_dir = opt.TRAINING.TRAIN_DIR
    val_dir = opt.TRAINING.VAL_DIR

    ######### Model ###########
    model_restoration = F35_N8()
    model_restoration.cuda()
    model_center = centerEsti()
    model_center.cuda()
    model_center_path = os.path.join(opt.TRAINING.CENTER_MODEL_DIR, session, "centerEsti", "centerEsti_model_best.pth")
    cente_ckpt = torch.load(model_center_path)
    model_center.load_state_dict(cente_ckpt['state_dict_G'])

    device_ids = [i for i in range(torch.cuda.device_count())]
    if torch.cuda.device_count() > 1:
        print("\n\nLet's use", torch.cuda.device_count(), "GPUs!\n\n")

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
        path_chk_rest = utils.get_last_path(model_dir, f'{mode}_model_latest.pth')
        utils.load_checkpoint(model_restoration, path_chk_rest)
        utils.load_optim(optimizer, path_chk_rest)

        start_epoch = utils.load_start_epoch(path_chk_rest) + 1

        for i in range(1, start_epoch):
            scheduler.step()
        new_lr = scheduler.get_lr()[0]

        print('------------------------------------------------------------------------------')
        print(f"==> Resuming Training with learning rate - new_lr: {new_lr}")
        print('------------------------------------------------------------------------------')

    if len(device_ids) > 1:
        model_restoration1 = nn.DataParallel(model_restoration, device_ids=device_ids)

    ######### Loss ###########
    # criterion_char = losses.CharbonnierLoss()
    # criterion_edge = losses.EdgeLoss()
    criterion = losses.F35_N8Loss()

    ######### DataLoaders ###########
    train_dataset = get_training_data(train_dir, {'patch_size': opt.TRAINING.TRAIN_PS}, 7, pic_index)
    train_loader = DataLoader(dataset=train_dataset, batch_size=opt.OPTIM.BATCH_SIZE, shuffle=True, num_workers=8,
                              drop_last=False, pin_memory=True)
    # print("!! Read val_dataset")
    val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS}, 7, [0, 1, 5, 6])
    val_loader = DataLoader(dataset=val_dataset, batch_size=16, shuffle=False, num_workers=8, drop_last=False,
                            pin_memory=True)

    print('===> Start Epoch {} End Epoch {}'.format(start_epoch, opt.OPTIM.NUM_EPOCHS + 1))
    print('===> Loading datasets')

    best_psnr = (0, 0, 0, 0)
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

            input_ = data[1].cuda()
            target3 = data[0][0].cuda()
            target5 = data[0][1].cuda()

            center_restored = model_center(input_)
            center_restored = torch.clamp(center_restored, 0, 1)
            restored = model_restoration(input_, center_restored)
            restored3 = restored[0]
            restored5 = restored[1]
            restored3 = torch.clamp(restored3, 0, 1)
            restored5 = torch.clamp(restored5, 0, 1)

            # print(f"\n>> input_:    mean: {input_.mean()}, max: {input_.max()}, min: {input_.min()}")
            # print(f">> restored4: mean: {center_restored.mean()}, max: {center_restored.max()}, min: {center_restored.min()}")
            # print(f">> target3:   mean: {target3.mean()}, max: {target3.max()}, min: {target3.min()}")
            # print(f">> restored3: mean: {restored3.mean()}, max: {restored3.max()}, min: {restored3.min()}")
            # print(f">> target5:   mean: {target5.mean()}, max: {target5.max()}, min: {target5.min()}")
            # print(f">> restored5: mean: {restored5.mean()}, max: {restored5.max()}, min: {restored5.min()}")

            # Compute loss at each stage
            loss = criterion(restored3, restored5, target3, target5)
            # print(f">> loss: {loss}")
            # print(f"\nrestored1: {restored1.mean()}, restored2: {restored2.mean()}, \ntarget3:   {target3.mean()}, target5:   {target5.mean()}, ")

            loss.backward()
            optimizer.step()
            epoch_loss += loss.item()

        #### Evaluation ####
        if epoch % opt.TRAINING.VAL_AFTER_EVERY == 0:
            model_restoration.eval()
            psnr_val_rgb3 = []
            psnr_val_rgb5 = []

            for ii, data_val in enumerate((val_loader), 0):
                target3 = data_val[0][0].cuda()
                target5 = data_val[0][1].cuda()

                input_ = data_val[1].cuda()

                with torch.no_grad():
                    center_restored = model_center(input_)
                    center_restored = torch.clamp(center_restored, 0, 1)
                    restored = model_restoration(input_, center_restored)

                restored3 = restored[0]
                restored5 = restored[1]
                restored3 = torch.clamp(restored3, 0, 1)
                restored5 = torch.clamp(restored5, 0, 1)
                # print(restored1.shape)
                # print(restored2.shape)
                # print(target3.shape)
                # print(target5.shape)
                for res3, res5, tar3, tar5 in zip(restored3, restored5, target3, target5):
                    psnr_val_rgb3.append(utils.torchPSNR(res3, tar3))
                    psnr_val_rgb5.append(utils.torchPSNR(res5, tar5))

            psnr_val_rgb3 = torch.stack(psnr_val_rgb3).mean().item()
            psnr_val_rgb5 = torch.stack(psnr_val_rgb5).mean().item()

            if (psnr_val_rgb3 - best_psnr[0]) + (psnr_val_rgb5 - best_psnr[1]) > 0:
                best_psnr = (psnr_val_rgb3, psnr_val_rgb5)
                best_epoch = epoch
                torch.save({'epoch': epoch,
                            'state_dict_G': model_restoration.state_dict(),
                            'optimizer': optimizer.state_dict()
                            }, os.path.join(model_dir, f'{mode}_model_best.pth'))

            print(
                "[epoch %d PSNR: %.4f, %.4f --- best_epoch %d Best_PSNR %.4f, %.4f]" % (
                    epoch, psnr_val_rgb3, psnr_val_rgb5, best_epoch, best_psnr[0], best_psnr[1]))

            torch.save({'epoch': epoch,
                        'state_dict_G': model_restoration.state_dict(),
                        'optimizer': optimizer.state_dict()
                        }, os.path.join(model_dir, f"{mode}_model_epoch_{epoch}.pth"))

        scheduler.step()

        print("------------------------------------------------------------------")
        print(
            "Epoch: {}\tTime: {:.4f}\tLoss: {:.4f}\tLearningRate {:.6f}".format(
                epoch,
                time.time() - epoch_start_time,
                epoch_loss,
                scheduler.get_lr()[
                    0]))
        print("------------------------------------------------------------------")

        torch.save({'epoch': epoch,
                    'state_dict_G': model_restoration.state_dict(),
                    'optimizer': optimizer.state_dict()
                    }, os.path.join(model_dir, f'{mode}_model_latest.pth'))
