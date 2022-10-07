
import matplotlib.image as mpimg
import matplotlib.pyplot as plt

path = "sharp_outputs/b5-esti3-pre.png"

img = mpimg.imread(path)

for c in img:
    for i in c:
        for j in i:
            if j > 1:
                print(j, "> 1")
            elif j < 0:
                print(j, "< 0")

# print(img)
