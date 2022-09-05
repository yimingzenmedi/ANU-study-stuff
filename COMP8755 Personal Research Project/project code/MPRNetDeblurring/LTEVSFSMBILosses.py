import torch.nn as nn
from torchvision.models import vgg16, VGG16_Weights


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


def adversarialLoss(x, y):
    print("> ", x.shape, y.shape)
    return nn.BCELoss(x, y)


class CenterEstiLoss(nn.Module):
    def __init__(self):
        super(CenterEstiLoss, self).__init__()

    def loss(self, x, y):
        loss = (y - x) ** 2 + perceptualLoss(y, x)
        return loss

    def forward(self, x4, y4):
        return self.loss(x4, y4).abs().sum()


class F17_N9Loss(nn.Module):
    def __init__(self):
        super(F17_N9Loss, self).__init__()

    def forward(self, x1, x7, y1, y7):
        pass


class F26_N9Loss(nn.Module):
    def __init__(self):
        super(F26_N9Loss, self).__init__()

    def forward(self):
        pass


class F35_N8Loss(nn.Module):
    def __init__(self):
        super(F35_N8Loss, self).__init__()

    def forward(self, x3, x5, y3, y5):
        loss = ((y3 + y5).abs() - (x3 + x5).abs()).abs() + ((y3 - y5).abs() - (x3 - x5).abs()).abs()
        print("> forward:\nloss:", loss.shape, ", adv:", adversarialLoss(x5, y5).shape)
        loss = loss + adversarialLoss(x3, y3) + adversarialLoss(x5, y5)
        return loss.sum()
