"""
CLAB Task-1: Harris Corner Detector
Han Zhang (u7235649):
"""

import numpy as np
import cv2
import matplotlib.pyplot as plt


def conv2(img, conv_filter):
    # flip the filter
    f_siz_1, f_size_2 = conv_filter.shape
    conv_filter = conv_filter[range(f_siz_1 - 1, -1, -1), :][:, range(f_siz_1 - 1, -1, -1)]
    pad = (conv_filter.shape[0] - 1) // 2
    result = np.zeros(img.shape)
    img = np.pad(img, ((pad, pad), (pad, pad)), 'constant', constant_values=(0, 0))
    filter_size = conv_filter.shape[0]
    for r in np.arange(img.shape[0] - filter_size + 1):
        for c in np.arange(img.shape[1] - filter_size + 1):
            curr_region = img[r:r + filter_size, c:c + filter_size]
            curr_result = curr_region * conv_filter
            conv_sum = np.sum(curr_result)  # Summing the result of multiplication.
            result[r, c] = conv_sum  # Saving the summation in the convolution layer feature map.

    return result


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


# Derivative masks
dx = np.array([[-1, 0, 1], [-1, 0, 1], [-1, 0, 1]])
dy = dx.transpose()

path = "..\\..\\img\\Task1\\"
img = plt.imread(path + 'Harris-1.jpg').astype(np.float32)
bw = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)

print(bw.shape)
# computer x and y derivatives of image
Ix = conv2(bw, dx)
Iy = conv2(bw, dy)

# Task 3: generates a Gaussian kernel as window function
g = fspecial((max(1, np.floor(3 * sigma) * 2 + 1), max(1, np.floor(3 * sigma) * 2 + 1)), sigma)
Iy2 = conv2(np.power(Iy, 2), g)
Ix2 = conv2(np.power(Ix, 2), g)
Ixy = conv2(Ix * Iy, g)

######################################################################
# Task: Compute the Harris Cornerness
######################################################################


######################################################################
# Task: Perform non-maximum suppression and
#       thresholding, return the N corner points
#       as an Nx2 matrix of x and y coordinates
######################################################################

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

    return locs


# plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(img / 255.0)
plt.show()

locs = corner_detect(bw, Ix2, Iy2, Ixy, k, thresh)

# plt.subplot(1, 2, 2)
plt.title("Detected corners, thresh = " + str(thresh))
plt.imshow(img / 255.0)
h, w = locs.shape
for i in range(h):
    for j in range(w):
        if locs[i, j] == 1:
            plt.plot(j, i, "x", color="red", linewidth='1')

plt.show()

