import os

from config import Config

opt = Config('./training_F35_N8.yml')

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
from MPRNet import MPRNet
import LTEVSFSMBILosses
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
    mode = opt.MODEL.MODE
    if mode == 'F35_N8':
        index1 = 3
        index2 = 5
    elif mode == 'F26_N9':
        index1 = 2
        index2 = 6
    else:
        index1 = 1
        index2 = 7

    result_dir1 = os.path.join(opt.TRAINING.SAVE_DIR, f"{mode}", 'results', session)
    model_dir1 = os.path.join(opt.TRAINING.SAVE_DIR, f"{mode}", 'models', session)
    result_dir2 = os.path.join(opt.TRAINING.SAVE_DIR, f"{mode}", 'results', session)
    model_dir2 = os.path.join(opt.TRAINING.SAVE_DIR, f"{mode}", 'models', session)

    utils.mkdir(result_dir1)
    utils.mkdir(model_dir1)
    utils.mkdir(result_dir2)
    utils.mkdir(model_dir2)

    train_dir = opt.TRAINING.TRAIN_DIR
    val_dir = opt.TRAINING.VAL_DIR

    ######### Model ###########
    model_restoration1 = MPRNet()
    model_restoration2 = MPRNet()
    model_restoration1.cuda()
    model_restoration2.cuda()

    device_ids = [i for i in range(torch.cuda.device_count())]
    if torch.cuda.device_count() > 1:
        print("\n\nLet's use", torch.cuda.device_count(), "GPUs!\n\n")

    new_lr = opt.OPTIM.LR_INITIAL

    optimizer1 = optim.Adam(model_restoration1.parameters(), lr=new_lr, betas=(0.9, 0.999), eps=1e-8)
    optimizer2 = optim.Adam(model_restoration2.parameters(), lr=new_lr, betas=(0.9, 0.999), eps=1e-8)

    ######### Scheduler ###########
    warmup_epochs = 3
    scheduler_cosine1 = optim.lr_scheduler.CosineAnnealingLR(optimizer1, opt.OPTIM.NUM_EPOCHS - warmup_epochs,
                                                             eta_min=opt.OPTIM.LR_MIN)
    scheduler1 = GradualWarmupScheduler(optimizer1, multiplier=1, total_epoch=warmup_epochs,
                                        after_scheduler=scheduler_cosine1)
    scheduler1.step()

    scheduler_cosine2 = optim.lr_scheduler.CosineAnnealingLR(optimizer2, opt.OPTIM.NUM_EPOCHS - warmup_epochs,
                                                             eta_min=opt.OPTIM.LR_MIN)
    scheduler2 = GradualWarmupScheduler(optimizer2, multiplier=1, total_epoch=warmup_epochs,
                                        after_scheduler=scheduler_cosine2)
    scheduler2.step()

    ######### Resume ###########
    if opt.TRAINING.RESUME:
        path_chk_rest1 = utils.get_last_path(model_dir1, f'{mode}_model_{index1}_latest.pth')
        utils.load_checkpoint(model_restoration1, path_chk_rest1)
        start_epoch = utils.load_start_epoch(path_chk_rest1) + 1
        utils.load_optim(optimizer1, path_chk_rest1)

        path_chk_rest2 = utils.get_last_path(model_dir2, f'{mode}_model_{index2}_latest.pth')
        utils.load_checkpoint(model_restoration2, path_chk_rest2)
        utils.load_optim(optimizer2, path_chk_rest2)

        for i in range(1, start_epoch):
            scheduler1.step()
            scheduler2.step()
        new_lr1 = scheduler1.get_lr()[0]
        new_lr2 = scheduler2.get_lr()[0]


        print('------------------------------------------------------------------------------')
        print(f"==> Resuming Training with learning rate - new_lr2: {new_lr1}, new_lr2: {new_lr2}")
        print('------------------------------------------------------------------------------')

    if len(device_ids) > 1:
        model_restoration1 = nn.DataParallel(model_restoration1, device_ids=device_ids)
        model_restoration2 = nn.DataParallel(model_restoration2, device_ids=device_ids)

    ######### Loss ###########
    # criterion_char = losses.CharbonnierLoss()
    # criterion_edge = losses.EdgeLoss()
    criterion = LTEVSFSMBILosses.F35_N8Loss()

    ######### DataLoaders ###########
    train_dataset = get_training_data(train_dir, {'patch_size': opt.TRAINING.TRAIN_PS}, 7, index1-1)
    train_loader = DataLoader(dataset=train_dataset, batch_size=opt.OPTIM.BATCH_SIZE, shuffle=True, num_workers=8,
                              drop_last=False, pin_memory=True)
    # print("!! Read val_dataset")
    val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS}, 7, index1-1)
    val_loader = DataLoader(dataset=val_dataset, batch_size=16, shuffle=False, num_workers=8, drop_last=False,
                            pin_memory=True)

    print('===> Start Epoch {} End Epoch {}'.format(start_epoch, opt.OPTIM.NUM_EPOCHS + 1))
    print('===> Loading datasets')

    best_psnr = (0, 0)
    best_epoch = 0

    for epoch in range(start_epoch, opt.OPTIM.NUM_EPOCHS + 1):
        epoch_start_time = time.time()
        epoch_loss = 0
        train_id = 1

        model_restoration1.train()
        model_restoration2.train()

        for i, data in enumerate(tqdm(train_loader), 0):
            # zero_grad
            for param in model_restoration1.parameters():
                param.grad = None
            for param in model_restoration2.parameters():
                param.grad = None

            input_ = data[1].cuda()
            target1 = data[0][0].cuda()
            target2 = data[0][1].cuda()
            # print(f">> input_: mean: {input_.mean()}, max: {input_.max()}, min: {input_.min()}")
            # print(f">> target1: mean: {target1.mean()}, max: {target1.max()}, min: {target1.min()}")
            # print(f">> target2: mean: {target2.mean()}, max: {target2.max()}, min: {target2.min()}")

            restored1 = model_restoration1(input_)[0]
            restored2 = model_restoration2(input_)[0]

            # print(f">> restored1: mean: {restored1.mean()}, max: {restored1.max()}, min: {restored1.min()}")
            # print(f">> restored2: mean: {restored2.mean()}, max: {restored2.max()}, min: {restored2.min()}")

        # Compute loss at each stage
            loss = criterion(restored1, restored2, target1, target2)
            # print(f"\nrestored1: {restored1.mean()}, restored2: {restored2.mean()}, \ntarget1:   {target1.mean()}, target2:   {target2.mean()}, ")

            loss.backward()
            optimizer1.step()
            optimizer2.step()
            epoch_loss += loss.item()

        #### Evaluation ####
        if epoch % opt.TRAINING.VAL_AFTER_EVERY == 0:
            model_restoration1.eval()
            model_restoration2.eval()
            psnr_val_rgb1 = []
            psnr_val_rgb2 = []
            for ii, data_val in enumerate((val_loader), 0):
                target1 = data_val[0][0].cuda()
                target2 = data_val[0][1].cuda()
                input_ = data_val[1].cuda()

                with torch.no_grad():
                    restored1 = model_restoration1(input_)
                    restored2 = model_restoration2(input_)
                restored1 = restored1[0]
                restored2 = restored2[0]
                # print(restored1.shape)
                # print(restored2.shape)
                # print(target1.shape)
                # print(target2.shape)
                for res1, res2, tar1, tar2 in zip(restored1, restored2, target1, target2):
                    psnr_val_rgb1.append(utils.torchPSNR(res1, tar2))
                    psnr_val_rgb2.append(utils.torchPSNR(res1, tar2))

            psnr_val_rgb1 = torch.stack(psnr_val_rgb1).mean().item()
            psnr_val_rgb2 = torch.stack(psnr_val_rgb2).mean().item()

            if (psnr_val_rgb1 - best_psnr[0]) + (psnr_val_rgb2 - best_psnr[1]) > 0:
                best_psnr = (psnr_val_rgb1, psnr_val_rgb2)
                best_epoch = epoch
                torch.save({'epoch': epoch,
                            'state_dict': model_restoration1.state_dict(),
                            'optimizer': optimizer1.state_dict()
                            }, os.path.join(model_dir1, f'{mode}_model_{index1}_best.pth'))
                torch.save({'epoch': epoch,
                            'state_dict': model_restoration2.state_dict(),
                            'optimizer': optimizer2.state_dict()
                            }, os.path.join(model_dir2, f'{mode}_model_{index2}_best.pth'))

            print(
                "[epoch %d PSNR: %.4f, %.4f --- best_epoch %d Best_PSNR %.4f, %.4f]" % (
                    epoch, psnr_val_rgb1, psnr_val_rgb2, best_epoch, best_psnr[0], best_psnr[1]))

            torch.save({'epoch': epoch,
                        'state_dict': model_restoration1.state_dict(),
                        'optimizer': optimizer1.state_dict()
                        }, os.path.join(model_dir1, f"{mode}_model_{index1}_epoch_{epoch}.pth"))
            torch.save({'epoch': epoch,
                        'state_dict': model_restoration2.state_dict(),
                        'optimizer': optimizer2.state_dict()
                        }, os.path.join(model_dir2, f"{mode}_model_{index2}_epoch_{epoch}.pth"))

        scheduler1.step()
        scheduler2.step()

        print("------------------------------------------------------------------")
        print("Epoch: {}\tTime: {:.4f}\tLoss: {:.4f}\tLearningRate1 {:.6f}\tLearningRate2 {:.6f}".format(epoch,
                                                                                  time.time() - epoch_start_time,
                                                                                  epoch_loss,
                                                                                  scheduler1.get_lr()[0],
                                                                                  scheduler2.get_lr()[0]))
        print("------------------------------------------------------------------")

        torch.save({'epoch': epoch,
                    'state_dict': model_restoration1.state_dict(),
                    'optimizer': optimizer1.state_dict()
                    }, os.path.join(model_dir1, f'{mode}_model_{index1}_latest.pth'))
        torch.save({'epoch': epoch,
                    'state_dict': model_restoration2.state_dict(),
                    'optimizer': optimizer2.state_dict()
                    }, os.path.join(model_dir2, f'{mode}_model_{index2}_latest.pth'))
