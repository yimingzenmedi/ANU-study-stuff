import imageio
import matplotlib.pyplot as plt
import numpy as np
import cv2

# 1.a. Read image 1 and resize the image to 384 x 256 in columns x rows

img = imageio.imread("../images/image1.jpg")
# print(img.shape)

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(img))

img = cv2.resize(img, (384, 256))
# print(resized_img.shape)

plt.subplot(1, 2, 2)
plt.title("Resized image")
plt.imshow(np.uint8(img))

plt.show()

# 1.b. Convert the colour image into three grayscale channels and display each separately

# (r, g, b) = cv2.split(img)
r = img[:, :, 0]
g = img[:, :, 1]
b = img[:, :, 2]

plt.subplot(2, 2, 1)
plt.title("Red channel")
plt.imshow(np.uint8(r), cmap='gray')

plt.subplot(2, 2, 2)
plt.title("Green channel")
plt.imshow(np.uint8(g), cmap='gray')

plt.subplot(2, 2, 3)
plt.title("Blue channel")
plt.imshow(np.uint8(b), cmap='gray')

# plt.subplot(2, 2, 4)
# plt.title("Original image")
# plt.imshow(np.uint8(img))

plt.show()


# 1.c. Compute the histograms for each grayscale image and display

def calcHist(_src):
    (_h, _w) = _src.shape
    n = _h * _w
    _out = np.zeros(256).astype(float)
    for i in _src:
        for j in i:
            _out[j] += 1
    _out = _out / n
    return _out


hist_r = calcHist(r)
hist_g = calcHist(g)
hist_b = calcHist(b)

x_co = np.arange(256) + 1

plt.subplot(2, 2, 1)
plt.title("Red channel histogram")
plt.bar(x_co, hist_r, facecolor="red")

plt.subplot(2, 2, 2)
plt.title("Green channel histogram")
plt.bar(x_co, hist_g, facecolor="green")

plt.subplot(2, 2, 3)
plt.title("Blue channel histogram")
plt.bar(x_co, hist_b, facecolor="blue")
# plt.plot(hist_b, 'b')

# plt.subplot(2, 2, 4)
# plt.title("Original image histogram")
# # plt.plot(hist_r, 'r')
# plt.hist(img.ravel(), 256)

plt.show()

# 1.d Apply histogram equalisation to the resized image and three grayscale channels then display the histograms

(resized_r, resized_g, resized_b) = cv2.split(img)

# equalised_resized_img = cv2.equalizeHist(np.uint8(img))
equalised_r = cv2.equalizeHist(resized_r)
equalised_g = cv2.equalizeHist(resized_g)
equalised_b = cv2.equalizeHist(resized_b)
equalised_resized_img = cv2.merge([equalised_r, equalised_g, equalised_b])

plt.subplot(2, 2, 1)
plt.title("Equalised resized image")
plt.imshow(np.uint8(equalised_resized_img))

plt.subplot(2, 2, 2)
plt.title("Equalised red channel")
plt.imshow(np.uint8(equalised_r), cmap='gray')

plt.subplot(2, 2, 3)
plt.title("Equalised green channel")
plt.imshow(np.uint8(equalised_g), cmap='gray')

plt.subplot(2, 2, 4)
plt.title("Equalised blue channel")
plt.imshow(np.uint8(equalised_b), cmap='gray')

plt.show()


# test:
hist_r = calcHist(equalised_r)
hist_g = calcHist(equalised_g)
hist_b = calcHist(equalised_b)

plt.subplot(2, 2, 1)
plt.title("Red channel histogram")
plt.bar(x_co, hist_r, facecolor="red")

plt.subplot(2, 2, 2)
plt.title("Green channel histogram")
plt.bar(x_co, hist_g, facecolor="green")

plt.subplot(2, 2, 3)
plt.title("Blue channel histogram")
plt.bar(x_co, hist_b, facecolor="blue")
# plt.plot(hist_b, 'b')

plt.show()
