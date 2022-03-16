This is a reproduce based on the paper "Fast, Accurate, and Lightweight Super-Resolution with Cascading Residual Network". 
The details of the paper can be found in "Ahn, N., Kang, B., &amp; Sohn, K.-A. (2018). Fast, accurate, and lightweight super-resolution with cascading residual network. Computer Vision – ECCV 2018, 256–272. https://doi.org/10.1007/978-3-030-01249-6_16".

This reproduce is completed by Ziyang Chen (u6908560) and Han Zhang (u7235649) for ENGN8501 in Australian National University and refers to "https://github.com/nmhkahn/CARN-pytorch".

---

## Requirements:
* Python 3, 
* Pytorch, 
* torchvision,
* h5py,
* numpy, 
* scipy,
* importlib.

## Dataset:
* training:
    * DIV2K (DIV2K_train_HR, DIV2K_train_LR_bicubic, DIV2K_valid_HR, DIV2K_valid_LR_bicubic) 
* validation and testing:
    * Set5, 
    * Set14, 
    * B100,
    * Urban100
    * Unnatural (self collected)
    
The DIV2K dataset can be downloaded from https://data.vision.ee.ethz.ch/cvl/DIV2K/.
The Unnatural dataset can be downloaded from https://drive.google.com/drive/folders/1kwJZwe4ewTsURt747T26uFSmak0BDb1-?usp=sharing.
The other datasets can be downloaded from https://github.com/ChaofWang/Awesome-Super-Resolution/blob/master/dataset.md.

## Contents:

```
|---project
    |---checkpoint  (save the trained mode)
    |---code
        |---main.py  (project entry)
        |---ops.py  (shared model blocks)
        |---carn.py  (carn model)
        |---carn_m.py  (carn_m model)
        |---dataset.py  (dataset loader)
        |---test.py  (for model testing)
        |---train.py  (for model training)
        |---utils.py (for calculating PSNR and SSIM)
    |---dataset
        |---B100
        |---DIV2K
        |---Set5
        |---Set14
        |---Urban100
        |---div2h5.py  (transform training images to h5 format)
    |---test_result  (save the test result)
```

## Test and Train Models

### Train:

1. Run the div2h5.py file to transform training images to h5 format.
2. Change the parameter "train_state" to 1 for training in main.py file.
3. Set the model name in main.py (carn or carn_m)
4. Set the other suitable parameters in main.py file.
5. The trained model will be saved in checkpoint file.

### Test:

1. Change the parameter "test_state" to 1 for testing in main.py.
2. Set the model name in main.py (carn or carn_m)
3. Choose the wanted model (.pth file) in checkpoint file and set it in main.py file.
4. Set tested images path and test result saving path in main.py.
5. Set the other suitable parameters in main.py file.
6. Get the test result in test_result folder.

## *Citation:*
1. Ahn, N., Kang, B., &amp; Sohn, K.-A. (2018). Fast, accurate, and lightweight super-resolution with cascading residual network. Computer Vision – ECCV 2018, 256–272. https://doi.org/10.1007/978-3-030-01249-6_16
2. https://github.com/nmhkahn/CARN-pytorch
	

