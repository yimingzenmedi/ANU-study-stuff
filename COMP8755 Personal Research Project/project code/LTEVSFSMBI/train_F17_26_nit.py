import os

from config import Config

opt = Config('./training_17_26.yml')

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
from data_RGB_4 import get_training_data, get_validation_data
from LTEVSFSMBI import centerEsti, F17_N9, F26_N9, F35_N8
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

    index1 = 0
    index2 = 1
    index6 = 5
    index7 = 6
    pic_index = [index1, index2, index6, index7]

    model_dir1 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F17_N9")
    model_dir2 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F26_N9")

    utils.mkdir(model_dir1)
    utils.mkdir(model_dir2)

    train_dir = opt.TRAINING.TRAIN_DIR
    val_dir = opt.TRAINING.VAL_DIR

    ######### Model ###########
    model_restoration1 = F17_N9()
    model_restoration1.cuda()
    model_restoration2 = F26_N9()
    model_restoration2.cuda()

    model_center = centerEsti()
    model_center.cuda()
    model_center_path = os.path.join(opt.TRAINING.CENTER_MODEL_DIR, session, "centerEsti", "centerEsti_model_best.pth")
    center_ckpt = torch.load(model_center_path)
    model_center.load_state_dict(center_ckpt['state_dict_G'])

    model_F35 = F35_N8()
    model_F35.cuda()
    model_F35_path = os.path.join(opt.TRAINING.CENTER_MODEL_DIR, session, "F35_N8", "F35_N8_model_best.pth")
    model_F35_ckpt = torch.load(model_F35_path)
    model_F35.load_state_dict(model_F35_ckpt["state_dict_G"])

    device_ids = [i for i in range(torch.cuda.device_count())]
    if torch.cuda.device_count() > 1:
        print("\n\nLet's use", torch.cuda.device_count(), "GPUs!\n\n")

    new_lr1 = opt.OPTIM.LR_INITIAL
    new_lr2 = opt.OPTIM.LR_INITIAL

    optimizer1 = optim.Adam(model_restoration1.parameters(), lr=new_lr1, betas=(0.9, 0.999), eps=1e-8)
    optimizer2 = optim.Adam(model_restoration2.parameters(), lr=new_lr2, betas=(0.9, 0.999), eps=1e-8)

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
        path_chk_rest1 = utils.get_last_path(model_dir1, 'F17_N9_model_latest.pth')
        utils.load_checkpoint(model_restoration1, path_chk_rest1)
        utils.load_optim(optimizer1, path_chk_rest1)

        path_chk_rest2 = utils.get_last_path(model_dir2, 'F26_N9_model_latest.pth')
        utils.load_checkpoint(model_restoration2, path_chk_rest2)
        utils.load_optim(optimizer2, path_chk_rest2)

        start_epoch = utils.load_start_epoch(path_chk_rest1) + 1

        for i in range(1, start_epoch):
            scheduler1.step()
            scheduler2.step()
        new_lr1 = scheduler1.get_lr()[0]
        new_lr2 = scheduler2.get_lr()[0]

        print('------------------------------------------------------------------------------')
        print(f"==> Resuming Training with learning rate - new_lr1: {new_lr1}, new_lr2: {new_lr2}")
        print('------------------------------------------------------------------------------')

    if len(device_ids) > 1:
        model_restoration1 = nn.DataParallel(model_restoration1, device_ids=device_ids)
        model_restoration2 = nn.DataParallel(model_restoration2, device_ids=device_ids)

    ######### Loss ###########
    criterion = losses.F17_N9_F26_N9Loss()

    ######### DataLoaders ###########
    train_dataset = get_training_data(train_dir, {'patch_size': opt.TRAINING.TRAIN_PS}, 7, pic_index)
    train_loader = DataLoader(dataset=train_dataset, batch_size=opt.OPTIM.BATCH_SIZE, shuffle=True, num_workers=12,
                              drop_last=False, pin_memory=True)
    # print("!! Read val_dataset")
    val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS}, 7, pic_index)
    val_loader = DataLoader(dataset=val_dataset, batch_size=16, shuffle=False, num_workers=12, drop_last=False,
                            pin_memory=True)

    print('===> Start Epoch {} End Epoch {}'.format(start_epoch, opt.OPTIM.NUM_EPOCHS + 1))
    print('===> Loading datasets')

    best_psnr = (0, 0, 0, 0)
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
            target6 = data[0][2].cuda()
            target7 = data[0][3].cuda()

            restored4 = model_center(input_)
            restored4 = torch.clamp(restored4, 0, 1)
            restored3, restored5 = model_F35(input_, restored4)
            restored3 = torch.clamp(restored3, 0, 1)
            restored5 = torch.clamp(restored5, 0, 1)

            restored2, restored6 = model_restoration2(input_, restored3, restored4, restored5)
            restored2 = torch.clamp(restored2, 0, 1)
            restored6 = torch.clamp(restored6, 0, 1)

            restored1, restored7 = model_restoration1(input_, restored2, restored3, restored5, restored6)
            restored1 = torch.clamp(restored1, 0, 1)
            restored7 = torch.clamp(restored7, 0, 1)

            # print(f"\n>> input_:    mean: {input_.mean()}, max: {input_.max()}, min: {input_.min()}")
            # print(f">> restored4: mean: {center_restored.mean()}, max: {center_restored.max()}, min: {center_restored.min()}")
            # print(f">> target3:   mean: {target3.mean()}, max: {target3.max()}, min: {target3.min()}")
            # print(f">> restored3: mean: {restored3.mean()}, max: {restored3.max()}, min: {restored3.min()}")
            # print(f">> target5:   mean: {target5.mean()}, max: {target5.max()}, min: {target5.min()}")
            # print(f">> restored5: mean: {restored5.mean()}, max: {restored5.max()}, min: {restored5.min()}")

            # Compute loss at each stage
            loss = criterion(restored1, restored7, restored2, restored6, target1, target7, target2, target6)
            # print(f">> loss: {loss}")
            # print(f"\nrestored1: {restored1.mean()}, restored2: {restored2.mean()}, \ntarget3:   {target3.mean()}, target5:   {target5.mean()}, ")

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
            psnr_val_rgb6 = []
            psnr_val_rgb7 = []

            for ii, data_val in enumerate((val_loader), 0):
                target1 = data_val[0][0].cuda()
                target2 = data_val[0][1].cuda()
                target6 = data_val[0][2].cuda()
                target7 = data_val[0][3].cuda()

                input_ = data_val[1].cuda()

                with torch.no_grad():
                    restored4 = model_center(input_)
                    restored4 = torch.clamp(restored4, 0, 1)
                    restored3, restored5 = model_F35(input_, restored4)
                    restored3 = torch.clamp(restored3, 0, 1)
                    restored5 = torch.clamp(restored5, 0, 1)

                    restored2, restored6 = model_restoration2(input_, restored3, restored4, restored5)
                    restored2 = torch.clamp(restored2, 0, 1)
                    restored6 = torch.clamp(restored6, 0, 1)

                    restored1, restored7 = model_restoration1(input_, restored2, restored3, restored5, restored6)
                    restored1 = torch.clamp(restored1, 0, 1)
                    restored7 = torch.clamp(restored7, 0, 1)

                for res1, res2, res6, res7, tar1, tar2, tar6, tar7 in zip(restored1, restored2, restored6, restored7, target1, target2, target6, target7):
                    psnr_val_rgb1.append(utils.torchPSNR(res1, tar1))
                    psnr_val_rgb2.append(utils.torchPSNR(res2, tar2))
                    psnr_val_rgb6.append(utils.torchPSNR(res6, tar6))
                    psnr_val_rgb7.append(utils.torchPSNR(res7, tar7))

            psnr_val_rgb1 = torch.stack(psnr_val_rgb1).mean().item()
            psnr_val_rgb2 = torch.stack(psnr_val_rgb2).mean().item()
            psnr_val_rgb6 = torch.stack(psnr_val_rgb6).mean().item()
            psnr_val_rgb7 = torch.stack(psnr_val_rgb7).mean().item()

            if (psnr_val_rgb1 - best_psnr[0]) + (psnr_val_rgb2 - best_psnr[1]) +\
                    (psnr_val_rgb6 - best_psnr[2]) + (psnr_val_rgb7 - best_psnr[3]) > 0:
                best_psnr = (psnr_val_rgb1, psnr_val_rgb2, psnr_val_rgb6, psnr_val_rgb7)
                best_epoch = epoch
                torch.save({'epoch': epoch,
                            'state_dict_G': model_restoration1.state_dict(),
                            'optimizer': optimizer1.state_dict()
                            }, os.path.join(model_dir1, f'F17_N9_model_best.pth'))
                torch.save({'epoch': epoch,
                            'state_dict_G': model_restoration2.state_dict(),
                            'optimizer': optimizer2.state_dict()
                            }, os.path.join(model_dir2, f'F26_N9_model_best.pth'))

            print(
                "[epoch %d PSNR: %.4f, %.4f, %.4f, %.4f --- best_epoch %d Best_PSNR %.4f, %.4f, %.4f, %.4f]" % (
                    epoch, psnr_val_rgb1, psnr_val_rgb2, psnr_val_rgb6, psnr_val_rgb7, best_epoch, best_psnr[0], best_psnr[1], best_psnr[2], best_psnr[3]))

            torch.save({'epoch': epoch,
                        'state_dict_G': model_restoration1.state_dict(),
                        'optimizer': optimizer1.state_dict()
                        }, os.path.join(model_dir1, f"F17_N9_model_epoch_{epoch}.pth"))
            torch.save({'epoch': epoch,
                        'state_dict_G': model_restoration2.state_dict(),
                        'optimizer': optimizer2.state_dict()
                        }, os.path.join(model_dir2, f"F26_N9_model_epoch_{epoch}.pth"))

        scheduler1.step()
        scheduler2.step()

        print("------------------------------------------------------------------")
        print(
            "Epoch: {}\tTime: {:.4f}\tLoss: {:.4f}\tLearningRate {:.6f}, {:.6f}".format(
                epoch,
                time.time() - epoch_start_time,
                epoch_loss,
                scheduler1.get_lr()[0],
                scheduler2.get_lr()[0]
            ))
        print("------------------------------------------------------------------")

        torch.save({'epoch': epoch,
                    'state_dict_G': model_restoration1.state_dict(),
                    'optimizer': optimizer1.state_dict()
                    }, os.path.join(model_dir1, f'F17_N9_model_latest.pth'))

        torch.save({'epoch': epoch,
                    'state_dict_G': model_restoration2.state_dict(),
                    'optimizer': optimizer2.state_dict()
                    }, os.path.join(model_dir2, f'F26_N9_model_latest.pth'))
