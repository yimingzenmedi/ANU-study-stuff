import os
import sys
import torch
import argparse
import numpy as np
from tensorboardX import SummaryWriter

from dataloader import *
from model_deblur_net import *
from generic_train_test import *

##===================================================##
##********** Configure training settings ************##
##===================================================##
torch.cuda.set_device(0)

parser = argparse.ArgumentParser()
parser.add_argument('--batch_sz', type=int, default=1, help='batch size used for training')
parser.add_argument('--continue_train', type=bool, default=False,
                    help='flags used to indicate if train model from previous trained weight')
parser.add_argument('--is_training', type=bool, default=False,
                    help='flag used for selecting training mode or evaluation mode')
parser.add_argument('--n_channels', type=int, default=3, help='number of channels of input/output image')
parser.add_argument('--n_init_feat', type=int, default=32, help='number of channels of initial features')
parser.add_argument('--seq_len', type=int, default=1)
parser.add_argument('--shuffle_data', type=bool, default=False)

parser.add_argument('--crop_sz_H', type=int, default=480, help='cropped image size height')
parser.add_argument('--crop_sz_W', type=int, default=640, help='cropped image size width')
parser.add_argument('--model_label', type=str, default='500', help='label used to load pre-trained model')

parser.add_argument('--dataset_root_dir', type=str, required=True)
parser.add_argument('--log_dir', type=str, required=True)
parser.add_argument('--results_dir', type=str, required=True, help='directory used to store experimental results')

parser.add_argument('--compute_metrics', action='store_true')
parser.add_argument('--save_images', action='store_true')

opts = parser.parse_args()


##===================================================##
##**************** Test the network ****************##
##===================================================##

class Test(Generic_train_test):
    def decode_input(self, data):
        return [data['A'], data['B']]


if __name__ == '__main__':
    print("> Start...")
    ##===================================================##
    ##*************** Create dataloader *****************##
    ##===================================================##
    dataloader = Create_dataloader_inference(opts)

    ##===================================================##
    ##****************** Create model *******************##
    ##===================================================##
    model = ModelDeblurNet(opts)
    Test(model, opts, dataloader, None).test()
