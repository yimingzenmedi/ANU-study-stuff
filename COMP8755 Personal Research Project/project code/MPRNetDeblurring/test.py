"""
## Multi-Stage Progressive Image Restoration
## Syed Waqas Zamir, Aditya Arora, Salman Khan, Munawar Hayat, Fahad Shahbaz Khan, Ming-Hsuan Yang, and Ling Shao
## https://arxiv.org/abs/2102.02808
"""

import os
import argparse
from tqdm import tqdm

import torch.nn as nn
import torch
from torch.utils.data import DataLoader
import torch.nn.functional as F
import utils

from data_RGB import get_test_data
from MPRNet import MPRNet
from skimage import img_as_ubyte

parser = argparse.ArgumentParser(description='Image Deblurring using MPRNet')

parser.add_argument('--result_dir', default='./results/', type=str, help='Directory for results')
parser.add_argument('--index', default=4, type=int, help='Mode')
parser.add_argument('--dataset', default='Datasets', type=str,
                    help='Test Dataset')  # ['GoPro', 'HIDE', 'RealBlur_J', 'RealBlur_R']
parser.add_argument('--gpus', default='0', type=str, help='CUDA_VISIBLE_DEVICES')

args = parser.parse_args()

os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = args.gpus


if args.index == 1 or args.index == 7 or args.index == 2 or args.index == 6:
    mode = 'F2617_N9'
    ckpt_path = f'./ckpt/{mode}/models/MPRNet/{mode}_model_{args.index}_best.pth'
elif args.index == 3 or args.index == 5:
    mode = 'F35_N8'
    ckpt_path = f'./ckpt/{mode}/models/MPRNet/{mode}_model_{args.index}_best.pth'
    # ckpt_path = f'./ckpt/{mode}/models/MPRNet/{mode}_model_{args.index}_latest.pth'
else:   # args.index == 4
    mode = 'centerEsti'
    ckpt_path = f'./ckpt/{mode}/models/MPRNet/{mode}_model_latest.pth'

print(">> ckpt_path:", ckpt_path)

model_restoration = MPRNet().cpu()
utils.load_checkpoint(model_restoration, ckpt_path, False)
print("===>Testing using weights: ", ckpt_path)


# model_restoration.cuda()
model_restoration = nn.DataParallel(model_restoration)
model_restoration.eval()

dataset = args.dataset
rgb_dir_test = dataset
test_dataset = get_test_data(rgb_dir_test, img_options={})
test_loader = DataLoader(dataset=test_dataset, batch_size=1, shuffle=False, num_workers=0, drop_last=False,
                         pin_memory=True)

result_dir = os.path.join(args.result_dir, dataset)
utils.mkdir(result_dir)

with torch.no_grad():
    for ii, data_test in enumerate(tqdm(test_loader), 0):
        # torch.cuda.ipc_collect()
        # torch.cuda.empty_cache()

        # input_ = data_test[0].cuda()
        input_ = data_test[0]
        filenames = data_test[1]

        # Padding in case images are not multiples of 8
        h, w = input_.shape[2], input_.shape[3]
        if h % 8 != 0 or w % 8 != 0:
            factor = 8
            H, W = ((h + factor) // factor) * factor, ((w + factor) // factor) * factor
            padh = H - h if h % factor != 0 else 0
            padw = W - w if w % factor != 0 else 0
            input_ = F.pad(input_, (0, padw, 0, padh), 'reflect')

        restored = model_restoration(input_)
        restored = torch.clamp(restored[0], 0, 1)

        # Unpad images to original dimensions
        if h % 8 != 0 or w % 8 != 0:
            restored = restored[:, :, :h, :w]

        restored = restored.permute(0, 2, 3, 1).detach().numpy()

        for batch in range(len(restored)):
            restored_img = img_as_ubyte(restored[batch])
            utils.save_img((os.path.join(result_dir, f'{filenames[batch]}_{args.index}.png')), restored_img)
