import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision.models import vgg16, VGG16_Weights
from image_proc import *


##################################################################################
# MPRNet losses:


def perceptualLoss(fakeIm, realIm):
    # print("\n!!! faleIm:", fakeIm.size(), ", realIm:", realIm.size())
    weights = [1, 1, 1]  # TODO: 改为relu2_2和relu3_3的层为1其他为0
    vggnet = vgg16(weights=VGG16_Weights.IMAGENET1K_V1).features.cuda()
    vggnet.eval()
    features_fake = vggnet(fakeIm)
    features_real = vggnet(realIm)
    features_real_no_grad = [f_real.detach() for f_real in features_real]
    mse_loss = nn.MSELoss(reduction='mean')

    loss = 0
    # print("!!! \nlen features_real:", len(features_real), ", \nlen features_real_no_grad:",
    # len(features_real_no_grad), ", \nlen features_fake:", len(features_fake))
    for i in range(len(features_real)):
        features_fake_i = features_fake[i]
        features_real_no_grad_i = features_real_no_grad[i]
        loss_i = mse_loss(features_fake_i, features_real_no_grad_i)
        loss = loss + sum([loss_i * weight for weight in weights])

    return loss


class CenterEstiLoss(nn.Module):
    def __init__(self):
        super(CenterEstiLoss, self).__init__()

    def loss(self, x, y):
        # print("y:", type(y), y.shape)
        # print("x:", type(x), len(x[0][0]))
        loss = (y - x) ** 2 + perceptualLoss(y, x)
        return loss

    def forward(self, x4, y4):
        return self.loss(x4, y4).abs().mean()


class F17_N9Loss_F26_N9Loss(nn.Module):
    def __init__(self):
        super(F17_N9Loss_F26_N9Loss, self).__init__()

    def forward(self, x1, x7, x2, x6, y1, y7, y2, y6):
        loss = ((x1 + x7) - (y1 + y7)).abs()
        loss += ((x1 - x7) - (y1 - y7)).abs()
        loss += ((x2 + x6) - (y2 + y6)).abs()
        loss += ((x2 - x6) - (y2 - y6)).abs()
        loss = loss.mean()
        return loss


class F35_N8Loss(nn.Module):
    def __init__(self):
        super(F35_N8Loss, self).__init__()

    def forward(self, x3, x5, y3, y5):
        # print("x3:", type(x3), x3[0].shape)
        # print("x5:", type(x5), len(x5))
        # print("y3:", type(y3), y3.shape)
        # print("y5:", type(y5), y5.shape)
        # x3 = torch.Tensor(x3)
        # x5 = torch.Tensor(x5)
        # print("x3_:", type(x3), x3.shape)
        # print("x5_:", type(x5), x5.shape)
        loss = ((x3 + x5) - (y3 + y5)).abs() + ((x3 - x5) - (y3 - y5)).abs()
        loss = loss.mean()
        # print(">> loss:", loss.data)
        return loss


class DetailedLoss(nn.Module):
    def __init__(self):
        super(DetailedLoss, self).__init__()

    def forward(self, x1, x2, x3, x4, x5, x6, x7, y1, y2, y3, y4, y5, y6, y7):
        return


##################################################################################
# Detailed losses:

class CharbonnierLoss(nn.Module):
    """Charbonnier Loss (L1)"""

    def __init__(self, eps=1e-3):
        super(CharbonnierLoss, self).__init__()
        self.eps = eps

    def forward(self, x, y):
        diff = x - y
        # loss = torch.sum(torch.sqrt(diff * diff + self.eps))
        loss = torch.mean(torch.sqrt((diff * diff) + (self.eps * self.eps)))
        return loss


class EdgeLoss(nn.Module):
    def __init__(self):
        super(EdgeLoss, self).__init__()
        k = torch.Tensor([[.05, .25, .4, .25, .05]])
        self.kernel = torch.matmul(k.t(), k).unsqueeze(0).repeat(3, 1, 1, 1)
        if torch.cuda.is_available():
            self.kernel = self.kernel.cuda()
        self.loss = CharbonnierLoss()

    def conv_gauss(self, img):
        n_channels, _, kw, kh = self.kernel.shape
        img = F.pad(img, (kw // 2, kh // 2, kw // 2, kh // 2), mode='replicate')
        return F.conv2d(img, self.kernel, groups=n_channels)

    def laplacian_kernel(self, current):
        filtered = self.conv_gauss(current)  # filter
        down = filtered[:, :, ::2, ::2]  # downsample
        new_filter = torch.zeros_like(filtered)
        new_filter[:, :, ::2, ::2] = down * 4  # upsample
        filtered = self.conv_gauss(new_filter)  # filter
        diff = current - filtered
        return diff

    def forward(self, x, y):
        loss = self.loss(self.laplacian_kernel(x), self.laplacian_kernel(y))
        return loss