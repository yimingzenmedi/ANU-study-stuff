import os
from dataset_RGB import DataLoaderTrain, DataLoaderVal, DataLoaderTest


def get_training_data(rgb_dir, img_options, pic_index=None, group=7):
    assert os.path.exists(rgb_dir)
    return DataLoaderTrain(rgb_dir, pic_index=pic_index, group=group, img_options=img_options)


def get_validation_data(rgb_dir, img_options, pic_index=None, group=7):
    assert os.path.exists(rgb_dir)
    return DataLoaderVal(rgb_dir, pic_index=pic_index, group=group, img_options=img_options)


def get_test_data(rgb_dir, img_options):
    assert os.path.exists(rgb_dir)
    return DataLoaderTest(rgb_dir, img_options)
