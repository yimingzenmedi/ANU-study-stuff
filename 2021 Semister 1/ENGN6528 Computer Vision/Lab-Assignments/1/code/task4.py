import imageio
import matplotlib.pyplot as plt
import numpy as np
import cv2


# 1. Converts the RGB image to YUV colour space

def cvRGB2YUV(rgb_img):
    _r = cv2.split(rgb_img)[0]
    _g = cv2.split(rgb_img)[1]
    _b = cv2.split(rgb_img)[2]

    _y = 0.299 * _r + 0.587 * _g + 0.114 * _b
    _u = (-0.168736 * _r - 0.331264 * _g + 0.5 * _b) + 128
    _v = (0.5 * _r - 0.418688 * _g - 0.081312 * _b) + 128
    # _u = 0.492111 * (_b - _y) + 128
    # _v = 0.877283 * (_r - _y) + 128
    return cv2.merge([_y, _u, _v])


img = imageio.imread("../images/Figure2-a.png")

yuv_img = cvRGB2YUV(img)

(y, u, v) = cv2.split(yuv_img)

plt.subplot(2, 2, 1)
plt.title("Y channel")
plt.imshow(np.uint8(y), cmap='gray')

plt.subplot(2, 2, 2)
plt.title("U channel")
plt.imshow(np.uint8(u), cmap='gray')

plt.subplot(2, 2, 3)
plt.title("V channel")
plt.imshow(np.uint8(v), cmap='gray')

plt.show()


# 2. Compute the average Y values of 5 regions

img_b = imageio.imread("../images/Figure2-b.png")

(r, g, b, _) = cv2.split(img_b)

plt.title("Figure2-b")
plt.imshow(np.uint8(img_b))

# Attempt 1:
#
# border_thr = 50     # color change threshold
# border_width = 3    # border transition width between regions
#
# col_means = np.mean(img_b, axis=0)
# borders = []
# compared_color = col_means[0]
# print(compared_color)
#
# for i in range(len(col_means) - 1):
#     c_r = compared_color[0]
#     c_g = compared_color[1]
#     c_b = compared_color[2]
#
#     pixel = col_means[i][:3]
#     # next_pixel = img_b[50, i+1][:3]
#     r = int(pixel[0])
#     g = int(pixel[1])
#     b = int(pixel[2])
#     if abs(r - c_r) > border_thr or abs(g - c_g) > border_thr or abs(b - c_b) > border_thr:
#         # print("\nr: ", r, ", c_r: ", c_r, ", abs(r - c_r): ", abs(r - c_r))
#         # print("g: ", g, ", c_g: ", c_g, ", abs(g - c_g): ", abs(g - c_g))
#         # print("b: ", b, ", c_b: ", c_b, ", abs(b - c_b): ", abs(b - c_b))
#
#         if r != 0 or g != 0 or b != 0:
#             last_border = borders[-1] if len(borders) else 0
#             if i - last_border > border_width:
#                 borders.append(i)
#                 # print(borders)
#         compared_color = [r, g, b]
#
# borders.append(len(col_means))
#
# start = 0
# # print(borders)
#
# if len(borders) > 0:
#     for border in borders:
#         region = img_b[:, start:border]
#         # print(region.shape)
#
#         yuv_mime = cvRGB2YUV(region)
#         yuv_inbuilt = cv2.cvtColor(region, cv2.COLOR_RGB2YUV)
#         y_mine = yuv_mime[:, :, 0]
#         y_inbuilt = yuv_inbuilt[:, :, 0]
#         y_mean_mine = np.mean(y_mine)
#         y_mean_inbuilt = np.mean(y_inbuilt)
#
#         plt.text(start + 50, 1050, "Y1: %.2f" % y_mean_mine)
#         plt.text(start + 50, 1130, "Y2: %.2f" % y_mean_inbuilt)
#
#         start = border + 1
#
# plt.text(5, 1270, "Y1: average Y value calculated with my function")
# plt.text(5, 1340, "Y2: average Y value calculated with inbuilt function")
#
# plt.subplots_adjust(top=0.6)
#
# plt.show()

# Attempt 2:

h, w, _ = img_b.shape

thr = 50
pxl_thr = 3 * w

res = [{"std_color": img_b[0, 0], "region": np.zeros(img_b.shape), "pixel_counter": 0, "start_col": 0}]

for i in range(h):
    for j in range(w):
        pixel = img_b[i, j]
        r = int(pixel[0])
        g = int(pixel[1])
        b = int(pixel[2])

        is_new = True

        for index in range(len(res)):
            std_color = res[index]["std_color"]
            std_r = std_color[0]
            std_g = std_color[1]
            std_b = std_color[2]
            if abs(r - std_r) < thr and abs(g - std_g) < thr and abs(b - std_b) < thr:
                res[index]["region"][i][j] = pixel
                is_new = False
                res[index]["pixel_counter"] += 1
                # print("color: ", pixel, " is in std color: ", std_color)
                break

        if is_new:
            # print("New std color: ", pixel)
            new_region = np.zeros(img_b.shape)
            new_region[i][j] = pixel
            res.append({
                "std_color": pixel,
                "region": new_region,
                "pixel_counter": 1,
                "start_col": j
            })

for re_index in range(len(res)):
    re = res[re_index]
    if re["pixel_counter"] >= pxl_thr:
        region = re["region"].astype("uint8")
        start_col = re["start_col"]
        pixels = re["pixel_counter"]
        yuv_mime = cvRGB2YUV(region)
        yuv_inbuilt = cv2.cvtColor(region, cv2.COLOR_RGB2YUV)
        y_mine = yuv_mime[:, :, 0]
        y_inbuilt = yuv_inbuilt[:, :, 0]
        y_mean_mine = np.sum(y_mine) / pixels
        y_mean_inbuilt = np.sum(y_inbuilt) / pixels

        plt.text(start_col + 50, h + 200, "Y1: %.2f" % y_mean_mine)
        plt.text(start_col + 50, h + 280, "Y2: %.2f" % y_mean_inbuilt)

plt.text(5, h + 400, "Y1: average Y value calculated with my function")
plt.text(5, h + 470, "Y2: average Y value calculated with inbuilt function")

plt.subplots_adjust(top=0.6)
plt.show()

print(img_b.shape)
###########################################



