import numpy as np
import cv2
import matplotlib.pyplot as plt
import os

# (1) ===============================================================

test_path = "..\\..\\img\\Task3\\Yale-FaceA\\testset\\"
training_path = "..\\..\\img\\Task3\\Yale-FaceA\\trainingset\\"
trainingpp_path = "..\\..\\img\\Task3\\Yale-FaceA\\trainingsetpp\\"


def loadImgs(path: str, h=231, w=195):
    fileList = os.listdir(path)
    imgs = []
    for file in fileList:
        img = cv2.imread(path + file, cv2.IMREAD_GRAYSCALE)
        img = cv2.resize(img, (w, h))
        #         plt.imshow(np.uint8(img), cmap="gray")
        vector = img.reshape(h * w)
        imgs.append(vector)
    return np.mat(imgs).T


h = 231
w = 195

training_set = loadImgs(training_path, h, w)
trainingpp_set = loadImgs(trainingpp_path, h, w)  # training set with my pictures
test_set = loadImgs(test_path, h, w)
print(training_set.shape)
print(trainingpp_set.shape)
print(test_set.shape)


# (2) ===============================================================

def pca(data, k: int):
    # Get the mean image:
    meanImg = data.mean(axis=1)
    # Normalize and get the deviation matrix:
    devi = data - meanImg
    deviMat = devi.T * devi
    # Calculate feature vector and feature value:
    vals, vects = np.linalg.eig(np.mat(deviMat))
    # Get the index of the k biggest features:
    valsIndex = np.argsort(-vals)
    valsIndex = valsIndex[:k]
    # eigenvector of convariance matrix:
    eigenVects = devi * vects[:, valsIndex]

    return meanImg, eigenVects, devi


# (3) ===============================================================

k = 15
meanImg, eigenVects, devi = pca(training_set, k)
print("meanImg:", meanImg.shape)
print("eigenVects:", eigenVects.shape)
print("devi:", devi.shape)
plt.title("mean face")
plt.imshow(np.uint8(meanImg.reshape(h, w)), cmap="gray")
plt.show()

lines = k // 5
mod = k % 5
if mod > 0:
    lines += 1
for i in range(k):
    img = (devi.T)[i].reshape(h, w)
    plt.subplot(lines, 5, i+1)
    plt.title("eigenface " + str(i + 1))
    # print(img)
    plt.imshow(img, cmap="gray")
plt.show()


# (4) ===============================================================

ref = test_set[:, 0]


def getSimilarFace(refImg, meanImg, eigenVects, devi, n=3):
    _, s = np.shape(devi)
    diff = refImg - meanImg
    # feature vector
    feaVect = eigenVects.T * diff
    # initialize result
    # res = [np.inf for _ in range(s)]
    res = np.array([np.inf for _ in range(s)]).astype(np.float32)

    for i in range(s):
        trainVect = eigenVects.T * devi[:, i]
        distance = np.sum((np.array(feaVect) - np.array(trainVect)) ** 2)
        res[i] = distance

    resIndex = np.argsort(res)
    resIndex = resIndex[:n]
    return resIndex


res_index = getSimilarFace(ref, meanImg, eigenVects, devi, n=3)
plt.figure(figsize=(7, 7))
plt.subplot(2, 2, 1)
plt.title("reference")
plt.imshow(ref.reshape(h, w), cmap="gray")
counter = 2
for index in res_index:
    img = training_set[:, index]
    img = img.reshape(h, w)
    plt.subplot(2, 2, counter)
    plt.title("image index: " + str(index))
    print("image index: " + str(index))
    plt.imshow(img.reshape(h, w), cmap="gray")
    counter += 1

plt.show()


# (5) ===============================================================

my_ref = test_set[:, -1]
plt.title("my reference")
plt.imshow(my_ref.reshape(h, w), cmap="gray")
plt.show()


res_index = getSimilarFace(my_ref, meanImg, eigenVects, devi, n=3)
# cv2.imshow("reference", ref.reshape(h, w))
plt.figure(figsize=(7, 7))
plt.subplot(2, 2, 1)
plt.title("my reference")
plt.imshow(my_ref.reshape(h, w), cmap="gray")
counter = 2
for index in res_index:
    img = training_set[:, index]
    img = img.reshape(h, w)
    plt.subplot(2, 2, counter)
    plt.title("image index: " + str(index))
    print("image index: " + str(index))
    plt.imshow(img.reshape(h, w), cmap="gray")
    counter += 1

plt.show()


# (6) ===============================================================

k = 15
meanImg, eigenVects, devi = pca(trainingpp_set, k)
print("meanImg:", meanImg.shape)
print("eigenVects:", eigenVects.shape)
print("devi:", devi.shape)
plt.imshow(np.uint8(meanImg.reshape(h, w)), cmap="gray")
plt.show()


res_index = getSimilarFace(my_ref, meanImg, eigenVects, devi, n=3)
# cv2.imshow("reference", ref.reshape(h, w))
plt.figure(figsize=(7, 7))
plt.subplot(2, 2, 1)
plt.title("my reference")
plt.imshow(my_ref.reshape(h, w), cmap="gray")
counter = 2
for index in res_index:
    img = trainingpp_set[:, index]
    img = img.reshape(h, w)
    plt.subplot(2, 2, counter)
    plt.title("image index: " + str(index))
    print("image index: " + str(index))
    plt.imshow(img.reshape(h, w), cmap="gray")
    counter += 1

plt.show()

print("done")
