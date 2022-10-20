import os
import random

import numpy as np
import torch
import torchvision.transforms.functional as TF
from PIL import Image
from torch.utils.data import Dataset


def is_image_file(filename):
    return any(filename.endswith(extension) for extension in ['jpeg', 'JPEG', 'jpg', 'png', 'JPG', 'PNG', 'gif'])


class DataLoaderTrain(Dataset):
    def __init__(self, rgb_dir, group_size, pic_index, img_options=None):
        super(DataLoaderTrain, self).__init__()

        inp_files = sorted(os.listdir(os.path.join(rgb_dir, 'blurry')))
        tar_files_all = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))
        self.inp_filenames = [os.path.join(rgb_dir, 'blurry', x) for x in inp_files if is_image_file(x)]
        self.group_size = group_size
        self.pic_index = pic_index

        # print("!! Not Center")
        pic_index3 = pic_index[0]
        pic_index5 = pic_index[1]
        tar_files3 = []
        tar_files5 = []
        for i in range(len(tar_files_all)):
            if i % group_size == pic_index3:
                tar_files3.append(tar_files_all[i])
            elif i % group_size == pic_index5:
                tar_files5.append(tar_files_all[i])
        tar_filenames3 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files3 if is_image_file(x)]
        tar_filenames5 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files5 if is_image_file(x)]
        self.tar_filenames_set = [tar_filenames3, tar_filenames5]

        self.img_options = img_options
        self.sizex = len(self.inp_filenames)  # get the size of target

        self.ps = self.img_options['patch_size']

        self.group_size = group_size
        self.pic_index = pic_index

    def __len__(self):
        return self.sizex

    def __getitem__(self, index):
        index_ = index % self.sizex
        ps = self.ps

        # print("!!! inp_filenames:", len(self.inp_filenames), ", tar_filenames:", self.sizex, ", index_:", index_)
        inp_path = self.inp_filenames[index_]
        inp_img = Image.open(inp_path)

        w, h = inp_img.size
        padw = ps - w if w < ps else 0
        padh = ps - h if h < ps else 0

        # print(f">> tar_filenames_set: {len(self.tar_filenames_set)}, child len: {len(self.tar_filenames_set[0])}, index_: {index_}")
        # print(f">> tar_path3: {self.tar_filenames_set[0][index_]}")
        # print(f">> tar_path5: {self.tar_filenames_set[1][index_]}")

        tar_path3 = self.tar_filenames_set[0][index_]
        tar_path5 = self.tar_filenames_set[1][index_]
        tar_img3 = Image.open(tar_path3)
        tar_img5 = Image.open(tar_path5)

        # Reflect Pad in case image is smaller than patch_size
        if padw != 0 or padh != 0:
            inp_img = TF.pad(inp_img, (0, 0, padw, padh), padding_mode='reflect')
            tar_img3 = TF.pad(tar_img3, (0, 0, padw, padh), padding_mode='reflect')
            tar_img5 = TF.pad(tar_img5, (0, 0, padw, padh), padding_mode='reflect')

        aug = random.randint(0, 2)
        if aug == 1:
            inp_img = TF.adjust_gamma(inp_img, 1)
            tar_img3 = TF.adjust_gamma(tar_img3, 1)
            tar_img5 = TF.adjust_gamma(tar_img5, 1)

        aug = random.randint(0, 2)
        if aug == 1:
            sat_factor = 1 + (0.2 - 0.4 * np.random.rand())
            inp_img = TF.adjust_saturation(inp_img, sat_factor)
            tar_img3 = TF.adjust_saturation(tar_img3, sat_factor)
            tar_img5 = TF.adjust_saturation(tar_img5, sat_factor)

        inp_img = TF.to_tensor(inp_img)
        tar_img3 = TF.to_tensor(tar_img3)
        tar_img5 = TF.to_tensor(tar_img5)

        hh, ww = tar_img3.shape[1], tar_img3.shape[2]

        rr = random.randint(0, hh - ps)
        cc = random.randint(0, ww - ps)
        aug = random.randint(0, 8)

        # Crop patch
        inp_img = inp_img[:, rr:rr + ps, cc:cc + ps]
        tar_img3 = tar_img3[:, rr:rr + ps, cc:cc + ps]
        tar_img5 = tar_img5[:, rr:rr + ps, cc:cc + ps]

        # Data Augmentations
        if aug == 1:
            inp_img = inp_img.flip(1)
            tar_img3 = tar_img3.flip(1)
            tar_img5 = tar_img5.flip(1)
        elif aug == 2:
            inp_img = inp_img.flip(2)
            tar_img3 = tar_img3.flip(2)
            tar_img5 = tar_img5.flip(2)
        elif aug == 3:
            inp_img = torch.rot90(inp_img, dims=(1, 2))
            tar_img3 = torch.rot90(tar_img3, dims=(1, 2))
            tar_img5 = torch.rot90(tar_img5, dims=(1, 2))
        elif aug == 4:
            inp_img = torch.rot90(inp_img, dims=(1, 2), k=2)
            tar_img3 = torch.rot90(tar_img3, dims=(1, 2), k=2)
            tar_img5 = torch.rot90(tar_img5, dims=(1, 2), k=2)
        elif aug == 5:
            inp_img = torch.rot90(inp_img, dims=(1, 2), k=3)
            tar_img3 = torch.rot90(tar_img3, dims=(1, 2), k=3)
            tar_img5 = torch.rot90(tar_img5, dims=(1, 2), k=3)
        elif aug == 6:
            inp_img = torch.rot90(inp_img.flip(1), dims=(1, 2))
            tar_img3 = torch.rot90(tar_img3.flip(1), dims=(1, 2))
            tar_img5 = torch.rot90(tar_img5.flip(1), dims=(1, 2))
        elif aug == 7:
            inp_img = torch.rot90(inp_img.flip(2), dims=(1, 2))
            tar_img3 = torch.rot90(tar_img3.flip(2), dims=(1, 2))
            tar_img5 = torch.rot90(tar_img5.flip(2), dims=(1, 2))

        tar_img_set = [tar_img3, tar_img5]

        filename3 = os.path.splitext(os.path.split(tar_path3)[-1])[0]
        filename5 = os.path.splitext(os.path.split(tar_path5)[-1])[0]
        filename_set = [filename3, filename5]

        return tar_img_set, inp_img, filename_set


class DataLoaderVal(Dataset):
    def __init__(self, rgb_dir, group_size, pic_index, img_options=None):
        super(DataLoaderVal, self).__init__()
        self.group_size = group_size
        self.pic_index = pic_index

        inp_files = sorted(os.listdir(os.path.join(rgb_dir, 'blurry')))
        # tar_files = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))

        tar_files_all = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))

        pic_index3 = pic_index[0]
        pic_index5 = pic_index[1]
        tar_files3 = []
        tar_files5 = []
        for i in range(len(tar_files_all)):
            if i % group_size == pic_index3:
                tar_files3.append(tar_files_all[i])
            elif i % group_size == pic_index5:
                tar_files5.append(tar_files_all[i])
        tar_filenames3 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files3 if is_image_file(x)]
        tar_filenames5 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files5 if is_image_file(x)]
        self.tar_filenames_set = [tar_filenames3, tar_filenames5]

        self.inp_filenames = [os.path.join(rgb_dir, 'blurry', x) for x in inp_files if is_image_file(x)]

        self.img_options = img_options
        self.sizex = len(self.inp_filenames)  # get the size of target

        self.ps = self.img_options['patch_size']

    def __len__(self):
        return self.sizex

    def __getitem__(self, index):
        index_ = index % self.sizex
        ps = self.ps

        inp_path = self.inp_filenames[index_]


        # else:
        tar_path3 = self.tar_filenames_set[0][index_]
        tar_path5 = self.tar_filenames_set[1][index_]

        inp_img = Image.open(inp_path)
        tar_img3 = Image.open(tar_path3)
        tar_img5 = Image.open(tar_path5)

        # print("!! ps:", self.ps)
        # Validate on center crop
        if self.ps is not None:
            inp_img = TF.center_crop(inp_img, [ps, ps])
            tar_img3 = TF.center_crop(tar_img3, [ps, ps])
            tar_img5 = TF.center_crop(tar_img5, [ps, ps])

        inp_img = TF.to_tensor(inp_img)
        tar_img3 = TF.to_tensor(tar_img3)
        tar_img5 = TF.to_tensor(tar_img5)
        tar_img_set = [tar_img3, tar_img5]
        filename3 = os.path.splitext(os.path.split(tar_path3)[-1])[0]
        filename5 = os.path.splitext(os.path.split(tar_path5)[-1])[0]
        filename_set = [filename3, filename5]

        return tar_img_set, inp_img, filename_set


class DataLoaderTest(Dataset):
    def __init__(self, inp_dir, img_options):
        super(DataLoaderTest, self).__init__()

        inp_files = sorted(os.listdir(inp_dir))
        self.inp_filenames = [os.path.join(inp_dir, x) for x in inp_files if is_image_file(x)]

        self.inp_size = len(self.inp_filenames)
        self.img_options = img_options

    def __len__(self):
        return self.inp_size

    def __getitem__(self, index):
        path_inp = self.inp_filenames[index]
        filename = os.path.splitext(os.path.split(path_inp)[-1])[0]
        inp = Image.open(path_inp)

        inp = TF.to_tensor(inp)
        return inp, filename
