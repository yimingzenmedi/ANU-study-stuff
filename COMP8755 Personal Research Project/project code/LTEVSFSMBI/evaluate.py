import os

from config import Config

opt = Config('./evaluate.yml')

gpus = ','.join([str(i) for i in opt.GPU])
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = gpus

import torch

torch.backends.cudnn.benchmark = True

import torch.nn as nn
from torch.utils.data import DataLoader

import random

import utils
from data_RGB import get_validation_data
from MPRNet import MPRNet

import os
import numpy as np

if __name__ == '__main__':

    ######### Set Seeds ###########
    random.seed(1234)
    np.random.seed(1234)
    torch.manual_seed(1234)
    torch.cuda.manual_seed_all(1234)

    start_epoch = 1
    mode = opt.MODEL.MODE
    session = opt.MODEL.SESSION
    val_dir = opt.TRAINING.VAL_DIR

    index1 = 1
    index2 = 2
    index3 = 3
    index4 = 4
    index5 = 5
    index6 = 6
    index7 = 7

    pic_index = [0, 1, 2, 3, 4, 5, 6]

    model_dir1 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F17_1")
    model_dir2 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F26_2")
    model_dir3 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F35_3")
    model_dir4 = os.path.join(opt.TRAINING.SAVE_DIR, session, "centerEsti")
    model_dir5 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F35_5")
    model_dir6 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F26_6")
    model_dir7 = os.path.join(opt.TRAINING.SAVE_DIR, session, "F17_7")
    model_dir_detailed = os.path.join(opt.TRAINING.SAVE_DIR, session, "detailed")

    ######### Model ###########
    model_detailed = MPRNet()
    ckpt_detailed = torch.load(os.path.join(model_dir_detailed, f'detailed_model_best.pth'))
    model_detailed.load_state_dict(ckpt_detailed['state_dict'])
    model_detailed.cuda()

    model_restoration1 = MPRNet()
    model_restoration2 = MPRNet()
    model_restoration3 = MPRNet()
    model_restoration4 = MPRNet()
    model_restoration5 = MPRNet()
    model_restoration6 = MPRNet()
    model_restoration7 = MPRNet()
    ckpt1 = torch.load(os.path.join(model_dir1, "F17_model_1_best.pth"))
    model_restoration1.load_state_dict(ckpt1['state_dict'])
    ckpt2 = torch.load(os.path.join(model_dir2, "F26_model_2_best.pth"))
    model_restoration2.load_state_dict(ckpt1['state_dict'])
    ckpt3 = torch.load(os.path.join(model_dir3, "F35_model_3_best.pth"))
    model_restoration3.load_state_dict(ckpt1['state_dict'])
    ckpt4 = torch.load(os.path.join(model_dir4, "centerEsti_model_best.pth"))
    model_restoration4.load_state_dict(ckpt1['state_dict'])
    ckpt5 = torch.load(os.path.join(model_dir5, "F35_model_5_best.pth"))
    model_restoration5.load_state_dict(ckpt1['state_dict'])
    ckpt6 = torch.load(os.path.join(model_dir6, "F26_model_6_best.pth"))
    model_restoration6.load_state_dict(ckpt1['state_dict'])
    ckpt7 = torch.load(os.path.join(model_dir7, "F17_model_7_best.pth"))
    model_restoration7.load_state_dict(ckpt1['state_dict'])
    model_restoration1.cuda()
    model_restoration2.cuda()
    model_restoration3.cuda()
    model_restoration4.cuda()
    model_restoration5.cuda()
    model_restoration6.cuda()
    model_restoration7.cuda()

    device_ids = [i for i in range(torch.cuda.device_count())]
    if torch.cuda.device_count() > 1:
        print("\n\nLet's use", torch.cuda.device_count(), "GPUs!\n\n")

    if len(device_ids) > 1:
        model_detailed = nn.DataParallel(model_detailed, device_ids=device_ids)

    ######### DataLoaders ###########
    val_dataset = get_validation_data(val_dir, {'patch_size': opt.TRAINING.VAL_PS}, 7, pic_index)
    val_loader = DataLoader(dataset=val_dataset, batch_size=16, shuffle=False, num_workers=8, drop_last=False,
                            pin_memory=True)

    print('===> Loading datasets')

    psnr_val_rgb1 = []
    psnr_val_rgb2 = []
    psnr_val_rgb3 = []
    psnr_val_rgb4 = []
    psnr_val_rgb5 = []
    psnr_val_rgb6 = []
    psnr_val_rgb7 = []

    for ii, data_val in enumerate(val_loader, 0):
        target1 = data_val[0][0].cuda()
        target2 = data_val[0][1].cuda()
        target3 = data_val[0][2].cuda()
        target4 = data_val[0][3].cuda()
        target5 = data_val[0][4].cuda()
        target6 = data_val[0][5].cuda()
        target7 = data_val[0][6].cuda()
        input_ = data_val[1].cuda()

        with torch.no_grad():
            restored1 = model_restoration1(input_)[0]
            restored2 = model_restoration2(input_)[0]
            restored3 = model_restoration2(input_)[0]
            restored4 = model_restoration2(input_)[0]
            restored5 = model_restoration2(input_)[0]
            restored6 = model_restoration6(input_)[0]
            restored7 = model_restoration7(input_)[0]

            restored_detailed1 = model_detailed(restored1)[0]
            restored_detailed2 = model_detailed(restored2)[0]
            restored_detailed3 = model_detailed(restored3)[0]
            restored_detailed4 = model_detailed(restored4)[0]
            restored_detailed5 = model_detailed(restored5)[0]
            restored_detailed6 = model_detailed(restored6)[0]
            restored_detailed7 = model_detailed(restored7)[0]

        for res_det1, res_det2, res_det3, res_det4, res_det5, res_det6, res_det7, tar1, tar2, tar3, tar4, tar5, tar6, tar7 \
                in zip(restored_detailed1, restored_detailed2, restored_detailed3, restored_detailed4,
                       restored_detailed5, restored_detailed6, restored_detailed7,
                       target1, target2, target3, target4, target5, target6, target7):
            psnr_val_rgb1.append(utils.torchPSNR(res_det1, tar1))
            psnr_val_rgb2.append(utils.torchPSNR(res_det2, tar2))
            psnr_val_rgb3.append(utils.torchPSNR(res_det3, tar3))
            psnr_val_rgb4.append(utils.torchPSNR(res_det4, tar4))
            psnr_val_rgb5.append(utils.torchPSNR(res_det5, tar5))
            psnr_val_rgb6.append(utils.torchPSNR(res_det6, tar6))
            psnr_val_rgb7.append(utils.torchPSNR(res_det7, tar7))

    psnr_val_rgb1 = torch.stack(psnr_val_rgb1).mean().item()
    psnr_val_rgb2 = torch.stack(psnr_val_rgb2).mean().item()
    psnr_val_rgb3 = torch.stack(psnr_val_rgb3).mean().item()
    psnr_val_rgb4 = torch.stack(psnr_val_rgb4).mean().item()
    psnr_val_rgb5 = torch.stack(psnr_val_rgb5).mean().item()
    psnr_val_rgb6 = torch.stack(psnr_val_rgb6).mean().item()
    psnr_val_rgb7 = torch.stack(psnr_val_rgb7).mean().item()

    print(">>\n"
          "PSNR: %.4f, %.4f, %.4f, %.4f, %.4f, %.4f, %.4f" % (
              psnr_val_rgb1, psnr_val_rgb2, psnr_val_rgb3, psnr_val_rgb4, psnr_val_rgb5, psnr_val_rgb6,
              psnr_val_rgb7
          ))

