import imageio
import matplotlib.pyplot as plt
import numpy as np
import cv2


# def gaussian(x, _sigma):
#     res = (1.0/(2*np.pi*(_sigma**2)))*np.exp(-(x**2)/(2*(_sigma**2)))
#     print("gaussian: ", x, _sigma, res)
#     return res
#
#
# def distance(x1, y1, x2, y2):
#     return np.sqrt(np.abs((x1-x2)**2-(y1-y2)**2))
#
#
# def bilateral_filter(image, diameter, sigma_i, sigma_s):
#     new_image = np.zeros(image.shape)
#
#     for row in range(len(image)):
#         for col in range(len(image[0])):
#             wp_total = 0
#             filtered_image = 0
#             for k in range(diameter):
#                 for l in range(diameter):
#                     n_x = row - (diameter/2 - k)
#                     n_y = col - (diameter/2 - l)
#                     if n_x >= len(image):
#                         n_x -= len(image)
#                     if n_y >= len(image[0]):
#                         n_y -= len(image[0])
#                     gi = gaussian(image[int(n_x)][int(n_y)] - image[row][col], sigma_i)
#                     gs = gaussian(distance(n_x, n_y, row, col), sigma_s)
#                     wp = gi * gs
#                     filtered_image = filtered_image + (image[int(n_x)][int(n_y)] * wp)
#                     wp_total = wp_total + wp
#             filtered_image = filtered_image // wp_total
#             new_image[row][col] = int(np.round(filtered_image))
#     return new_image


##################################################################################

def gaus_kernel(winsize, gsigma):
    r = int(winsize/2)
    c = r
    kernel = np.zeros((winsize, winsize))
    sigma1 = 2*gsigma*gsigma
    for i in range(-r, r+1):
        for j in range(-c, c+1):
            kernel[i+r][j+c] = np.exp(-float(float((i*i+j*j))/sigma1))
    print(kernel)
    return kernel

def getKernel(_ksize, _sigma):
    _k = cv2.getGaussianKernel(ksize=_ksize, sigma=_sigma)
    return _k * _k.T


def bilateral_filter(image, winsize, gsigma, ssigma):
    r = int(winsize/2)
    c = r
    image1 = np.pad(image, ((r, c),(r, c)), constant_values=0)
    image = image1
    row, col = image.shape
    sigma2 = 2*ssigma*ssigma
    # gkernel = gaus_kernel(winsize, gsigma)
    gkernel = getKernel(winsize, gsigma)
    kernel = np.zeros((winsize, winsize))
    bilater_image = np.zeros((row, col))
    for i in range(1,row-r):
        for j in range(1,col-c):
            skernel = np.zeros((winsize, winsize))
            for m in range(-r, r+1):
                for n in range(-c, c+1):
                    #print(m, n)
                    #if (i != 0 and j !=0 and i != r and j !=c):
                    skernel[m+r][n+c] = np.exp(-pow((image[i][j]-image[i+m][j+n]),2)/sigma2)
                    # else:
                    #skernel[m+r][n+c] = np.exp(-pow((image[i][j]),2)/sigma2)
                    #print(skernel[m+r][n+c])
                    kernel[m+r][n+c] = skernel[m+r][n+r] * gkernel[m+r][n+r]
            sum_kernel = sum(sum(kernel))
            kernel = kernel/sum_kernel
            for m in range(-r, r+1):
                for n in range(-c, c+1):
                    bilater_image[i][j] =  image[i+m][j+n] * kernel[m+r][n+c] + bilater_image[i][j]

    return bilater_image














img = imageio.imread("../images/image2.jpg")

(height, width) = img.shape[:2]

if height <= width:
    start_from = int((width - height) / 2)
    square_img = np.uint8(cv2.cvtColor(img[:, start_from:start_from + height], cv2.COLOR_BGR2GRAY))
else:
    start_from = int((height - width) / 2)
    square_img = np.uint8(cv2.cvtColor(img[start_from:start_from + width, :], cv2.COLOR_BGR2GRAY))

resized_img = np.uint8(cv2.resize(square_img, (512, 512)))


mean = 0
sigma = 15

noise = np.random.normal(mean, sigma, resized_img.shape)
gauss_img = np.uint8(resized_img + noise)

# print("Noise: ", noise.shape, noise)

gauss_img[gauss_img > 255] = 255
gauss_img[gauss_img < 0] = 0

f_img = bilateral_filter(gauss_img.astype(float), 5, 10, 10)

plt.subplot(1, 2, 1)
plt.title("Noisy image")
plt.imshow(np.uint8(gauss_img), cmap='gray')

plt.subplot(1, 2, 2)
plt.title("Filtered image")
plt.imshow(np.uint8(f_img), cmap='gray')

plt.show()
