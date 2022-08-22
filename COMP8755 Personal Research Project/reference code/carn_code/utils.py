import skimage.metrics as metrics

import numpy as np
import cv2
import os


def im2double(im):
    min_val, max_val = 0, 255
    out = (im.astype(np.float64) - min_val) / (max_val - min_val)
    return out


def get_psnr(im1, im2):
    im1 = im2double(im1)
    im2 = im2double(im2)
    h, w = im1.shape[:2]
    im2 = cv2.resize(im2, (w, h))
    psnr = metrics.peak_signal_noise_ratio(im1, im2, data_range=1)
    return psnr


def get_ssim(im1, im2):
    multichannel = False
    if im1.shape[-1] > 0 or im2.shape[-1] > 0:
        multichannel = True
    im1 = im2double(im1)
    im2 = im2double(im2)
    h, w = im1.shape[:2]
    im2 = cv2.resize(im2, (w, h))
    ssim = metrics.structural_similarity(im1, im2, data_range=1, multichannel=multichannel)
    return ssim


if __name__ == "__main__":
    # pic_hr = cv2.imread("../sample/Custom/x4/HR/00014_4_HR.png")
    # # # h, w = pic_hr.shape[:2]
    # # pic_sr2 = cv2.imread("../sample/B100/x2/SR/img_001_SRF_2_SR.png")
    # # pic_sr3 = cv2.imread("../sample/B100/x3/SR/img_001_SRF_3_SR.png")
    # # pic_sr3 = cv2.resize(pic_sr3, (480, 320))
    # pic_sr4 = cv2.imread("../sample/Custom/x4/SR/00014_4_SR.png")
    # # print(pic_sr4.shape)
    # pic_lr = cv2.imread("../dataset/Custom/x4/00014_4_LR.png")
    # # # pic_lr = cv2.resize(pic_lr, (w, h))
    # #
    # # print("x2 PSNR:", get_psnr(pic_hr, pic_sr2), "| SSIM:", get_ssim(pic_hr, pic_sr2))
    # # print("x3 PSNR:", get_psnr(pic_hr, pic_sr3), "| SSIM:", get_ssim(pic_hr, pic_sr3))
    # print("x4 PSNR:", get_psnr(pic_hr, pic_sr4), "| SSIM:", get_ssim(pic_hr, pic_sr4))
    # print("LR PSNR:", get_psnr(pic_hr, pic_lr), "| SSIM:", get_ssim(pic_hr, pic_lr))

    # LR mean ============================================
    # dataset_path = "../dataset/Urban100/x4/"
    # dir = os.listdir(dataset_path)
    # counter = 0
    # psnrs = []
    # ssims = []
    #
    # HR = LR = None
    # for file in dir:
    #     counter += 1
    #     if "HR" in file:
    #         HR = cv2.imread(dataset_path+file)
    #     elif "LR" in file:
    #         LR = cv2.imread(dataset_path+file)
    #     if counter % 2 == 0:
    #         psnr = get_psnr(HR, LR)
    #         ssim = get_ssim(HR, LR)
    #         psnrs.append(psnr)
    #         ssims.append(ssim)
    # # print(psnrs)
    # print("mean PSNR:", np.array(psnrs).mean())
    #
    # # print(ssims)
    # print("mean SSIM:", np.array(ssims).mean())

    carn_path = "../sample/loss"
    # carn_m_path = "../sample/loss/"
    # for dataset in ["Custom"]:
    #     for scale in [2, 3, 4]:
    for dataset in ["Set5", "Set14", "Custom", "B100", "Urban100"]:
        for loss in ["L1", "mse", "SmoothL1"]:
            HR_dir_path = carn_path + "/" + loss + "/" + dataset + "/x4" + "/HR/"
            HR_dir = os.listdir(HR_dir_path)
            SR_dir_path = carn_path + "/" + loss + "/" + dataset + "/x4" + "/SR/"
            SR_dir = os.listdir(SR_dir_path)
            # SR_m_dir_path = carn_m_path + "/" + loss + "/" + dataset + "/x4" + "/SR/"
            # SR_m_dir = os.listdir(SR_m_dir_path)
            LR_dir_path = "../dataset/" + dataset + "/x4" + "/"
            LR_dir = os.listdir(LR_dir_path)

            print("\nHR_dir:", HR_dir_path)
            print("LR_dir:", LR_dir_path)
            print("SR_dir:", SR_dir_path)
            # print("SR_m_dir:", SR_m_dir_path)
            psnrs = []
            psnrs_lr = []
            # psnrs_m = []
            ssims = []
            ssims_lr = []
            # ssims_m = []

            counter = 0
            n = len(HR_dir)
            for i in range(n):
                hr = cv2.imread(HR_dir_path + HR_dir[i])
                lr = cv2.imread(LR_dir_path + LR_dir[i * 2 + 1])
                sr = cv2.imread(SR_dir_path + SR_dir[i])
                # sr_m = cv2.imread(SR_m_dir_path + SR_m_dir[i])
                psnrs.append(get_psnr(hr, sr))
                ssims.append(get_ssim(hr, sr))
                psnrs_lr.append(get_psnr(hr, lr))
                ssims_lr.append(get_ssim(hr, lr))
                # psnrs_m.append(get_psnr(hr, sr_m))
                # ssims_m.append(get_ssim(hr, sr_m))
                counter += 1
                # print(HR_dir[i], "| LR: %f/%f, CARN: %f/%f, CARN-M: %f/%f" % (
                #     round(get_psnr(hr, lr), 2), round(get_ssim(hr, lr), 4),
                #     round(psnrs[i], 2), round(ssims[i], 4),
                #     round(psnrs_m[i], 2), round(ssims_m[i], 4))
                #       )
                # print(HR_dir[i], "| LR: %f/%f, CARN: %f/%f" % (
                #     round(get_psnr(hr, lr), 2), round(get_ssim(hr, lr), 4),
                #     round(psnrs[i], 2), round(ssims[i], 4))
                #       )

            print("\n", dataset, "|", loss)
            print("LR:", round(np.array(psnrs_lr).mean(), 2), "/", round(np.array(ssims_lr).mean(), 4))
            print("CARN:", round(np.array(psnrs).mean(), 2), "/", round(np.array(ssims).mean(), 4))
