import argparse
import torch
from collections import OrderedDict
import importlib
from test import test
from train import train
from carn import CARN_Net
from carn_m import CARN_M_Net
from dataset import TrainDataset, TestDataset

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


def parse_args():
    parser = argparse.ArgumentParser()

    # common config:
    parser.add_argument("--model_type", type=str, default="carn_m")
    parser.add_argument("--scale", type=int, default=4)
    parser.add_argument("--task", type=int, default=1)      # 1: test, 2: train

    # training config:
    parser.add_argument("--ckpt_name", type=str, default="carn_test")
    parser.add_argument("--print_interval", type=int, default=100)
    parser.add_argument("--train_data_path", type=str, default="../dataset/DIV2K_train.h5")
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
    parser.add_argument("--loss_fn", type=str, choices=["MSE", "L1", "SmoothL1"], default="L1")
    parser.add_argument("--valid_data_dir", type=str, default="../dataset/B100")

    # test config:
    parser.add_argument("--ckpt_path", type=str, default="../checkpoint/carn.pth")
    parser.add_argument("--test_data_dir", type=str, default="../dataset/B100")
    parser.add_argument("--test_dir", type=str, default="../test_result")
    parser.add_argument("--group", type=int, default=1)

    args = parser.parse_args()

    return args


if __name__ == "__main__":

    args = parse_args()
    test_state = train_state = 0

    if args.task == 1:      # test
        test_state = 1
        train_state = 0
    elif args.task == 2:    # train
        test_state = 0
        train_state = 1
    elif args.task == 3:    # both
        test_state = 1
        train_state = 1
    else:
        print("Task should choose from 1 to 3")

    if test_state == 1:

        if args.model_type == "carn":
            net = CARN_Net().to(device)
        else:
            net = CARN_M_Net().to(device)

        state_dict = torch.load(args.ckpt_path)
        new_state_dict = OrderedDict()

        for k, v in state_dict.items():
            new_state_dict[k] = v

        net.load_state_dict(new_state_dict)

        dataset = TestDataset(args.test_data_dir, args.scale)

        test(args, net, dataset)

    if train_state == 1:
        print(args)
        net = None
        if args.model_type in "carn":
            net = importlib.import_module("{}".format(args.model_type)).CARN_Net
        else:
            net = importlib.import_module("{}".format(args.model_type)).CARN_M_Net

        train = train(args, net)
        train.training()
