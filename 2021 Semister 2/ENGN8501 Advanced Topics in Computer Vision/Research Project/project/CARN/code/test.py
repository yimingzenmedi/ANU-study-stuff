"""
This file refers to https://github.com/nmhkahn/CARN-pytorch and made changes.
"""

import os
import torch
from PIL import Image

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


def save_image(tensor, filename):
    tensor = tensor.cpu()
    ndarr = tensor.mul(255).clamp(0, 255).byte().permute(1, 2, 0).numpy()
    im = Image.fromarray(ndarr)
    print("filename:", filename)
    im.save(filename)


def test(args, model, dataset):
    for step, (hr, lr, name) in enumerate(dataset):
        lr = lr.unsqueeze(0).to(device)
        sr = model(lr, args.scale).detach().squeeze(0)
        lr = lr.squeeze(0)

        sr_dir = os.path.join(args.test_dir,
                              args.test_data_dir.split("/")[-1],
                              "x{}".format(args.scale),
                              "SR")

        hr_dir = os.path.join(args.test_dir,
                              args.test_data_dir.split("/")[-1],
                              "x{}".format(args.scale),
                              "HR")

        os.makedirs(sr_dir, exist_ok=True)
        os.makedirs(hr_dir, exist_ok=True)

        filename = name.split("\\")[-1]

        sr_im_path = os.path.join(sr_dir, "{}".format(filename.replace("HR", "SR")))
        hr_im_path = os.path.join(hr_dir, "{}".format(filename))

        save_image(sr, sr_im_path)
        save_image(hr, hr_im_path)
