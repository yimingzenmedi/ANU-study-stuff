import imageio
import matplotlib.pyplot as plt
import numpy as np
import cv2

################################

# part 1

x_co = np.arange(256) + 1


def calcHist(_src):
    (_h, _w) = _src.shape
    n = _h * _w
    _out = np.zeros(256).astype(float)
    for i in _src:
        for j in i:
            _out[j] += 1
    _out = _out / n
    return _out


# 1. Crop square image and add Gaussian noise

# img = imageio.imread("../images/Lenna.png")
img = imageio.imread("../images/image2.jpg")

(height, width) = img.shape[:2]

if height <= width:
    start_from = int((width - height) / 2)
    square_img = np.uint8(cv2.cvtColor(img[:, start_from:start_from + height], cv2.COLOR_BGR2GRAY))
else:
    start_from = int((height - width) / 2)
    square_img = np.uint8(cv2.cvtColor(img[start_from:start_from + width, :], cv2.COLOR_BGR2GRAY))

resized_img = np.uint8(cv2.resize(square_img, (512, 512)))

# print(resized_img.shape)

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(img))

plt.subplot(1, 2, 2)
plt.title("Grayscale square image")
plt.imshow(np.uint8(resized_img), cmap='gray')

plt.show()

mean = 0
sigma = 15

noise = np.random.normal(mean, sigma, resized_img.shape)
gauss_img = np.uint8(resized_img + noise)

# print("Noise: ", noise.shape, noise)

gauss_img[gauss_img > 255] = 255
gauss_img[gauss_img < 0] = 0


hist_before = calcHist(square_img.astype("uint8"))
hist_after = calcHist(gauss_img.astype("uint8"))


plt.subplot(1, 2, 1)
plt.title("Before adding noise")
plt.bar(x_co, hist_before, facecolor="blue")

plt.subplot(1, 2, 2)
plt.title("After adding noise")
plt.bar(x_co, hist_after, facecolor="red")

plt.show()


# 2. implement Gaussian filtering

def my_Gauss_filter(noisy_img, kernel):
    _padding_h = kernel.shape[0] // 2
    _padding_w = kernel.shape[1] // 2
    (h, w) = noisy_img.shape

    # wrap padding:
    out = np.zeros((h + 2 * _padding_h, w + 2 * _padding_w), dtype=np.float)
    out[_padding_h:_padding_h + h, _padding_w:_padding_w + w] = noisy_img.copy().astype(np.float)

    out[:_padding_h, _padding_w:_padding_w + w] = noisy_img[h - _padding_h:, :]
    out[_padding_h + h:, _padding_w:_padding_w + w] = noisy_img[:_padding_h, :]
    out[_padding_h:_padding_h + h, :_padding_w] = noisy_img[:, w - _padding_w:]
    out[_padding_h:_padding_h + h, _padding_w + w:] = noisy_img[:, :_padding_w]

    out[:_padding_h, :_padding_w] = noisy_img[h - _padding_h:, w - _padding_w:]
    out[_padding_h + h:, :_padding_w] = noisy_img[:_padding_h, w - _padding_w:]
    out[:_padding_h, w + _padding_w:] = noisy_img[h - _padding_h:, :_padding_w]
    out[_padding_h + h:, _padding_w + w:] = noisy_img[:_padding_h, :_padding_w]

    # filtering:
    tmp = out.copy()
    for _y in range(h):
        for _x in range(w):
            out[_padding_h + _y, _padding_w + _x] = np.sum(kernel * tmp[_y:_y + kernel_size, _x:_x + kernel_size])

    out = out[_padding_h:_padding_h + h, _padding_w:_padding_w + w].astype(np.uint8)
    return out


# 3. Apply Gaussian filter with different standard deviations

kernel_size = 5

deviation1 = 1
deviation2 = 1.5
deviation3 = 3
deviation4 = 5
deviation5 = 8


def getKernel(_ksize, _sigma):
    _k = cv2.getGaussianKernel(ksize=_ksize, sigma=_sigma)
    return _k * _k.T


K1 = getKernel(kernel_size, deviation1)
print(K1)
filtered_img1 = my_Gauss_filter(gauss_img.astype(float), K1)

K2 = getKernel(kernel_size, deviation2)
print(K2)
filtered_img2 = my_Gauss_filter(gauss_img.astype(float), K2)

K3 = getKernel(kernel_size, deviation3)
print(K3)
filtered_img3 = my_Gauss_filter(gauss_img.astype(float), K3)

K4 = getKernel(kernel_size, deviation4)
print(K4)
filtered_img4 = my_Gauss_filter(gauss_img.astype(float), K4)

K5 = getKernel(kernel_size, deviation5)
print(K5)
filtered_img5 = my_Gauss_filter(gauss_img.astype(float), K5)

plt.subplot(2, 3, 1)
plt.title("Noisy image")
plt.imshow(np.uint8(gauss_img), cmap='gray')

plt.subplot(2, 3, 2)
plt.title("Sigma = %s" % deviation1)
plt.imshow(np.uint8(filtered_img1), cmap='gray')

plt.subplot(2, 3, 3)
plt.title("Sigma = %s" % deviation2)
plt.imshow(np.uint8(filtered_img2), cmap='gray')

plt.subplot(2, 3, 4)
plt.title("Sigma = %s" % deviation3)
plt.imshow(np.uint8(filtered_img3), cmap='gray')

plt.subplot(2, 3, 5)
plt.title("Sigma = %s" % deviation4)
plt.imshow(np.uint8(filtered_img4), cmap='gray')


plt.subplot(2, 3, 6)
plt.title("Sigma = %s" % deviation5)
plt.imshow(np.uint8(filtered_img5), cmap='gray')

plt.show()

# 4. Compare with inbuilt function

deviation = 50
inbuilt_res = cv2.GaussianBlur(gauss_img, ksize=(kernel_size, kernel_size), sigmaX=deviation, sigmaY=deviation)
my_res = my_Gauss_filter(gauss_img.astype(float), getKernel(kernel_size, deviation))

plt.subplot(1, 3, 1)
plt.title("Noisy image")
plt.imshow(np.uint8(gauss_img), cmap='gray')

plt.subplot(1, 3, 2)
plt.title("Inbuilt function result")
plt.imshow(np.uint8(inbuilt_res), cmap='gray')

plt.subplot(1, 3, 3)
plt.title("My function result")
plt.imshow(np.uint8(my_res), cmap='gray')

plt.show()


#################################

# Part 2

#################################

# 1. Implement Bilateral filter

def my_Bilateral_filter(noisy_img, kernel, color_sigma):
    _padding_h = kernel.shape[0] // 2
    _padding_w = kernel.shape[1] // 2
    (h, w) = noisy_img.shape

    # wrap padding:
    out = np.zeros((h + 2 * _padding_h, w + 2 * _padding_w), dtype=np.float)
    out[_padding_h:_padding_h + h, _padding_w:_padding_w + w] = noisy_img.copy().astype(np.float)

    out[:_padding_h, _padding_w:_padding_w + w] = noisy_img[h - _padding_h:, :]
    out[_padding_h + h:, _padding_w:_padding_w + w] = noisy_img[:_padding_h, :]
    out[_padding_h:_padding_h + h, :_padding_w] = noisy_img[:, w - _padding_w:]
    out[_padding_h:_padding_h + h, _padding_w + w:] = noisy_img[:, :_padding_w]

    out[:_padding_h, :_padding_w] = noisy_img[h - _padding_h:, w - _padding_w:]
    out[_padding_h + h:, :_padding_w] = noisy_img[:_padding_h, w - _padding_w:]
    out[:_padding_h, w + _padding_w:] = noisy_img[h - _padding_h:, :_padding_w]
    out[_padding_h + h:, _padding_w + w:] = noisy_img[:_padding_h, :_padding_w]

    # c_kernel = getKernel(kernel.shape[0], color_sigma)
    tmp = out.copy()
    for _y in range(h):
        for _x in range(w):
            c_kernel = np.zeros(kernel.shape)
            for i in range(c_kernel.shape[0]):
                for j in range(c_kernel.shape[1]):
                    _distance_sqr = pow((tmp[_y, _x] - tmp[_y + i - _padding_h, _x + j - _padding_w]), 2)
                    c_kernel[i][j] = np.exp(-_distance_sqr / (2 * (color_sigma ** 2)))
            k = kernel * c_kernel
            k = k / k.sum()
            out[_padding_h + _y, _padding_w + _x] = np.sum(k * tmp[_y:_y + kernel.shape[0], _x:_x + kernel.shape[1]])
    out = out[_padding_h:_padding_h + h, _padding_w:_padding_w + w].astype(np.uint8)
    return out


# 2. Apply the Bilateral filter to the above noisy image and try different color sigma value

color_sig_1 = 0.1
color_sig_2 = 0.5
color_sig_3 = 1
color_sig_4 = 5
color_sig_5 = 25

K = getKernel(kernel_size, deviation)

my_bilateral_filtered_img = my_Bilateral_filter(gauss_img.astype(float), K, color_sig_2)

plt.subplot(2, 3, 1)
plt.title("Noisy image")
plt.imshow(np.uint8(gauss_img), cmap='gray')

plt.subplot(2, 3, 2)
plt.title("color sigma=%.1f" % color_sig_1)
plt.imshow(np.uint8(my_Bilateral_filter(gauss_img.astype(float), K, color_sig_1)), cmap='gray')

plt.subplot(2, 3, 3)
plt.title("color sigma=%.1f" % color_sig_2)
plt.imshow(np.uint8(my_Bilateral_filter(gauss_img.astype(float), K, color_sig_2)), cmap='gray')

plt.subplot(2, 3, 4)
plt.title("color sigma=%.1f" % color_sig_3)
plt.imshow(np.uint8(my_Bilateral_filter(gauss_img.astype(float), K, color_sig_3)), cmap='gray')

plt.subplot(2, 3, 5)
plt.title("color sigma=%.1f" % color_sig_4)
plt.imshow(np.uint8(my_Bilateral_filter(gauss_img.astype(float), K, color_sig_4)), cmap='gray')

plt.subplot(2, 3, 6)
plt.title("color sigma=%.1f" % color_sig_5)
plt.imshow(np.uint8(my_Bilateral_filter(gauss_img.astype(float), K, color_sig_5)), cmap='gray')

plt.show()


# 3. Extend the Bilateral filter to colour images

def color_Bilateral_filter(_rgb_img, kernel, color_sigma):
    lab_img = cv2.cvtColor(_rgb_img, code=cv2.COLOR_RGB2LAB)
    print(lab_img.shape)
    (l, a, b) = cv2.split(lab_img)
    filtered_l = my_Bilateral_filter(l, kernel, color_sigma)
    filtered_a = my_Bilateral_filter(a, kernel, color_sigma)
    filtered_b = my_Bilateral_filter(b, kernel, color_sigma)
    # filtered_lab_img = cv2.merge((filtered_l, a, b))
    filtered_lab_img = cv2.merge((filtered_l, filtered_a, filtered_b))
    filtered_img = cv2.cvtColor(filtered_lab_img, cv2.COLOR_LAB2RGB)
    return filtered_img

def color_Bilateral_filter2(_rgb_img, kernel, color_sigma):
    r, g, b = cv2.split(_rgb_img)
    filtered_r = my_Bilateral_filter(r, kernel, color_sigma)
    filtered_g = my_Bilateral_filter(g, kernel, color_sigma)
    filtered_b = my_Bilateral_filter(b, kernel, color_sigma)
    filtered_img = cv2.merge((filtered_r, filtered_g, filtered_b))

    return filtered_img

if height <= width:
    start_from = int((width - height) / 2)
    square_img = np.uint8(img[:, start_from:start_from + height])
else:
    start_from = int((height - width) / 2)
    square_img = np.uint8(img[start_from:start_from + width, :])

resized_color_img = np.uint8(cv2.resize(square_img, (512, 512)))

color_noise = np.uint8(np.random.normal(0, 10, resized_color_img.shape))

noisy_color_img = resized_color_img + color_noise

noisy_color_img[noisy_color_img > 255] = 255
noisy_color_img[noisy_color_img < 0] = 0


filtered_color_img = color_Bilateral_filter(noisy_color_img, K, 1)

plt.subplot(1, 3, 1)
plt.title("Original noisy image")
plt.imshow(np.uint8(noisy_color_img))

plt.subplot(1, 3, 2)
plt.title("My function result")
plt.imshow(np.uint8(filtered_color_img))

plt.subplot(1, 3, 3)
plt.title("Inbuilt function result")
plt.imshow(np.uint8(cv2.bilateralFilter(noisy_color_img, kernel_size, 1, 50)))

plt.show()


# 4

filtered_color_img2 = color_Bilateral_filter2(noisy_color_img, K, 1)

plt.subplot(2, 2, 1)
plt.title("Original noisy image")
plt.imshow(np.uint8(noisy_color_img[:128, 400:, :]))

plt.subplot(2, 2, 2)
plt.title("Filtered in LAB")
plt.imshow(np.uint8(filtered_color_img)[:128, 400:, :])

plt.subplot(2, 2, 3)
plt.title("Filtered in RGB")
plt.imshow(np.uint8(filtered_color_img2[:128, 400:, :]))

plt.subplot(2, 2, 4)
plt.title("Gaussian filtered in RGB")
plt.imshow(np.uint8(cv2.GaussianBlur(noisy_color_img, (5, 5), deviation, sigmaY=deviation)[:128, 400:, :]))

plt.show()
