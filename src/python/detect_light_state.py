import cv2
import os
import math
import numpy as np
import matplotlib
from matplotlib import pyplot as plt


def show_image(i):
    cv2.imshow('image',i)
    k = cv2.waitKey(0)
    if k == 1048680:
        _show_hist_curve(i)
    cv2.destroyAllWindows()

def _split_image(img):
    height, width, _ = img.shape
    return img[0 : (height / 2), 0 : width], img[(height / 2) : height, 0 : width]

def _show_hist_curve(img):
    color = ('b','g','r')
    for i,col in enumerate(color):
        histr = cv2.calcHist([img],[i],None,[256],[0,256])
        plt.plot(histr,color = col)
        plt.xlim([0,256])
    plt.show()

def _heuristic_dominant_color(img, lower_thresh=100, upper_thresh=250):
    blue_histogram = cv2.calcHist([img],[0],None,[256],[0,256])
    green_histogram = cv2.calcHist([img],[1],None,[256],[0,256])
    red_histogram = cv2.calcHist([img],[2],None,[256],[0,256])

    blue = sum(p[0] for p in blue_histogram[lower_thresh:upper_thresh])
    green = sum(p[0] for p in green_histogram[lower_thresh:upper_thresh])
    red = sum(p[0] for p in red_histogram[lower_thresh:upper_thresh])

    if red > green and red > blue:
        return 'red', (red - max(green, blue)) * 1000 / math.sqrt(img.size)
    if green > red and green > blue:
        return 'green', (green - max(red, blue)) * 1000 / math.sqrt(img.size)

    return 'blue', 0

def detect_light_state(img, match_threshold=180, off_threshold=40):
    upper_img, lower_img = _split_image(img)
    upper_state, upper_diff = _heuristic_dominant_color(upper_img)
    lower_state, lower_diff = _heuristic_dominant_color(lower_img)

    possibly_red = (upper_state == 'red' and upper_diff > match_threshold and lower_diff < off_threshold)
    possibly_green = (lower_state =='green' and lower_diff > match_threshold and upper_diff < off_threshold)

    print upper_state, upper_diff
    print lower_state, lower_diff

    if possibly_green and not possibly_red:
        return 'green'
    elif possibly_red and not possibly_green:
        return 'red'
    else:
        return 'NAN'

