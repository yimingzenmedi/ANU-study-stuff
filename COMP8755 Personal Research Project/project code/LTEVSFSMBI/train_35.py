from difflib import restore
import os

from config import Config

opt = Config('training_35.yml')

gpus = ','.join([str(i) for i in opt.GPU])
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = gpus

import torch

torch.backends.cudnn.benchmark = True

import torch.nn as nn
import torch.optim as optim
import torch.nn.functional as F
from torch.autograd import Variable
from torch.utils.data import DataLoader

import random
import time
import numpy as np

import utils
from data_RGB_2 import get_training_data, get_validation_data
from LTEVSFSMBI import centerEsti, F35_N8
from FCDiscriminator import FCDiscriminator
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

    new_lr = opt.OPTIM.LR_INITIAL

    group_size = 7

    ######### Model ###########
    model_center = centerEsti()
    model_center.cuda()
    model_restoration = F35_N8()
    model_restoration.cuda()
    # discriminator = FCDiscriminator()
    # discriminator.cuda()
    # discriminator_params = discriminator.parameters()
    # discriminator_optimizer = optim.Adam(discriminator_params, new_lr)

    pic_index = [2, 4]

    ######### Loss ###########
    # criterion_char = losses.CharbonnierLoss()
    # criterion_edge = losses.EdgeLoss()
    criterion = losses.F35_N8Loss()

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
        path_chk_rest = utils.get_last_path(model_dir, mode + '_model_latest.pth')
        utils.load_checkpoint(model_restoration, path_chk_rest)
        start_epoch = utils.load_start_epoch(path_chk_rest) + 1
        # utils.load_optim(optimizer, path_chk_rest)

        for i in range(1, start_epoch):
            scheduler.step()
        new_lr = scheduler.get_lr()[0]
        print('------------------------------------------------------------------------------')
        print("==> Resuming Training with learning rate:", new_lr)
        print('------------------------------------------------------------------------------')

    if len(device_ids) > 1:
        model_restoration = nn.DataParallel(model_restoration, device_ids=device_ids)

    ######### DataLoaders ###########

    train_dataset = get_training_data(train_dir, {'patch_size': opt.TRAINING.TRAIN_PS},
                                      group_size=group_size, pic_index=pic_index)
    val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS},
                                      group_size=group_size, pic_index=pic_index)

    train_loader = DataLoader(dataset=train_dataset, batch_size=opt.OPTIM.BATCH_SIZE, shuffle=True, num_workers=8,
                              drop_last=False, pin_memory=True)
    val_loader = DataLoader(dataset=val_dataset, batch_size=opt.OPTIM.VAL_BATCH_SIZE, shuffle=False, num_workers=8,
                            drop_last=False, pin_memory=True)

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

            input_ = data[1].cuda()

            target1 = data[0][0].cuda()
            target2 = data[0][1].cuda()
            centerEsti_model = centerEsti().cuda()
            centerEsit_model_path = os.path.join(opt.TRAINING.CENTER_MODEL_DIR, "centerEsti", 'models', session,
                                                 "centerEsti_model_best.pth")
            centerEsti_ckpt = torch.load(centerEsit_model_path)
            # print(f">> train: input_: {input_.shape}")
            restored4 = centerEsti_model(input_)
            restored4 = restored4.data * 255

            # print("input_:", input_.shape, ", restored4:", restored4.shape)

            restored = model_restoration(input_, restored4)
            # print("restored:", restored)

            #######################################################
            # discriminator:
            # CE = torch.nn.BCEWithLogitsLoss()
            #
            # dis_output1 = discriminator(target1, restored[0])
            # dis_output1 = F.interpolate(dis_output1, size=(target1.shape[2], target1.shape[3]),
            #                             mode="bilinear", align_corners=True)
            # labels1 = Variable(torch.FloatTensor(np.ones(dis_output1.shape))).cuda()
            # dis_output1_loss = CE(dis_output1, labels1) * np.prod(dis_output1.shape)
            # # print("> dis_output1_loss:", dis_output1_loss)
            #
            # dis_output2 = discriminator(target2, restored[1])
            # dis_output2 = F.interpolate(dis_output2, size=(target2.shape[2], target2.shape[3]),
            #                             mode="bilinear", align_corners=True)
            # labels2 = Variable(torch.FloatTensor(np.ones(dis_output2.shape))).cuda()
            # dis_output2_loss = CE(dis_output2, labels2) * np.prod(dis_output1.shape)
            # print("> dis_output2_loss:", dis_output2_loss)

            # dis_output_loss = dis_output1_loss + dis_output2_loss
            # print("> dis_output_loss:", dis_output_loss.data)
            #
            criterion_loss = criterion(restored[0], restored[1], target1, target2)
            print("> criterion_loss:", criterion_loss.data)
            #
            # loss = criterion_loss + dis_output_loss * 1
            # print("> loss:", loss.data)
            loss = criterion_loss

            loss.backward()
            optimizer.step()
            #
            # ##########################################################################################
            # train discriminator:
            # torch.autograd.set_detect_anomaly(True)
            #
            # dis_pred1 = torch.sigmoid(restored[0]).detach()
            # dis_output1 = F.interpolate(discriminator(target1, dis_pred1),
            #                             size=(target1.shape[2], target1.shape[3]),
            #                             mode="bilinear", align_corners=True)
            # dis_output1_loss = CE(dis_output1, labels1)
            # # print(f"> target1: {target1.shape}, labels1: {labels1.shape}, restored[0]: {restored[0].shape}, "
            # #       f"dis_output1: {dis_output1.shape}, dis_output1_loss: {dis_output1_loss.shape}")
            # dis_target1 = discriminator(target1, target1)
            # dis_target1 = F.interpolate(dis_target1, size=(target1.shape[2], target1.shape[3]),
            #                             mode="bilinear", align_corners=True)
            # dis_target1_loss = CE(dis_target1, labels1)
            #
            # dis_pred2 = torch.sigmoid(restored[1]).detach()
            # dis_output2 = F.interpolate(discriminator(target2, dis_pred2),
            #                             size=(target2.shape[2], target2.shape[3]),
            #                             mode="bilinear", align_corners=True)
            # dis_output2_loss = CE(dis_output2, labels2)
            #
            # dis_target2 = discriminator(target2, target2)
            # dis_target2 = F.interpolate(dis_target2, size=(target2.shape[2], target2.shape[3]),
            #                             mode="bilinear", align_corners=True)
            # dis_target2_loss = CE(dis_target2, labels2)
            #
            # dis_loss = 0.25 * (dis_output1_loss + dis_target1_loss + dis_output2_loss + dis_target2_loss)
            # # print("dis_loss:", dis_loss)
            # dis_loss.backward()
            # discriminator_optimizer.step()

            # loss.backward()
            # optimizer.step()
            epoch_loss += loss.item()

        #### Evaluation ####
        if epoch % opt.TRAINING.VAL_AFTER_EVERY == 0:
            print("> Evaluation")
            model_restoration.eval()
            psnr_val_rgb = []
            val_loader_emur = enumerate(val_loader, 0)
            print("> val_loader:", len(val_loader))

            for ii, data_val in val_loader_emur:
                target1 = data_val[0][0].cuda()
                target2 = data_val[0][1].cuda()
                input_ = data_val[1].cuda()

                # centerEsti_model = centerEsti().cuda()
                # centerEsit_model_path = os.path.join(opt.TRAINING.CENTER_MODEL_DIR, "centerEsti", 'models', session,
                #                                      "centerEsti_model_latest.pth")
                # centerEsti_ckpt = torch.load(centerEsit_model_path)
                restored4 = centerEsti_model(input_)
                restored4 = restored4.data * 255

                with torch.no_grad():
                    restored = model_restoration(input_, restored4)
                restored1 = restored[0]
                restored2 = restored[1]

                # print("> target:", target, ",\ninput_:", input_, ",\nrestored:", restored)

                for res1, res2, tar1, tar2 in zip(restored1, restored2, target1, target2):
                    # print(f"> res1: {res1.shape}, res2: {res2.shape}, tar1: {tar1.shape}, tar2: {tar2.shape}")
                    psnr_val_rgb.append(utils.torchPSNR(res1, tar1))
                    psnr_val_rgb.append(utils.torchPSNR(res2, tar2))
                    # print("psnr_val_rgb appended. Now:", psnr_val_rgb)

            psnr_val_rgb = torch.stack(psnr_val_rgb).mean().item()

            if psnr_val_rgb > best_psnr:
                best_psnr = psnr_val_rgb
                best_epoch = epoch
                torch.save({'epoch': epoch,
                            'state_dict_G': model_restoration.state_dict(),
                            'optimizer': optimizer.state_dict()
                            }, os.path.join(model_dir, mode + "_model_best.pth"))

            print(
                "[epoch %d PSNR: %.4f --- best_epoch %d Best_PSNR %.4f]" % (epoch, psnr_val_rgb, best_epoch, best_psnr))

            torch.save({'epoch': epoch,
                        'state_dict_G': model_restoration.state_dict(),
                        'optimizer': optimizer.state_dict()
                        }, os.path.join(model_dir, f"{mode}_model_epoch_{epoch}.pth"))

        scheduler.step()

        print("------------------------------------------------------------------")
        print("Epoch: {}\tTime: {:.4f}\tLoss: {:.4f}\tLearningRate {:.6f}".format(epoch, time.time() - epoch_start_time,
                                                                                  epoch_loss, scheduler.get_lr()[0]))
        print("------------------------------------------------------------------")

        torch.save({'epoch': epoch,
                    'state_dict_G': model_restoration.state_dict(),
                    'optimizer': optimizer.state_dict()
                    }, os.path.join(model_dir, mode + "_model_latest.pth"))