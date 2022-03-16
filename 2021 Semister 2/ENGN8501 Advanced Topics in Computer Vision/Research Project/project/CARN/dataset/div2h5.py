"""
This file refers to https://github.com/nmhkahn/CARN-pytorch and made changes.
"""

import os
import numpy as np
import imageio
import glob
import h5py

dataset_dir = "DIV2K\\"
dataset_type = "train"

h5 = h5py.File("DIV2K_{}.h5".format(dataset_type), "w")
dt = h5py.special_dtype(vlen=np.dtype('uint8'))

size_limit = 0

for subdir in ["HR", "X2", "X3", "X4"]:

    # load images:
    if subdir == "HR":
        im_paths = glob.glob(os.path.join(dataset_dir, "DIV2K_{}_HR".format(dataset_type), "*.png"))
    else:
        im_paths = glob.glob(os.path.join(dataset_dir, "DIV2K_{}_LR_bicubic".format(dataset_type), subdir, "*.png"))

    # print(im_paths)
    im_paths.sort()
    grp = h5.create_group(subdir)

    # build *.h5
    for i, path in enumerate(im_paths):
        if size_limit <= 0 or i < size_limit:
            im = imageio.imread(path)
            print(path)
            grp.create_dataset(str(i), data=im)
