import cv2
import os
import math
import numpy as np
import matplotlib
from matplotlib import pyplot as plt


def show_image(i):
    cv2.imshow('image',i)
    k = cv2.waitKey(0) & 0xff
    if k == ord('h'):
        _show_hist_curve(i)
    elif k == 27:
        os._exit(2)
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
    lower_green = np.array([50,50,50])
    upper_green = np.array([80,255,255])
    green_mask = cv2.inRange(img, lower_green, upper_green)
    green = sum(sum(i) for i in green_mask) / 255

    lower_red = np.array([0,50,50])
    upper_red = np.array([7,255,255])
    red_mask = cv2.inRange(img, lower_red, upper_red)
    red = sum(sum(i) for i in red_mask) / 255.

    lower_red = np.array([155,50,50])
    upper_red = np.array([180,255,255])
    red_mask = cv2.inRange(img, lower_red, upper_red)
    red += sum(sum(i) for i in red_mask) / 255.

    lower_blue = np.array([90, 50, 50])
    upper_blue = np.array([140, 255, 255])
    blue_mask = cv2.inRange(img, lower_blue, upper_blue)
    blue = sum(sum(i) for i in blue_mask) / 255.

    total = red + green + blue
    if total == 0:
        return 'nan', 0, 0

    red /= total
    green /= total
    if red > green:
        return 'red', red, red * total
    return 'green', green, green * total

def detect_light_state(img, match_threshold=0.6, off_threshold=0.5):
    upper_img, lower_img = _split_image(img)
    upper_state, upper_diff, upper_t = _heuristic_dominant_color(upper_img)
    lower_state, lower_diff, lower_t = _heuristic_dominant_color(lower_img)

    possibly_red = (upper_state == 'red' and upper_diff > match_threshold and upper_t > 30 and lower_t * 2 < upper_t and (lower_t < 15 or lower_diff < off_threshold))
    possibly_green = (lower_state =='green' and lower_diff > match_threshold and lower_t > 30 and upper_t * 2 < lower_t and (upper_t < 15 or upper_diff < off_threshold))

    if possibly_green and not possibly_red:
        return 'green'
    elif possibly_red and not possibly_green:

        print upper_state, upper_diff, upper_t
        print lower_state, lower_diff, lower_t

        return 'red'
    else:
        return 'NAN'

