import numpy as np

# 1
a = np.array([[2, 3, 4], [5, 2, 200]])
# print("a: ", a)

# 2
b = a[:, 1]
# print("b: ", b)

# 3
f = np.random.randn(400, 2)
# print("f: ", f)

# 4
g = f[f > 0] * 3
# print("g: ", g.size, g, type(g))

# 5
x = np.zeros(100) + 0.45
# print("x: ", x)

# 6
y = 0.5 * np.ones([1, len(x)])
# print("y: ", y)

# 7
z = x + y
# print("z: ", z)

# 8
a = np.linspace(1, 499, 250, dtype=int)
print("a: ", a)
# a_ = np.linspace(1, 499, 250, dtype=float)
#
# size = a.size
# for i in range(0, size):
#     print("a: {0}, a_: {1}".format(a[i], a_[i]))


# 9
b = a[::-2]
print("b: ", b, b.size)

# 10
# b[b > 50] = 0
# print("b: ", b)
