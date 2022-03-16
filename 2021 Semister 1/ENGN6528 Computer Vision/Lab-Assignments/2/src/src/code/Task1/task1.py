"""
CLAB2 Task-1: Harris Corner Detector
Han Zhang (u7235649)
"""
import numpy as np
import cv2
import matplotlib.pyplot as plt

# Task 1


# 2. =======================================
# Complete the missing parts, rewrite them to “harris.py” as a
# python script, and design appropriate function signature.


def conv2(img, conv_filter):
    # flip the filter
    f_siz_1, f_size_2 = conv_filter.shape
    conv_filter = conv_filter[range(f_siz_1 - 1, -1, -1), :][:, range(f_siz_1 - 1, -1, -1)]
    pad = (conv_filter.shape[0] - 1) // 2
    result = np.zeros((img.shape))
    img = np.pad(img, ((pad, pad), (pad, pad)), 'constant', constant_values=(0, 0))
    filter_size = conv_filter.shape[0]
    for r in np.arange(img.shape[0] - filter_size + 1):
        for c in np.arange(img.shape[1] - filter_size + 1):
            curr_region = img[r:r + filter_size, c:c + filter_size]
            curr_result = curr_region * conv_filter
            conv_sum = np.sum(curr_result)  # Summing the result of multiplication.
            result[r, c] = conv_sum  # Saving the summation in the convolution layer feature map.

    return result


# window function with Gaussian:
def fspecial(shape=(3, 3), sigma=0.5):
    m, n = [(ss - 1.) / 2. for ss in shape]
    y, x = np.ogrid[-m:m + 1, -n:n + 1]
    h = np.exp(-(x * x + y * y) / (2. * sigma * sigma))
    h[h < np.finfo(h.dtype).eps * h.max()] = 0
    sumh = h.sum()
    if sumh != 0:
        h /= sumh
    return h


# Parameters, add more if needed
sigma = 2
thresh = 0.01
k = 0.05    # normally 0.04 - 0.16

# image path
path = "..\\..\\img\\Task1\\"

# Derivative masks
dx = np.array([[-1, 0, 1], [-1, 0, 1], [-1, 0, 1]])
dy = dx.transpose()

img = plt.imread(path + 'Harris-1.jpg').astype(np.float32)
bw = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
print(bw.shape)

# bw = np.array(bw * 255, dtype=int)

# Compute x and y derivatives of image
Ix = conv2(bw, dx)
Iy = conv2(bw, dy)

# generates a Gaussian kernel as window function
g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)


# 3. =======================================

def corner_detect(_img, _Ix2, _Iy2, _Ixy, _k, _th):
    """
    imput:
    _img: grayscale image to process
    _Ix2: the square of Ix
    _Iy2: the square of Iy
    _Ixy: IxIy
    _k:   response function parameter
    _th:  threshold

    output:
    locs: an array with the same shape with _img that corners are marked as 1 and the others 0
    """

    h, w = _img.shape

    # calculate M:
    m = []
    for i in range(h):
        for j in range(w):
            # each element of m:
            mi = np.array([
                [_Ix2[i, j], _Ixy[i, j]],
                [_Ixy[i, j], _Iy2[i, j]]
            ])
            m.append(mi)

    # calculate determinant and trace:
    det = list(map(np.linalg.det, m))
    trace = list(map(np.trace, m))

    # calculate R:
    R = np.array([d - _k * t ** 2 for d, t in zip(det, trace)])
    R = R.reshape(h, w)

    # get the maximum of R for comparison:
    R_max = np.max(R)

    # corner locations:
    locs = np.zeros((h, w))
    for i in range(2, h - 2):
        for j in range(2, w - 2):
            # perform non-maximum suppression and thresholding
            if R[i, j] == np.max(R[i - 1 : i + 2, j - 1 : j + 2]) and R[i, j] > R_max * _th:
                locs[i, j] = 1
    #                 print("i: ", i, ", j: ", j, "R: \n", R[i - 1 : i + 2, j - 1 : j + 2])

    return locs


# 4. ========================================

locs = corner_detect(bw, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(2, 1, 2)
plt.title("Harris-1.jpg detected thresh = " + str(thresh))
plt.imshow(img / 255.0)
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')
#             print("i: ", i, ", j: ", j)

plt.show()

# plt.subplot(2, 1, 1)
plt.title("Harris-1.jpg")
plt.imshow(img / 255.0)
plt.show()

img1 = plt.imread(path + 'Harris-2.jpg').astype(np.float32)
# bw1 = cv2.cvtColor(img1, cv2.COLOR_RGB2GRAY)
bw1 = img1

Ix = conv2(bw1, dx)
Iy = conv2(bw1, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

locs = corner_detect(bw1, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(2, 1, 2)
plt.title("Harris-2.jpg detected thresh = " + str(thresh))
plt.imshow(img1 / 255.0, cmap="gray")
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')

plt.show()

plt.title("Harris-2.jpg")
plt.imshow(img1 / 255.0, cmap="gray")
plt.show()


img3 = plt.imread(path + 'Harris-3.jpg').astype(np.float32)
bw3 = cv2.cvtColor(img3, cv2.COLOR_RGB2GRAY)

Ix = conv2(bw3, dx)
Iy = conv2(bw3, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

locs = corner_detect(bw3, Ix2, Iy2, Ixy, k, thresh)

plt.title("Harris-3.jpg detected thresh = " + str(thresh))
plt.imshow(img3 / 255.0)
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')

plt.show()

plt.title("Harris-3.jpg")
plt.imshow(img3 / 255.0)
plt.show()


img4 = plt.imread(path + 'Harris-4.jpg').astype(np.float32)
bw4 = cv2.cvtColor(img4, cv2.COLOR_RGB2GRAY)
# bw1 = img1

Ix = conv2(bw4, dx)
Iy = conv2(bw4, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

locs = corner_detect(bw4, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(2, 1, 2)
plt.title("Harris-4.jpg detected thresh = " + str(thresh))
plt.imshow(img4 / 255.0)
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')

plt.show()

plt.title("Harris-4.jpg")
plt.imshow(img4 / 255.0)
plt.show()

# 6. =====================================

img5 = plt.imread(path + 'Harris-5.jpg').astype(np.float32)
print("Origin:", img5.shape)
bw5 = img5

Ix = conv2(bw5, dx)
Iy = conv2(bw5, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

print(Ix2.shape, Iy2.shape, Ixy.shape)

locs = corner_detect(bw5, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(2, 1, 2)
plt.title("Harris-5.jpg detected thresh = " + str(thresh))
plt.imshow(img5 / 255.0, cmap="gray")
h, w = locs.shape
print("Result: ", locs.shape)
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')
            print("i: ", i, ", j: ", j, "img: \n", img5[i - 1 : i + 2, j - 1 : j + 2])

plt.show()

plt.title("Harris-5.jpg")
plt.imshow(img5 / 255.0, cmap="gray")
plt.show()
np.set_printoptions(threshold=np.inf)
# print(img5)


# 7. ============================================

img6 = plt.imread(path + 'Harris-6.jpg').astype(np.float32)
bw6 = img6

Ix = conv2(bw6, dx)
Iy = conv2(bw6, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

locs = corner_detect(bw6, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(2, 1, 2)
plt.title("Harris-6.jpg detected thresh = " + str(thresh))
plt.imshow(img6 / 255.0, cmap="gray")
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')

plt.show()

img6= plt.imread(path + 'Harris-6.jpg').astype(np.float32)
bw6 = img_Guassian = cv2.GaussianBlur(img6, (5, 5), 0)


Ix = conv2(bw6, dx)
Iy = conv2(bw6, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

locs = corner_detect(bw6, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(2, 1, 2)
plt.title("Gaussian blurred image")
plt.imshow(img6 / 255.0, cmap="gray")
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')

plt.show()

# 5. ========================================

# compare with inbuilt function:

# Compute x and y derivatives of image
Ix = conv2(bw, dx)
Iy = conv2(bw, dy)

g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
# print("g shape: ", g.shape)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

locs = corner_detect(bw, Ix2, Iy2, Ixy, k, thresh)

plt.title("My result")
plt.imshow(img / 255.0)
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')
#             print("i: ", i, ", j: ", j)

plt.show()


blocksize = (max(1, np.floor(3 * sigma) * 2 + 1) - 1) / 2
if blocksize == 0:
    blocksize = 1

print(blocksize)

# dist = cv2.cornerHarris(bw, int(blocksize), 3, k)
dist = cv2.cornerHarris(bw, blockSize=int(blocksize), ksize=3, k=k)

h, w = bw.shape

inbuilt_res = np.zeros((h, w))
for i in range(2, h - 2):
    for j in range(2, w - 2):
        # perform non-maximum suppression and thresholding
        if dist[i, j] == np.max(dist[i - 1 : i + 2, j - 1 : j + 2]) and dist[i, j] > dist.max() * thresh:
            inbuilt_res[i, j] = 1

for i in range(h):
    for j in range(w):
        if inbuilt_res[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth = '1')

plt.title("Inbuilt result")
# plt.imshow(inbuilt_res)
plt.imshow(img / 255.0)
plt.show()

