import numpy as np


img = np.array([
    [3,2,1,2,4],
    [2,1,3,200,3],
    [6,7,8,7,9],
    [8,100,6,6,7],
    [7,9,6,8,8]
]).T

print(img)

# for i in range(1, 4):
#     for j in range(1, 4):
#         i1, i2 = i-1, i+2
#         j1, j2 = j-1, j+2
#         res = img[i1:i2, j1:j2].sum() / 9
        
#         print(res)

f1 = np.array([
    [-1, -2, -1],
    [0,0,0],
    [1,2,1]
])



for i in range(1, 4):
    for j in range(1, 4):
        i1, i2 = i-1, i+2
        j1, j2 = j-1, j+2
        m = img[i1:i2, j1:j2]
        # res = f1 * m
        res = np.dot(f1, m)
        
        print(res.sum())