from __future__ import print_function
import argparse
import torch
from torch.autograd import Variable
from torchvision import transforms
import utils
from LTEVSFSMBI import centerEsti
from LTEVSFSMBI import F26_N9
from LTEVSFSMBI import F17_N9
from LTEVSFSMBI import F35_N8

# Training settings
parser = argparse.ArgumentParser(description='parser for video prediction')
parser.add_argument('--input', type=str, required=True, help='input image')
parser.add_argument('--cuda', action='store_true', help='use cuda')
parser.add_argument('--model', type=int, default=1)

args = parser.parse_args()

if args.model == 1:  # Learning to Extract a Video Sequence from a Single Motion Blurred-Image
    # load model
    model1 = centerEsti()
    # model2 = F35_N8()
    # model3 = F26_N9()
    # model4 = F17_N9()

    # checkpoint = torch.load('ckpt/centerEsti/models/LTEVSFSMBI_reduced/centerEsti_model_latest.pth')
    checkpoint = torch.load('ckpt\\centerEsti\\models\\LTEVSFSMBI\\centerEsti_model_latest.pth')
    model1.load_state_dict(checkpoint['state_dict'])

    checkpoint = torch.load('pretrained_models/center_v3.pth')
    model1.load_state_dict(checkpoint['state_dict_G'])
    # checkpoint = torch.load('pretrained_models/F35_N8.pth')
    # model2.load_state_dict(checkpoint['state_dict_G'])
    # checkpoint = torch.load('pretrained_models/F26_N9_from_F35_N8.pth')
    # model3.load_state_dict(checkpoint['state_dict_G'])
    # checkpoint = torch.load('pretrained_models/F17_N9_from_F26_N9_from_F35_N8.pth')
    # model4.load_state_dict(checkpoint['state_dict_G'])

    if args.cuda:
        model1.cuda()
        # model2.cuda()
        # model3.cuda()
        # model4.cuda()

    model1.eval()
    # model2.eval()
    # model3.eval()
    # model4.eval()

    inputFile = args.input
    input_ = utils.load_image(inputFile)
    width, height = input_.size
    input_ = input_.crop((0, 0, width - width % 20, height - height % 20))
    input_transform = transforms.Compose([
        transforms.ToTensor(),
    ])
    input_ = input_transform(input_)
    input_ = input_.unsqueeze(0)
    if args.cuda:
        input_ = input_.cuda()

    with torch.no_grad():
        input_ = Variable(input_)
    output4 = model1(input_)
    # output3_5 = model2(input_, output4)
    # output2_6 = model3(input_, output3_5[0], output4, output3_5[1])
    # output1_7 = model4(input_, output2_6[0], output3_5[0], output3_5[1], output2_6[1])
    if args.cuda:
        # output1 = output1_7[0].cpu()
        # output2 = output2_6[0].cpu()
        # output3 = output3_5[0].cpu()
        output4 = output4.cpu()
        # output5 = output3_5[1].cpu()
        # output6 = output2_6[1].cpu()
        # output7 = output1_7[1].cpu()
    else:
        # output1 = output1_7[0]
        # output2 = output2_6[0]
        # output3 = output3_5[0]
        output4 = output4
        # output5 = output3_5[1]
        # output6 = output2_6[1]
        # output7 = output1_7[1]
    fileName = inputFile.split("/")[-1][:-4]
    # output_data = output1.data[0] * 255
    # utils.save_image("sharp_outputs\\" + fileName + '-esti1' + inputFile[-4:], output_data)
    # output_data = output2.data[0] * 255
    # utils.save_image("sharp_outputs\\" + fileName + '-esti2' + inputFile[-4:], output_data)
    # output_data = output3.data[0] * 255
    # utils.save_image("sharp_outputs\\" + fileName + '-esti3' + inputFile[-4:], output_data)
    output_data = output4.data[0] * 255
    utils.save_image("sharp_outputs\\" + fileName + '-esti4' + inputFile[-4:], output_data)
    # output_data = output5.data[0] * 255
    # utils.save_image("sharp_outputs\\" + fileName + '-esti5' + inputFile[-4:], output_data)
    # output_data = output6.data[0] * 255
    # utils.save_image("sharp_outputs\\" + fileName + '-esti6' + inputFile[-4:], output_data)
    # output_data = output7.data[0] * 255
    # utils.save_image("sharp_outputs\\" + fileName + '-esti7' + inputFile[-4:], output_data)

# elif args.model == 2:
#
