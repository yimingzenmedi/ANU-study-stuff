import numpy as np
import cv2
import matplotlib.pyplot as plt
from datetime import datetime


# calculate the distance between two vectors:
def calc_distance(v1, v2):
    dist = np.sqrt(np.sum(np.square(v1 - v2)))
    return dist


# 1. ==============================================================================

# my kmeans function:
def my_kmeans(data, k):
    print("std kmeans:")
    start = datetime.now()

    s, c = data.shape
    # print("Chanels: ", c)

    centers = []

    # Randomly initialize cluster centers:
    for i in range(k):
        ran_i = np.random.randint(0, s)
        print(ran_i)
        centers.append(data[ran_i])
    #     print("centers: ", len(centers), centers)

    # run cluster
    labels = np.zeros(s)
    new_centers = centers.copy()
    # print("centers:", centers)
    finished = False
    counter = 1
    while not finished:
        # print("***** Round:", counter)
        counter += 1

        labels = np.zeros(s)      # reset labels
        finished = True

        for i in range(s):
            distances = []
            # calculate the distance to each center
            for ci in range(k):
                center = centers[ci]
                distances.append(calc_distance(center, data[i]))
            min_dist_ci = distances.index(min(distances))
            #             print("distances:", distances, ", min:", min_dist_ci)
            labels[i] = min_dist_ci

        #         print("labels:", labels)
        # calculate and update center
        for ci in range(k):
            #             print("\n", ci, ":", labels[labels == ci].size)
            if labels[labels == ci].size:
                new_centers[ci] = np.mean(data[labels == ci], axis=0)
            else:
                new_centers[ci] = data[np.random.randint(0, s)]

            # finished?
            if (new_centers[ci] != centers[ci]).any():
                #                 print(ci, " not satisfied：")
                #                 print("old: ", centers[ci])
                #                 print("new: ", new_centers[ci])
                finished = False

        centers = new_centers.copy()               # update centers

    end = datetime.now()
    print("Rounds:", counter)
    print("Time:", (end - start).seconds)

    return labels


# build data by the image
def get_data(img, withCoordinates=True):
    c = 5 if withCoordinates else 3
    h, w, _ = img.shape
    img_ = cv2.cvtColor(img, cv2.COLOR_RGB2LAB)
    data = np.zeros((h, w, c)).astype(np.float32)
    for i in range(h):
        for j in range(w):
            pl, pa, pb = img_[i, j]
            if withCoordinates:
                data[i][j] = np.array([pl, pa, pb, i, j])
            else:
                data[i][j] = np.array([pl, pa, pb])
    return data.reshape(h * w, c)


# generate k hsv colors
def get_hsv_colors(k):
    colors = []
    div = 360 / k
    for h in range(k):
        colors.append((h * div, 1, 255))
    return colors


# 2. ==========================================================================================
#
path = "..\\..\\img\\Task2\\"
# # img = plt.imread('Task2\mandm.png')
# img = cv2.imread(path + 'peppers.png')
# print(img.shape)
# h, w, c = img.shape
#
# plt.title('peppers.png')
# plt.imshow(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
# plt.show()
#
# data = get_data(img)
#
# k = 4
k2 = 6
#
# print(data.shape)
# res = my_kmeans(data, k)
#
# print("\n\nres:", res)
#
#
colors = get_hsv_colors(k2)
# print(colors)
#
# new_img = np.zeros((h * w, 3))
#
# for ci in range(k):
#     new_img[res == ci] = colors[ci]
#     print(new_img[res == ci].size)
#
# new_img = new_img.reshape((h, w, 3)).astype(np.float32)
# new_img = cv2.cvtColor(new_img, cv2.COLOR_HSV2RGB)
#
# print(new_img.shape)
#
# plt.title('peppers.png with coordinates k=' + str(k))
# plt.imshow(new_img / 255.0)
# plt.show()
#
#
# # k = 6:
#
# res = my_kmeans(data, k=k2)
# new_img = np.zeros((h * w, 3))
#
# for ci in range(k2):
#     new_img[res == ci] = colors[ci]
#     print(new_img[res == ci].size)
#
# new_img = new_img.reshape((h, w, 3)).astype(np.float32)
# new_img = cv2.cvtColor(new_img, cv2.COLOR_HSV2RGB)
#
# print(new_img.shape)
#
# plt.title('peppers.png with coordinates k=' + str(k2))
# plt.imshow(new_img / 255.0)
# plt.show()
#
#
# # without position:
#
# data2 = get_data(img, False)
#
# print(data2.shape)
#
# res2 = my_kmeans(data2, k)
#
# print("\n\nres:", res2)
#
#
# new_img2 = np.zeros((h * w, 3))
#
# for ci in range(k):
#     new_img2[res2 == ci] = colors[ci]
#     print(new_img2[res2 == ci].size)
#
# new_img2 = new_img2.reshape((h, w, 3)).astype(np.float32)
# new_img2 = cv2.cvtColor(new_img2, cv2.COLOR_HSV2RGB)
#
# print(new_img2.shape)
#
# plt.title('peppers.png without coordinates k=' + str(k))
# plt.imshow(new_img2 / 255.0)
# plt.show()
#
#
# res2 = my_kmeans(data2, k2)
#
# print("\n\nres:", res2)
#
#
# new_img2 = np.zeros((h * w, 3))
#
# for ci in range(k2):
#     new_img2[res2 == ci] = colors[ci]
#     print(new_img2[res2 == ci].size)
#
# new_img2 = new_img2.reshape((h, w, 3)).astype(np.float32)
# new_img2 = cv2.cvtColor(new_img2, cv2.COLOR_HSV2RGB)
#
# print(new_img2.shape)
#
# plt.title('peppers.png without coordinates k=' + str(k2))
# plt.imshow(new_img2 / 255.0)
# plt.show()


# mandm.png

img2 = cv2.imread(path + 'mandm.png')
print(img2.shape)
h2, w2, c2 = img2.shape
#
# plt.title("mandm.png")
# plt.imshow(cv2.cvtColor(img2, cv2.COLOR_BGR2RGB))
# plt.show()
#
# data3 = get_data(img2, False)
#
# print(data3.shape)
# start3 = datetime.now()
# res3 = my_kmeans(data3, k)
# end3 = datetime.now()
#
# print("Time:", (end3 - start3).seconds)
#
# print("\n\nres2:", res3)
#
#
#
# new_img3 = np.zeros((h2 * w2, 3))
# print(colors)
#
# for ci in range(k):
#     new_img3[res3 == ci] = colors[ci]
#     print(new_img3[res3 == ci].size)
#
# new_img3 = new_img3.reshape((h2, w2, 3)).astype(np.float32)
# new_img3 = cv2.cvtColor(new_img3, cv2.COLOR_HSV2RGB)
#
# print(new_img3.shape)
#
# plt.title('mandm.png without coordinates k=' + str(k))
# plt.imshow(new_img3 / 255.0)
# plt.show()
#
# # with coordinates:
#
# data3 = get_data(img2, True)
#
# print(data3.shape)
# start3 = datetime.now()
# res3 = my_kmeans(data3, k)
# end3 = datetime.now()
#
# print("Time:", (end3 - start3).seconds)
#
# new_img3 = np.zeros((h2 * w2, 3))
#
# for ci in range(k):
#     new_img3[res3 == ci] = colors[ci]
#     print(new_img3[res3 == ci].size)
#
# new_img3 = new_img3.reshape((h2, w2, 3)).astype(np.float32)
# new_img3 = cv2.cvtColor(new_img3, cv2.COLOR_HSV2RGB)
#
# print(new_img3.shape)
#
# plt.title('mandm.png with coordinates k=' + str(k))
# plt.imshow(new_img3 / 255.0)
# plt.show()
#

# 3. ====================================================================

# K-means++

def my_kmeans_pp(data, k):
    print("k-means++:")
    start = datetime.now()

    s, c = data.shape

    # Initialize cluster centers:
    centers = []
    centers.append(data[np.random.randint(0, s)])
    d = np.zeros(s)
    for _ in range(k - 1):
        total = 0.0
        for i in range(s):
            distances = []
            for center in centers:
                distances.append(calc_distance(center, data[i]))
            d[i] = distances.index(min(distances))
            total += d[i]
        total *= np.random.random()
        for i in range(s):
            total -= d[i]
            if total > 0:
                continue
            centers.append(data[i])
            break

    # endt = datetime.now()
    # print("Initialize Time:", (endt - startt).seconds)
    # print("centers: ", len(centers), centers)

    # run cluster
    # startt = datetime.now()
    new_centers = centers.copy()
    labels = np.zeros(s)
    finished = False
    counter = 1
    while not finished:
        # print("***** Round:", counter)
        counter += 1

        labels = np.zeros(s)      # reset labels
        finished = True

        for i in range(s):
            distances = []
            # calculate the distance to each center
            for ci in range(k):
                center = centers[ci]
                distances.append(calc_distance(center, data[i]))
            min_dist_ci = distances.index(min(distances))
            # print("distances:", distances, ", min:", min_dist_ci)
            labels[i] = min_dist_ci

        # print("labels:", labels)
        # calculate and update center
        for ci in range(k):
            if labels[labels == ci].size:
                new_centers[ci] = np.mean(data[labels == ci], axis=0)
            else:
                new_centers[ci] = data[np.random.randint(0, s)]

            # finished?
            if (new_centers[ci] != centers[ci]).any():
                #                 print(ci, " not satisfied：")
                #                 print("old: ", centers[ci])
                #                 print("new: ", new_centers[ci])
                finished = False

        centers = new_centers.copy()               # update centers

    end = datetime.now()
    print("Rounds:", counter)
    print("Time:", (end - start).seconds)

    return labels


data4 = get_data(img2, True)

k = 3

# Kmeans++

start4 = datetime.now()
res4 = my_kmeans_pp(data4, k)
end4 = datetime.now()

new_img4 = np.zeros((h2 * w2, 3))
# print(colors)

for ci in range(k):
    new_img4[res4 == ci] = colors[ci]
    print(new_img4[res4 == ci].size, colors[ci])

new_img4 = new_img4.reshape((h2, w2, 3)).astype(np.float32)
new_img4 = cv2.cvtColor(new_img4, cv2.COLOR_HSV2RGB)

print(new_img4.shape)

plt.title('mandm.png by kmeans++ k=' + str(k))
plt.imshow(new_img4 / 255.0)
plt.show()

# std kmeans:

print(data4.shape)
start4 = datetime.now()
res4 = my_kmeans(data4, k)
end4 = datetime.now()

print("Time:", (end4 - start4).seconds)

print("\n\nres4:", res4)


new_img4 = np.zeros((h2 * w2, 3))
# print(colors)

for ci in range(k):
    new_img4[res4 == ci] = colors[ci]
    print(new_img4[res4 == ci].size, colors[ci])

new_img4 = new_img4.reshape((h2, w2, 3)).astype(np.float32)
new_img4 = cv2.cvtColor(new_img4, cv2.COLOR_HSV2RGB)

print(new_img4.shape)

plt.title('mandm.png by kmeans k=' + str(k))
plt.imshow(new_img4 / 255.0)
plt.show()


k = 5

# Kmeans++

start4 = datetime.now()
res4 = my_kmeans_pp(data4, k)
end4 = datetime.now()

new_img4 = np.zeros((h2 * w2, 3))
# print(colors)

for ci in range(k):
    new_img4[res4 == ci] = colors[ci]
    print(new_img4[res4 == ci].size, colors[ci])

new_img4 = new_img4.reshape((h2, w2, 3)).astype(np.float32)
new_img4 = cv2.cvtColor(new_img4, cv2.COLOR_HSV2RGB)

print(new_img4.shape)

plt.title('mandm.png by kmeans++ k=' + str(k))
plt.imshow(new_img4 / 255.0)
plt.show()

# std kmeans:

print(data4.shape)
start4 = datetime.now()
res4 = my_kmeans(data4, k)
end4 = datetime.now()

print("Time:", (end4 - start4).seconds)

print("\n\nres:", res4)


new_img4 = np.zeros((h2 * w2, 3))
# print(colors)

for ci in range(k):
    new_img4[res4 == ci] = colors[ci]
    print(new_img4[res4 == ci].size, colors[ci])

new_img4 = new_img4.reshape((h2, w2, 3)).astype(np.float32)
new_img4 = cv2.cvtColor(new_img4, cv2.COLOR_HSV2RGB)

print(new_img4.shape)

plt.title('mandm.png by kmeans k=' + str(k))
plt.imshow(new_img4 / 255.0)
plt.show()

