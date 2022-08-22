import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision.models import vgg16


def perceptualLoss(fakeIm, realIm):
    # print("\n!!! faleIm:", fakeIm.size(), ", realIm:", realIm.size())
    weights = [1, 1, 1]       # TODO: 改为relu2_2和relu3_3的层为1其他为0
    vggnet = vgg16(pretrained=True).features.cuda()
    vggnet.eval()
    features_fake = vggnet(fakeIm)
    features_real = vggnet(realIm)
    features_real_no_grad = [f_real.detach() for f_real in features_real]
    mse_loss = nn.MSELoss(reduction='mean')

    loss = 0
    # print("!!! \nlen features_real:", len(features_real), ", \nlen features_real_no_grad:", len(features_real_no_grad), ", \nlen features_fake:", len(features_fake))
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
        diff = (y - x) ** 2 + perceptualLoss(y, x)

        return diff

    def forward(self, x4, y4):
        return self.loss(x4, y4).mean()


class F17_N9Loss(nn.Module):
    def __init__(self):
        super(F17_N9Loss, self).__init__()

    def forward(self):
        pass


class F26_N9Loss(nn.Module):
    def __init__(self):
        super(F26_N9Loss, self).__init__()

    def forward(self):
        pass


class F35_N8Loss(nn.Module):
    def __init__(self):
        super(F35_N8Loss, self).__init__()

    def forward(self):
        pass
