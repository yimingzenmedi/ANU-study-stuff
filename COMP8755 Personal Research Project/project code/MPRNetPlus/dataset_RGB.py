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

        tar_filenames_set = []

        # print(f">> pic_index: {pic_index}")
        for _ in self.pic_index:
            tar_filenames_set.append([])
        for i in range(len(tar_files_all)):
            for j in range(len(pic_index)):
                index = pic_index[j]
                if i % group_size == index:
                    tar_file = tar_files_all[i]
                    if is_image_file(tar_file):
                        tar_filenames_set[j].append(os.path.join(rgb_dir, 'sharp', tar_file))
                    break
        self.tar_filenames_set = tar_filenames_set

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

        tar_imgs = []
        tar_paths = []
        for i in range(len(self.pic_index)):
            tar_path = self.tar_filenames_set[i][index_]
            tar_paths.append(tar_path)
            tar_img = Image.open(tar_path)
            tar_imgs.append(tar_img)
        # print(f">> tar_paths: {tar_paths}")
        # print(f">> pic_index: {self.pic_index}")

        len_tar_imgs = len(tar_imgs)

        # Reflect Pad in case image is smaller than patch_size
        if padw != 0 or padh != 0:
            inp_img = TF.pad(inp_img, (0, 0, padw, padh), padding_mode='reflect')
            tar_img = []
            for i in range(len_tar_imgs):
                tar_imgs[i] = TF.pad(tar_imgs[i], (0, 0, padw, padh), padding_mode='reflect')
            # tar_img1 = TF.pad(tar_img1, (0, 0, padw, padh), padding_mode='reflect')
            # tar_img2 = TF.pad(tar_img2, (0, 0, padw, padh), padding_mode='reflect')
            # tar_img6 = TF.pad(tar_img6, (0, 0, padw, padh), padding_mode='reflect')
            # tar_img7 = TF.pad(tar_img7, (0, 0, padw, padh), padding_mode='reflect')

        aug = random.randint(0, 2)
        if aug == 1:
            inp_img = TF.adjust_gamma(inp_img, 1)
            for i in range(len_tar_imgs):
                tar_imgs[i] = TF.adjust_gamma(tar_imgs[i], 1)
            # tar_img1 = TF.adjust_gamma(tar_img1, 1)
            # tar_img2 = TF.adjust_gamma(tar_img2, 1)
            # tar_img6 = TF.adjust_gamma(tar_img6, 1)
            # tar_img7 = TF.adjust_gamma(tar_img7, 1)

        aug = random.randint(0, 2)
        if aug == 1:
            sat_factor = 1 + (0.2 - 0.4 * np.random.rand())
            inp_img = TF.adjust_saturation(inp_img, sat_factor)
            for i in range(len_tar_imgs):
                tar_imgs[i] = TF.adjust_saturation(tar_imgs[i], sat_factor)
            # tar_img1 = TF.adjust_saturation(tar_img1, sat_factor)
            # tar_img2 = TF.adjust_saturation(tar_img2, sat_factor)
            # tar_img6 = TF.adjust_saturation(tar_img6, sat_factor)
            # tar_img7 = TF.adjust_saturation(tar_img7, sat_factor)

        inp_img = TF.to_tensor(inp_img)
        for i in range(len_tar_imgs):
            tar_imgs[i] = TF.to_tensor(tar_imgs[i])
        # tar_img1 = TF.to_tensor(tar_img1)
        # tar_img2 = TF.to_tensor(tar_img2)
        # tar_img6 = TF.to_tensor(tar_img6)
        # tar_img7 = TF.to_tensor(tar_img7)

        hh, ww = tar_imgs[0].shape[1], tar_imgs[0].shape[2]

        rr = random.randint(0, hh - ps)
        cc = random.randint(0, ww - ps)
        aug = random.randint(0, 8)

        # Crop patch
        inp_img = inp_img[:, rr:rr + ps, cc:cc + ps]
        for i in range(len_tar_imgs):
            tar_imgs[i] = tar_imgs[i][:, rr:rr + ps, cc:cc + ps]
        # tar_img1 = tar_img1[:, rr:rr + ps, cc:cc + ps]
        # tar_img2 = tar_img2[:, rr:rr + ps, cc:cc + ps]
        # tar_img6 = tar_img6[:, rr:rr + ps, cc:cc + ps]
        # tar_img7 = tar_img7[:, rr:rr + ps, cc:cc + ps]

        # Data Augmentations
        if aug == 1:
            inp_img = inp_img.flip(1)
            for i in range(len_tar_imgs):
                tar_imgs[i] = tar_imgs[i].flip(1)
            # tar_img1 = tar_img1.flip(1)
            # tar_img2 = tar_img2.flip(1)
            # tar_img6 = tar_img6.flip(1)
            # tar_img7 = tar_img7.flip(1)
        elif aug == 2:
            inp_img = inp_img.flip(2)
            for i in range(len_tar_imgs):
                tar_imgs[i] = tar_imgs[i].flip(2)
            # tar_img1 = tar_img1.flip(2)
            # tar_img2 = tar_img2.flip(2)
            # tar_img6 = tar_img6.flip(2)
            # tar_img7 = tar_img7.flip(2)
        elif aug == 3:
            inp_img = torch.rot90(inp_img, dims=(1, 2))
            for i in range(len_tar_imgs):
                tar_imgs[i] = torch.rot90(tar_imgs[i], dims=(1, 2))
            # tar_img1 = torch.rot90(tar_img1, dims=(1, 2))
            # tar_img2 = torch.rot90(tar_img2, dims=(1, 2))
            # tar_img6 = torch.rot90(tar_img6, dims=(1, 2))
            # tar_img7 = torch.rot90(tar_img7, dims=(1, 2))
        elif aug == 4:
            inp_img = torch.rot90(inp_img, dims=(1, 2), k=2)
            for i in range(len_tar_imgs):
                tar_imgs[i] = torch.rot90(tar_imgs[i], dims=(1, 2), k=2)
            # tar_img1 = torch.rot90(tar_img1, dims=(1, 2), k=2)
            # tar_img2 = torch.rot90(tar_img2, dims=(1, 2), k=2)
            # tar_img6 = torch.rot90(tar_img6, dims=(1, 2), k=2)
            # tar_img7 = torch.rot90(tar_img7, dims=(1, 2), k=2)
        elif aug == 5:
            inp_img = torch.rot90(inp_img, dims=(1, 2), k=3)
            for i in range(len_tar_imgs):
                tar_imgs[i] = torch.rot90(tar_imgs[i], dims=(1, 2), k=3)
            # tar_img1 = torch.rot90(tar_img1, dims=(1, 2), k=3)
            # tar_img2 = torch.rot90(tar_img2, dims=(1, 2), k=3)
            # tar_img6 = torch.rot90(tar_img6, dims=(1, 2), k=3)
            # tar_img7 = torch.rot90(tar_img7, dims=(1, 2), k=3)
        elif aug == 6:
            inp_img = torch.rot90(inp_img.flip(1), dims=(1, 2))
            for i in range(len_tar_imgs):
                tar_imgs[i] = torch.rot90(tar_imgs[i].flip(1), dims=(1, 2))
            # tar_img1 = torch.rot90(tar_img1.flip(1), dims=(1, 2))
            # tar_img2 = torch.rot90(tar_img2.flip(1), dims=(1, 2))
            # tar_img6 = torch.rot90(tar_img6.flip(1), dims=(1, 2))
            # tar_img7 = torch.rot90(tar_img7.flip(1), dims=(1, 2))
        elif aug == 7:
            inp_img = torch.rot90(inp_img.flip(2), dims=(1, 2))
            for i in range(len_tar_imgs):
                tar_imgs[i] = torch.rot90(tar_imgs[i].flip(2), dims=(1, 2))
            # tar_img1 = torch.rot90(tar_img1.flip(2), dims=(1, 2))
            # tar_img2 = torch.rot90(tar_img2.flip(2), dims=(1, 2))
            # tar_img6 = torch.rot90(tar_img6.flip(2), dims=(1, 2))
            # tar_img7 = torch.rot90(tar_img7.flip(2), dims=(1, 2))

        # tar_img_set = [tar_img1, tar_img2, tar_img6, tar_img7]

        # filename1 = os.path.splitext(os.path.split(tar_path1)[-1])[0]
        # filename2 = os.path.splitext(os.path.split(tar_path2)[-1])[0]
        # filename6 = os.path.splitext(os.path.split(tar_path6)[-1])[0]
        # filename7 = os.path.splitext(os.path.split(tar_path7)[-1])[0]
        filename_set = []
        for tar_path in tar_paths:
            filename_set.append(os.path.splitext(os.path.split(tar_path)[-1])[0])

        return tar_imgs, inp_img, filename_set


class DataLoaderVal(Dataset):
    def __init__(self, rgb_dir, group_size, pic_index, img_options=None):
        super(DataLoaderVal, self).__init__()
        self.group_size = group_size
        self.pic_index = pic_index

        inp_files = sorted(os.listdir(os.path.join(rgb_dir, 'blurry')))
        # tar_files = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))

        tar_files_all = sorted(os.listdir(os.path.join(rgb_dir, 'sharp')))

        tar_filenames_set = []

        for _ in pic_index:
            tar_filenames_set.append([])
        for i in range(len(tar_files_all)):
            for j in range(len(pic_index)):
                index = pic_index[j]
                if i % group_size == index:
                    tar_file = tar_files_all[i]
                    if is_image_file(tar_file):
                        tar_filenames_set[j].append(os.path.join(rgb_dir, 'sharp', tar_file))
                    break
        self.tar_filenames_set = tar_filenames_set

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
        len_pic_index = len(self.pic_index)
        tar_paths = [self.tar_filenames_set[i][index_] for i in range(len_pic_index)]

        # tar_path1 = self.tar_filenames_set[0][index_]
        # tar_path2 = self.tar_filenames_set[1][index_]
        # tar_path6 = self.tar_filenames_set[2][index_]
        # tar_path7 = self.tar_filenames_set[3][index_]

        inp_img = Image.open(inp_path)
        tar_imgs = [Image.open(tar_path) for tar_path in tar_paths]
        # tar_img1 = Image.open(tar_path1)
        # tar_img2 = Image.open(tar_path2)
        # tar_img6 = Image.open(tar_path6)
        # tar_img7 = Image.open(tar_path7)

        # print("!! ps:", self.ps)
        # Validate on center crop
        if self.ps is not None:
            inp_img = TF.center_crop(inp_img, [ps, ps])
            for i in range(len_pic_index):
                tar_imgs[i] = TF.center_crop(tar_imgs[i], [ps, ps])
            # tar_img1 = TF.center_crop(tar_img1, [ps, ps])
            # tar_img2 = TF.center_crop(tar_img2, [ps, ps])
            # tar_img6 = TF.center_crop(tar_img6, [ps, ps])
            # tar_img7 = TF.center_crop(tar_img7, [ps, ps])

        inp_img = TF.to_tensor(inp_img)
        for i in range(len_pic_index):
            tar_imgs[i] = TF.to_tensor(tar_imgs[i])
        # tar_img1 = TF.to_tensor(tar_img1)
        # tar_img2 = TF.to_tensor(tar_img2)
        # tar_img6 = TF.to_tensor(tar_img6)
        # tar_img7 = TF.to_tensor(tar_img7)
        # tar_img_set = [tar_img1, tar_img2, tar_img6, tar_img7]
        # filename1 = os.path.splitext(os.path.split(tar_path1)[-1])[0]
        # filename2 = os.path.splitext(os.path.split(tar_path2)[-1])[0]
        # filename6 = os.path.splitext(os.path.split(tar_path6)[-1])[0]
        # filename7 = os.path.splitext(os.path.split(tar_path7)[-1])[0]
        filename_set = [os.path.splitext(os.path.split(tar_path)[-1])[0] for tar_path in tar_paths]

        return tar_imgs, inp_img, filename_set


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