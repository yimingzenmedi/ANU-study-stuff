import imageio
import matplotlib.pyplot as plt
import numpy as np

# 1. Negate the image

img = imageio.imread("../images/Atowergray.jpg")
negative_img = 255 - img

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(img), cmap='gray')

plt.subplot(1, 2, 2)
plt.title("Negative image")
plt.imshow(np.uint8(negative_img), cmap='gray')

plt.show()


# 2. Flip the image horizontally

# flipped_img = cv2.flip(img, 1)
flipped_img = img.T[::-1].T

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(img), cmap='gray')

plt.subplot(1, 2, 2)
plt.title("Flipped image")
plt.imshow(np.uint8(flipped_img), cmap='gray')

plt.show()


# 3. Swap red and green color channels

color_img = imageio.imread("../images/image1.jpg")

print(color_img.shape)

swapped_img = color_img.copy()
tmp_r = swapped_img[:, :, 0].copy()

swapped_img[:, :, 0] = swapped_img[:, :, 1]
swapped_img[:, :, 1] = tmp_r

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(color_img))

plt.subplot(1, 2, 2)
plt.title("Swapped image")
plt.imshow(np.uint8(swapped_img))

plt.show()


# 4. Average the input image with its horizontally flipped image

# mixed_img = cv2.addWeighted(img, 0.5, flipped_img, 0.5, 0)
mixed_img = np.uint8((img.astype(float) + flipped_img.astype(float)) * 0.5)

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(img), cmap='gray')

plt.subplot(1, 2, 2)
plt.title("Mixed image")
plt.imshow(np.uint8(mixed_img), cmap='gray')

plt.show()


# 5. Add random value and clip

noise = np.random.randint(low=0, high=128, size=img.shape)

clipped_img = img + noise

clipped_img[clipped_img > 255] = 255
clipped_img[clipped_img < 0] = 0

plt.subplot(1, 2, 1)
plt.title("Original image")
plt.imshow(np.uint8(img), cmap='gray')

plt.subplot(1, 2, 2)
plt.title("Clipped image")
plt.imshow(np.uint8(clipped_img), cmap='gray')

plt.show()
