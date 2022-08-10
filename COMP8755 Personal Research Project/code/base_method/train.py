"""
This file refers to https://github.com/nmhkahn/CARN-pytorch and made changes.
"""
import importlib as importlib
import os
from tensorboardX import SummaryWriter
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader
from dataset import TrainDataset, TestDataset
from model import centerEsti, F35_N8, F26_N9, F17_N9
from utils import *
import argparse


class train():
    def __init__(self, args, model):

        self.args = args
        self.refiner = model().to(device)
        self.device = device
        self.loss_fn = nn.L1Loss()
        self.optim = optim.Adam(filter(lambda p: p.requires_grad, self.refiner.parameters()), self.args.lr)

        self.train_data = TrainDataset(self.args.train_data_path,   # scale=self.args.scale,
                                       size=self.args.patch_size)

        self.train_loader = DataLoader(self.train_data,
                                       batch_size=self.args.batch_size,
                                       num_workers=1,
                                       shuffle=True, drop_last=True)

        self.step = 0

        self.writer = SummaryWriter(log_dir=os.path.join("runs", self.args.ckpt_name))
        self.psnr_list = []

        os.makedirs(self.args.ckpt_dir, exist_ok=True)

    def training(self):

        refiner = nn.DataParallel(self.refiner, device_ids=range(self.args.num_gpu))

        learning_rate = self.args.lr

        while True:
            for inputs in self.train_loader:

                self.refiner.train()

                hr, lr = inputs[-1][0], inputs[-1][1]

                hr = hr.to(device)
                lr = lr.to(device)

                sr = refiner(lr, self.args.scale)
                loss = self.loss_fn(sr, hr)

                self.optim.zero_grad()
                loss.backward()
                nn.utils.clip_grad_norm_(self.refiner.parameters(), self.args.clip)
                self.optim.step()

                learning_rate = self.decay_learning_rate()
                for param_group in self.optim.param_groups:
                    param_group["lr"] = learning_rate

                self.step += 1
                if self.args.verbose and self.step % self.args.print_interval == 0:
                    psnr = self.evaluate(self.args.test_data_path)
                    print("PSNR:", psnr)
                    self.writer.add_scalar("Set14", psnr, self.step)

                    self.save()

                print("step:", self.step)

            if self.step > self.args.max_steps:
                break

    def evaluate(self, test_data_dir):

        mean_psnr = 0
        # self.refiner.eval()

        test_data = TestDataset(test_data_dir)
        test_loader = DataLoader(test_data,
                                 batch_size=1,
                                 num_workers=1,
                                 shuffle=False)
        # for step, inputs in enumerate(test_loader):
        #     hr = inputs[0].squeeze(0)
        #     lr = inputs[1].squeeze(0)
        #     # name = inputs[2][0]

            # h, w = lr.size()[1:]
            # h_half, w_half = int(h / 2), int(w / 2)
            # h_chop, w_chop = h_half + self.args.shave, w_half + self.args.shave

            # # split large image to 4 patch to avoid OOM error
            # lr_patch = torch.FloatTensor(4, 3, h_chop, w_chop)
            # lr_patch[0].copy_(lr[:, 0:h_chop, 0:w_chop])
            # lr_patch[1].copy_(lr[:, 0:h_chop, w - w_chop:w])
            # lr_patch[2].copy_(lr[:, h - h_chop:h, 0:w_chop])
            # lr_patch[3].copy_(lr[:, h - h_chop:h, w - w_chop:w])
            # lr_patch = lr_patch.to(device)

            # # run refine process in here!
            # sr = self.refiner(lr_patch, scale).data
            #
            # h, h_half, h_chop = h * scale, h_half * scale, h_chop * scale
            # w, w_half, w_chop = w * scale, w_half * scale, w_chop * scale

            # merge splited patch images
            # result = torch.FloatTensor(3, h, w).to(self.device)
            # result[:, 0:h_half, 0:w_half].copy_(sr[0, :, 0:h_half, 0:w_half])
            # result[:, 0:h_half, w_half:w].copy_(sr[1, :, 0:h_half, w_chop - w + w_half:w_chop])
            # result[:, h_half:h, 0:w_half].copy_(sr[2, :, h_chop - h + h_half:h_chop, 0:w_half])
            # result[:, h_half:h, w_half:w].copy_(sr[3, :, h_chop - h + h_half:h_chop, w_chop - w + w_half:w_chop])
            # sr = result

            # hr = hr.cpu().mul(255).clamp(0, 255).byte().permute(1, 2, 0).numpy()
            # sr = sr.cpu().mul(255).clamp(0, 255).byte().permute(1, 2, 0).numpy()

            # evaluate PSNR
            # this evaluation is different to MATLAB version
            # we evaluate PSNR in RGB channel not Y in YCbCR
            # bnd = scale
            # im1 = hr[bnd:-bnd, bnd:-bnd]
            # im2 = sr[bnd:-bnd, bnd:-bnd]
            # # mean_psnr += psnr(im1, im2) / len(test_data)
            # mean_psnr += get_psnr(im1, im2) / len(test_data)

            # print("measure step:", step)
            # print(utils.get_psnr(im1, im2) / len(test_data))

        return mean_psnr

    def save(self):
        save_path = os.path.join(self.args.ckpt_dir, "{}_{}.pth".format(self.args.ckpt_name, self.step))
        print("save to:", save_path)
        torch.save(self.refiner.state_dict(), save_path)

    def decay_learning_rate(self):
        lr = self.args.lr * (0.5 ** (self.step // self.args.decay))
        return lr


def parse_args():
    parser = argparse.ArgumentParser()

    # config:
    parser.add_argument("--scale", type=int, default=4)
    parser.add_argument("--ckpt_name", type=str, default="carn_test")
    parser.add_argument("--print_interval", type=int, default=100)
    parser.add_argument("--train_data_path", type=str, default="datasets/video_train.h5")
    parser.add_argument("--test_data_path", type=str, default="datasets/video_test.h5")
    parser.add_argument("--ckpt_dir", type=str, default="../checkpoint/m")
    parser.add_argument("--num_gpu", type=int, default=1)
    parser.add_argument("--shave", type=int, default=20)
    parser.add_argument("--verbose", action="store_true", default="store_true")
    parser.add_argument("--patch_size", type=int, default=32)
    parser.add_argument("--batch_size", type=int, default=32)
    parser.add_argument("--max_steps", type=int, default=200000)
    parser.add_argument("--decay", type=int, default=400000)
    parser.add_argument("--lr", type=float, default=0.0001)
    parser.add_argument("--clip", type=float, default=10.0)
    # parser.add_argument("--loss_fn", type=str, choices=["MSE", "L1", "SmoothL1"], default="L1")
    parser.add_argument("--net", type=int, default=1, choices=[1, 2, 3, 4])
    parser.add_argument("--cuda", type=bool, default=False)

    args = parser.parse_args()

    return args


if __name__ == "__main__":

    args = parse_args()

    device = torch.device("cuda" if torch.cuda.is_available() and args.cuda else "cpu")

    # load model
    # model1 = centerEsti()
    # model2 = F35_N8()
    # model3 = F26_N9()
    # model4 = F17_N9()
    #
    # if args.cuda:
    #     model1.cuda()
    #     model2.cuda()
    #     model3.cuda()
    #     model4.cuda()

    # model1.eval()
    # model2.eval()
    # model3.eval()
    # model4.eval()

    # net1 = importlib.import_module("centerEsti")
    # net2 = importlib.import_module("F35_N8")
    # net3 = importlib.import_module("F26_N9")
    # net4 = importlib.import_module("F17_N9")
    net1 = centerEsti
    net2 = F35_N8
    net3 = F26_N9
    net4 = F17_N9

    if args.net == 1:
        train = train(args, net1)
    elif args.net == 2:
        train = train(args, net2)
    elif args.net == 3:
        train = train(args, net3)
    elif args.net == 4:
        train = train(args, net4)
    else:
        print("arg 'train' should be 1 to 4")
        exit(1)
    train.training()
