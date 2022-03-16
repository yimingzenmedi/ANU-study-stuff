import imageio
import matplotlib.pyplot as plt
import numpy as np
import cv2

img = cv2.resize(imageio.imread("../images/image3.jpg"), (512, 512))


# 1. Implement image translation function

def my_translation(_img, _tx, _ty):
    if _ty < -100 or _ty > 100 or _tx < -100 or _tx > 100:
        raise Exception("x and y should in range [-100, 100]")
    print(_tx, _ty)
    (_h, _w, _c) = _img.shape
    out = np.zeros((_h, _w, _c), dtype=float)
    _img = _img.astype(float)
    _empty_pixel = np.array([0, 0, 0])
    for y in range(_h):
        for x in range(_w):
            ori_y = y + _ty
            ori_x = x - _tx
            ori_y1 = int(ori_y) if ori_y >= 0 else int(ori_y) - 1
            ori_x1 = int(abs(ori_x)) if ori_x >= 0 else int(ori_x) - 1
            ori_y2 = 1 + ori_y1
            ori_x2 = 1 + ori_x1
            diff_y1 = ori_y - ori_y1
            diff_x1 = ori_x - ori_x1
            diff_y2 = ori_y2 - ori_y
            diff_x2 = ori_x2 - ori_x

            if _h > ori_y1 >= 0:
                if _w > ori_x1 >= 0:
                    ori_pixel1 = _img[ori_y1, ori_x1]
                else:
                    ori_pixel1 = _empty_pixel
                if _w > ori_x2 >= 0:
                    ori_pixel2 = _img[ori_y1, ori_x2]
                else:
                    ori_pixel2 = _empty_pixel
            else:
                ori_pixel1 = _empty_pixel
                ori_pixel2 = _empty_pixel

            if _h > ori_y2 >= 0:
                if _w > ori_x1 >= 0:
                    ori_pixel3 = _img[ori_y1, ori_x1]
                else:
                    ori_pixel3 = _empty_pixel
                if _w > ori_x2 >= 0:
                    ori_pixel4 = _img[ori_y1, ori_x2]
                else:
                    ori_pixel4 = _empty_pixel
            else:
                ori_pixel3 = _empty_pixel
                ori_pixel4 = _empty_pixel

            if _ty == 1.7:
                print("(%d,%d) ->" % (x, y), "x1: %d, x2: %d, y1: %d, y2: %d" % (ori_x1, ori_x2, ori_y1, ori_y2))
            # print("\ndiff_y1: ", diff_y1, ", diff_x1: ", diff_x1, ", diff_y2: ", diff_y2, ", diff_x2: ", diff_x2)
            out[y][x] = diff_y1 * diff_x1 * ori_pixel1 + diff_y1 * diff_x2 * ori_pixel2 + diff_y2 * diff_x1 * ori_pixel3 + diff_y2 * diff_x2 * ori_pixel4
            # print("done")
    return out


(tx1, ty1) = (2.0, 4.0)
(tx2, ty2) = (-4.0, -6.0)
(tx3, ty3) = (2.5, 4.5)
(tx4, ty4) = (-0.9, 1.7)
(tx5, ty5) = (92.0, -91.0)
# (tx5, ty5) = (50.3, -90.7)

trans_img1 = my_translation(img, tx1, ty1)
trans_img2 = my_translation(img, tx2, ty2)
trans_img3 = my_translation(img, tx3, ty3)
trans_img4 = my_translation(img, tx4, ty4)
trans_img5 = my_translation(img, tx5, ty5)

# plt.subplot(3, 2, 1)
plt.subplot(1, 1, 1)
plt.title("Original image")
plt.imshow(np.uint8(img))
plt.show()

# plt.subplot(3, 2, 2)
plt.subplot(1, 1, 1)
plt.title("Translated by (%.1f, %.1f)" % (tx1, ty1))
plt.imshow(np.uint8(trans_img1))
plt.show()

# plt.subplot(3, 2, 3)
plt.subplot(1, 1, 1)
plt.title("Translated by (%.1f, %.1f)" % (tx2, ty2))
plt.imshow(np.uint8(trans_img2))
plt.show()

# plt.subplot(3, 2, 4)
plt.subplot(1, 1, 1)
plt.title("Translated by (%.1f, %.1f)" % (tx3, ty3))
plt.imshow(np.uint8(trans_img3))
plt.show()

# plt.subplot(3, 2, 5)
plt.subplot(1, 1, 1)
plt.title("Translated by (%.1f, %.1f)" % (tx4, ty4))
plt.imshow(np.uint8(trans_img4))
plt.show()

# plt.subplot(3, 2, 6)
plt.subplot(1, 1, 1)
plt.title("Translated by (%.1f, %.1f)" % (tx5, ty5))
plt.imshow(np.uint8(trans_img5))

plt.show()
