# -*- coding: utf-8 -*-
# CLAB3

import numpy as np
from PIL import Image
import matplotlib.pyplot as plt
from vgg_KR_from_P import *
import cv2

img_dir = '..\\img\\'
img_file = "stereo2012b.jpg"
I = Image.open(img_dir + img_file)
uv_path = ".\\uv.npy"

N = 12
plt.title(img_file)
plt.imshow(I)
# uv = plt.ginput(N)  # Graphical user interface to get 12 points
# uv = np.array(uv)
uv = np.load(uv_path)
plt.show()

print(type(uv), "\n", uv)

plt.title("Selected points")
plt.imshow(I)
counter = 1
for x, y in uv:
    plt.plot(x, y, ".", color="red")
    plt.text(x, y, str(counter), color="red")
    counter += 1
plt.show()

# print("After", N, "clicks:")
# print(uv)

# np.save(uv_path, uv)


# for test:
# test_uv = np.load(uv_path)
# print("test_uv:\n", test_uv)
#
# print((uv == test_uv).all())

L = 7  # distance between points in cm

XYZ = [
    [1, 1, 0],
    [2, 1, 0],
    [1, 2, 0],
    [2, 2, 0],
    [0, 1, 1],
    [0, 2, 1],
    [0, 2, 2],
    [0, 1, 2],
    [1, 0, 1],
    [1, 0, 2],
    [2, 0, 1],
    [2, 0, 2]
]

XYZ = np.array(XYZ) * L


# print("XYZ:", XYZ.shape, "\n", XYZ)


# XYZ_path = ".\\XYZ.npy"
# np.save(XYZ_path, XYZ)


#####################################################################
def calibrate(im, XYZ, uv):
    n = uv.shape[0]
    A = []
    for i in range(n):
        x, y = uv[i]
        X, Y, Z = XYZ[i]
        A.append([X, Y, Z, 1, 0, 0, 0, 0, -x * X, -x * Y, -x * Z, -x])
        A.append([0, 0, 0, 0, X, Y, Z, 1, -y * X, -y * Y, -y * Z, -y])
    A = np.array(A)
    C = np.linalg.svd(A)[-1][-1]
    C = C / C[-1]
    C = C.reshape((3, -1))
    print("C:", C.shape, "\n", C)

    h, w, _ = np.array(im).shape
    addition = np.array([
        [0, 0, 0],
        [1, 0, 0],
        [0, 1, 0],
        [0, 0, 1]
    ])

    XYZ = np.vstack((XYZ, addition))
    print(n, n + 4)
    print("XYZ:", XYZ)

    XYZ_T = np.vstack((XYZ.T, np.ones((1, n + 4))))
    uv_new = np.dot(C, XYZ_T)
    uv_new_t = np.transpose(uv_new)

    x_new = np.empty((n + 4, 1))
    y_new = np.empty((n + 4, 1))
    for i in range(0, n + 4):
        x_new[i] = uv_new_t[i][0] / uv_new_t[i][2]
        y_new[i] = uv_new_t[i][1] / uv_new_t[i][2]

    uv_proj = np.hstack((x_new, y_new))
    x_proj = uv_proj[: n, 0]
    y_proj = uv_proj[: n, 1]

    plt.title("Projected points")
    plt.imshow(im)
    plt.xlim(xmin=0, xmax=w)
    plt.ylim(ymin=h, ymax=0)
    # draw points:
    for xx, yy in zip(x_proj, y_proj):
        plt.plot(xx, yy, ".", color='blue')

    # draw Lines from the origin to the vanishing points in the X, Y and Z directions:
    original_point_x = uv_proj[n, 0]
    original_point_y = uv_proj[n, 1]

    key_points_x = uv_proj[n + 1:, 0]
    key_points_y = uv_proj[n + 1:, 1]
    dir_x = key_points_x - original_point_x
    dir_y = key_points_y - original_point_y
    colors = ['r', 'g', 'b']
    start = [(original_point_x, original_point_y) for _ in range(3)]
    flag = True
    while flag:
        flag = False
        for i in range(3):
            start_x = start[i][0]
            start_y = start[i][1]
            kx = start_x + dir_x[i]
            ky = start_y + dir_y[i]
            c = colors[i]
            plt.plot([start_x, kx], [start_y, ky], color=c)

            if 0 < kx < w and 0 < ky < h:
                print("Draw")
                flag = True
                start[i] = (kx, ky)
    plt.show()
    return C


C = calibrate(I, XYZ, uv)

K, R, T = vgg_KR_from_P(C, noscale=True)

print("K:", K.shape, "\n", K)
print("R:", R.shape, "\n", R)
print("T:", T.shape, "\n", T)


# ===============================================================
uv2_path = ".\\uv2.npy"

H, W = I.size
print(H, W)
I_array = np.array(I)
print(I_array.shape, I_array)
I2 = cv2.resize(I_array, (int(H/2), int(W/2)))

plt.title("Resized image")
plt.imshow(I2)
# uv2 = plt.ginput(N)  # Graphical user interface to get 12 points
# uv2 = np.array(uv2)

plt.show()
# np.save(uv2_path, uv2)
uv2 = np.load(uv2_path)
# uv2 = uv / 2

print("uv2:", uv2.shape, "\n", uv2)

C2 = calibrate(I2, XYZ, uv2)

print("C2 for the resized image:\n", C2)

K2, R2, T2 = vgg_KR_from_P(C2, noscale=True)
print("K2: ", K2.shape, "\n", K2)
print("R2: ", R2.shape, "\n", R2)
print("T2: ", T2.shape, "\n", T2)


'''
%% TASK 1: CALIBRATE
%
% Function to perform camera calibration
%
% Usage:   calibrate(image, XYZ, uv)
%          return C
%   Where:   image - is the image of the calibration target.
%            XYZ - is a N x 3 array of  XYZ coordinates
%                  of the calibration target points. 
%            uv  - is a N x 2 array of the image coordinates
%                  of the calibration target points.
%            K   - is the 3 x 4 camera calibration matrix.
%  The variable N should be an integer greater than or equal to 6.
%
%  This function plots the uv coordinates onto the image of the calibration
%  target. 
%
%  It also projects the XYZ coordinates back into image coordinates using
%  the calibration matrix and plots these points too as 
%  a visual check on the accuracy of the calibration process.
%
%  Lines from the origin to the vanishing points in the X, Y and Z
%  directions are overlaid on the image. 
%
%  The mean squared error between the positions of the uv coordinates 
%  and the projected XYZ coordinates is also reported.
%
%  The function should also report the error in satisfying the 
%  camera calibration matrix constraints.
% 
% your name, date 
'''


############################################################################
def homography(u2Trans, v2Trans, uBase, vBase):
    n = u2Trans.shape[0]
    A = []
    for i in range(n):
        a1 = [0, 0, 0, -uBase[i], -vBase[i], -1, v2Trans[i] * uBase[i], v2Trans[i] * vBase[i], v2Trans[i]]
        a2 = [uBase[i], vBase[i], 1, 0, 0, 0, -u2Trans[i] * uBase[i], -u2Trans[i] * vBase[i], -u2Trans[i]]
        A.append(a1)
        A.append(a2)
    A = np.array(A)
    H = np.linalg.svd(A)[-1][-1]
    H = H / H[-1]
    H = H.reshape((3, -1))
    return H


N_corr = 6

L_path = "Left.jpg"
R_path = "Right.jpg"
uv_left_path = "uv_left.npy"
uv_right_path = "uv_right.npy"

L_img = Image.open(img_dir + L_path)
R_img = Image.open(img_dir + R_path)

plt.title(L_path)
plt.imshow(L_img)
# uv_left = plt.ginput(N_corr)
uv_left = np.load(uv_left_path)
for i in range(N_corr):
    plt.plot(uv_left[i][0], uv_left[i][1], ".", color="r")
    plt.text(uv_left[i][0], uv_left[i][1], i, color="r")
plt.show()

plt.title(R_path)
plt.imshow(R_img)
# uv_right = plt.ginput(N_corr)
uv_right = np.load(uv_right_path)
for i in range(N_corr):
    plt.plot(uv_right[i][0], uv_right[i][1], ".", color="b")
    plt.text(uv_right[i][0], uv_right[i][1], i, color="b")
plt.show()

# np.save(uv_left_path, uv_left)
# np.save(uv_right_path, uv_right)

print("uv_left: ", uv_left)
print("uv_right: ", uv_right)

uBase = uv_left[:, 0]
vBase = uv_left[:, 1]
u2Trans = uv_right[:, 0]
v2Trans = uv_right[:, 1]
H = homography(u2Trans, v2Trans, uBase, vBase)

print("H: \n", H)

w, h, _ = np.array(L_img).shape
L_img_warp = cv2.warpPerspective(np.array(L_img), H, (h, w))
plt.title("Warpped left image ")
plt.imshow(L_img_warp)
plt.show()


'''
%% TASK 2: 
% Computes the homography H applying the Direct Linear Transformation 
% The transformation is such that 
% p = np.matmul(H, p.T), i.e.,
% (uBase, vBase, 1).T = np.matmul(H, (u2Trans , v2Trans, 1).T)
% Note: we assume (a, b, c) => np.concatenate((a, b, c), axis), be careful when 
% deal the value of axis 
%
% INPUTS: 
% u2Trans, v2Trans - vectors with coordinates u and v of the transformed image point (p') 
% uBase, vBase - vectors with coordinates u and v of the original base image point p  
% 
% OUTPUT 
% H - a 3x3 Homography matrix  
% 
% your name, date 
'''


############################################################################
def rq(A):
    # RQ factorisation

    [q, r] = np.linalg.qr(A.T)  # numpy has QR decomposition, here we can do it
    # with Q: orthonormal and R: upper triangle. Apply QR
    # for the A-transpose, then A = (qr).T = r.T@q.T = RQ
    R = r.T
    Q = q.T
    return R, Q
