"""
Used to build self collected dataset.
"""

import os
import cv2

HR_path = ".\\HR\\"
HR_files = os.listdir(HR_path)

types = ["png", "jpg", "jpeg"]

targets = [2, 3, 4, 8]
threshold = 721


counter = 0
for file in HR_files:
	print("file:", file)
	file_parts = file.split(".")
	l = len(file_parts)
	suffix = file_parts[-1] if l > 0 else ""
	if not os.path.isdir(file) and suffix in types:
		new_filename = ".".join(file[:l-2])+"%05d."%counter+suffix
		os.rename(HR_path+file, HR_path+new_filename)
		counter += 1

		# resize:
		for target in targets:
			sub_path = ".\\x%d\\" % target
			if not os.path.exists(sub_path) or not os.path.isdir(sub_path):
				os.makedirs(sub_path)

			img = cv2.imread(HR_path+new_filename)
			h, w = img.shape[:2]
			if h >= w and h > threshold:
				new_h = threshold
				new_w = int(threshold / h * w)
				img = cv2.resize(img, (new_w, new_h))
			elif w >= h and w > threshold:
				new_w = threshold
				new_h = int(threshold / w * h)
				img = cv2.resize(img, (new_w, new_h))
			LR_filename = ".".join(file[:l-2])+"%05d_%d_LR.png"%(counter, target)
			HR_filename = ".".join(file[:l-2])+"%05d_%d_HR.png"%(counter, target)

			k = 1/target
			target_img = cv2.resize(img, None, fx=k, fy=k, interpolation=cv2.INTER_AREA)
			cv2.imwrite(sub_path+LR_filename, target_img)
			cv2.imwrite(sub_path+HR_filename, img)
			print("save file:", sub_path+LR_filename)



