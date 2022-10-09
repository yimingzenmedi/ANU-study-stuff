import os
import random

import numpy as np
import torch
import torchvision.transforms.functional as TF
from PIL import Image
from torch.utils.data import Dataset


def is_image_file(filename):
    return any(filename.endswith(extension) for extension in ['jpeg', 'JPEG', 'jpg', 'png', 'JPG', 'PNG', 'gif'])


def is_mid_index(group_size, pic_index):
    return group_size // 2 == pic_index


class DataLoaderTrain(Dataset):
    def __init__(self, rgb_dir, group_size, pic_index, img_options=None):
        super(DataLoaderTrain, self).__init__()

        inp_files = sorted(os.listdir(os.path.join(rgb_dir, 'blurry')))
        tar_files_all = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))
        self.inp_filenames = [os.path.join(rgb_dir, 'blurry', x) for x in inp_files if is_image_file(x)]
        self.group_size = group_size
        self.pic_index = pic_index

        # print("!! Not Center")
        pic_index1 = pic_index[0]
        pic_index2 = pic_index[1]
        pic_index6 = pic_index[2]
        pic_index7 = pic_index[3]
        tar_files1 = []
        tar_files2 = []
        tar_files6 = []
        tar_files7 = []
        for i in range(len(tar_files_all)):
            if i % group_size == pic_index1:
                tar_files1.append(tar_files_all[i])
            elif i % group_size == pic_index2:
                tar_files2.append(tar_files_all[i])
            elif i % group_size == pic_index6:
                tar_files6.append(tar_files_all[i])
            elif i % group_size == pic_index7:
                tar_files7.append(tar_files_all[i])
        tar_filenames1 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files1 if is_image_file(x)]
        tar_filenames2 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files2 if is_image_file(x)]
        tar_filenames6 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files2 if is_image_file(x)]
        tar_filenames7 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files2 if is_image_file(x)]
        self.tar_filenames_set = [tar_filenames1, tar_filenames2, tar_filenames6, tar_filenames7]

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
        # print(f">> tar_path1: {self.tar_filenames_set[0][index_]}")
        # print(f">> tar_path2: {self.tar_filenames_set[1][index_]}")

        tar_path1 = self.tar_filenames_set[0][index_]
        tar_path2 = self.tar_filenames_set[1][index_]
        tar_path6 = self.tar_filenames_set[2][index_]
        tar_path7 = self.tar_filenames_set[3][index_]
        tar_img1 = Image.open(tar_path1)
        tar_img2 = Image.open(tar_path2)
        tar_img6 = Image.open(tar_path6)
        tar_img7 = Image.open(tar_path7)

        # Reflect Pad in case image is smaller than patch_size
        if padw != 0 or padh != 0:
            inp_img = TF.pad(inp_img, (0, 0, padw, padh), padding_mode='reflect')
            tar_img1 = TF.pad(tar_img1, (0, 0, padw, padh), padding_mode='reflect')
            tar_img2 = TF.pad(tar_img2, (0, 0, padw, padh), padding_mode='reflect')
            tar_img6 = TF.pad(tar_img6, (0, 0, padw, padh), padding_mode='reflect')
            tar_img7 = TF.pad(tar_img7, (0, 0, padw, padh), padding_mode='reflect')

        aug = random.randint(0, 2)
        if aug == 1:
            inp_img = TF.adjust_gamma(inp_img, 1)
            tar_img1 = TF.adjust_gamma(tar_img1, 1)
            tar_img2 = TF.adjust_gamma(tar_img2, 1)
            tar_img6 = TF.adjust_gamma(tar_img6, 1)
            tar_img7 = TF.adjust_gamma(tar_img7, 1)

        aug = random.randint(0, 2)
        if aug == 1:
            sat_factor = 1 + (0.2 - 0.4 * np.random.rand())
            inp_img = TF.adjust_saturation(inp_img, sat_factor)
            tar_img1 = TF.adjust_saturation(tar_img1, sat_factor)
            tar_img2 = TF.adjust_saturation(tar_img2, sat_factor)
            tar_img6 = TF.adjust_saturation(tar_img6, sat_factor)
            tar_img7 = TF.adjust_saturation(tar_img7, sat_factor)

        inp_img = TF.to_tensor(inp_img)
        tar_img1 = TF.to_tensor(tar_img1)
        tar_img2 = TF.to_tensor(tar_img2)
        tar_img6 = TF.to_tensor(tar_img6)
        tar_img7 = TF.to_tensor(tar_img7)

        hh, ww = tar_img1.shape[1], tar_img1.shape[2]

        rr = random.randint(0, hh - ps)
        cc = random.randint(0, ww - ps)
        aug = random.randint(0, 8)

        # Crop patch
        inp_img = inp_img[:, rr:rr + ps, cc:cc + ps]
        tar_img1 = tar_img1[:, rr:rr + ps, cc:cc + ps]
        tar_img2 = tar_img2[:, rr:rr + ps, cc:cc + ps]
        tar_img6 = tar_img6[:, rr:rr + ps, cc:cc + ps]
        tar_img7 = tar_img7[:, rr:rr + ps, cc:cc + ps]

        # Data Augmentations
        if aug == 1:
            inp_img = inp_img.flip(1)
            tar_img1 = tar_img1.flip(1)
            tar_img2 = tar_img2.flip(1)
            tar_img6 = tar_img6.flip(1)
            tar_img7 = tar_img7.flip(1)
        elif aug == 2:
            inp_img = inp_img.flip(2)
            tar_img1 = tar_img1.flip(2)
            tar_img2 = tar_img2.flip(2)
            tar_img6 = tar_img6.flip(2)
            tar_img7 = tar_img7.flip(2)
        elif aug == 3:
            inp_img = torch.rot90(inp_img, dims=(1, 2))
            tar_img1 = torch.rot90(tar_img1, dims=(1, 2))
            tar_img2 = torch.rot90(tar_img2, dims=(1, 2))
            tar_img6 = torch.rot90(tar_img6, dims=(1, 2))
            tar_img7 = torch.rot90(tar_img7, dims=(1, 2))
        elif aug == 4:
            inp_img = torch.rot90(inp_img, dims=(1, 2), k=2)
            tar_img1 = torch.rot90(tar_img1, dims=(1, 2), k=2)
            tar_img2 = torch.rot90(tar_img2, dims=(1, 2), k=2)
            tar_img6 = torch.rot90(tar_img6, dims=(1, 2), k=2)
            tar_img7 = torch.rot90(tar_img7, dims=(1, 2), k=2)
        elif aug == 5:
            inp_img = torch.rot90(inp_img, dims=(1, 2), k=3)
            tar_img1 = torch.rot90(tar_img1, dims=(1, 2), k=3)
            tar_img2 = torch.rot90(tar_img2, dims=(1, 2), k=3)
            tar_img6 = torch.rot90(tar_img6, dims=(1, 2), k=3)
            tar_img7 = torch.rot90(tar_img7, dims=(1, 2), k=3)
        elif aug == 6:
            inp_img = torch.rot90(inp_img.flip(1), dims=(1, 2))
            tar_img1 = torch.rot90(tar_img1.flip(1), dims=(1, 2))
            tar_img2 = torch.rot90(tar_img2.flip(1), dims=(1, 2))
            tar_img6 = torch.rot90(tar_img6.flip(1), dims=(1, 2))
            tar_img7 = torch.rot90(tar_img7.flip(1), dims=(1, 2))
        elif aug == 7:
            inp_img = torch.rot90(inp_img.flip(2), dims=(1, 2))
            tar_img1 = torch.rot90(tar_img1.flip(2), dims=(1, 2))
            tar_img2 = torch.rot90(tar_img2.flip(2), dims=(1, 2))
            tar_img6 = torch.rot90(tar_img6.flip(2), dims=(1, 2))
            tar_img7 = torch.rot90(tar_img7.flip(2), dims=(1, 2))

        tar_img_set = [tar_img1, tar_img2, tar_img6, tar_img7]

        filename1 = os.path.splitext(os.path.split(tar_path1)[-1])[0]
        filename2 = os.path.splitext(os.path.split(tar_path2)[-1])[0]
        filename6 = os.path.splitext(os.path.split(tar_path6)[-1])[0]
        filename7 = os.path.splitext(os.path.split(tar_path7)[-1])[0]
        filename_set = [filename1, filename2, filename6, filename7]

        return tar_img_set, inp_img, filename_set


class DataLoaderVal(Dataset):
    def __init__(self, rgb_dir, group_size, pic_index, img_options=None):
        super(DataLoaderVal, self).__init__()
        self.group_size = group_size
        self.pic_index = pic_index

        inp_files = sorted(os.listdir(os.path.join(rgb_dir, 'blurry')))
        # tar_files = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))

        tar_files_all = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))

        pic_index1 = pic_index[0]
        pic_index2 = pic_index[1]
        pic_index6 = pic_index[2]
        pic_index7 = pic_index[3]
        tar_files1 = []
        tar_files2 = []
        tar_files6 = []
        tar_files7 = []
        for i in range(len(tar_files_all)):
            if i % group_size == pic_index1:
                tar_files1.append(tar_files_all[i])
            elif i % group_size == pic_index2:
                tar_files2.append(tar_files_all[i])
            elif i % group_size == pic_index6:
                tar_files6.append(tar_files_all[i])
            elif i % group_size == pic_index7:
                tar_files7.append(tar_files_all[i])
        tar_filenames1 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files1 if is_image_file(x)]
        tar_filenames2 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files2 if is_image_file(x)]
        tar_filenames6 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files6 if is_image_file(x)]
        tar_filenames7 = [os.path.join(rgb_dir, 'sharp', x) for x in tar_files7 if is_image_file(x)]
        self.tar_filenames_set = [tar_filenames1, tar_filenames2, tar_filenames6, tar_filenames7]

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
        tar_path1 = self.tar_filenames_set[0][index_]
        tar_path2 = self.tar_filenames_set[1][index_]
        tar_path6 = self.tar_filenames_set[2][index_]
        tar_path7 = self.tar_filenames_set[3][index_]

        inp_img = Image.open(inp_path)
        tar_img1 = Image.open(tar_path1)
        tar_img2 = Image.open(tar_path2)
        tar_img6 = Image.open(tar_path6)
        tar_img7 = Image.open(tar_path7)

        # print("!! ps:", self.ps)
        # Validate on center crop
        if self.ps is not None:
            inp_img = TF.center_crop(inp_img, [ps, ps])
            tar_img1 = TF.center_crop(tar_img1, [ps, ps])
            tar_img2 = TF.center_crop(tar_img2, [ps, ps])
            tar_img6 = TF.center_crop(tar_img6, [ps, ps])
            tar_img7 = TF.center_crop(tar_img7, [ps, ps])

        inp_img = TF.to_tensor(inp_img)
        tar_img1 = TF.to_tensor(tar_img1)
        tar_img2 = TF.to_tensor(tar_img2)
        tar_img6 = TF.to_tensor(tar_img6)
        tar_img7 = TF.to_tensor(tar_img7)
        tar_img_set = [tar_img1, tar_img2, tar_img6, tar_img7]
        filename1 = os.path.splitext(os.path.split(tar_path1)[-1])[0]
        filename2 = os.path.splitext(os.path.split(tar_path2)[-1])[0]
        filename6 = os.path.splitext(os.path.split(tar_path6)[-1])[0]
        filename7 = os.path.splitext(os.path.split(tar_path7)[-1])[0]
        filename_set = [filename1, filename2, filename6, filename7]

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
