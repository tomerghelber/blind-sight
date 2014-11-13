import cv2
import os
import math
import numpy as np
import matplotlib
from matplotlib import pyplot as plt


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


def _heuristic_dominant_color(img, threshold=230):
    counter = 0
    green_histogram = cv2.calcHist([img],[1],None,[256],[0,256])
    red_histogram = cv2.calcHist([img],[2],None,[256],[0,256])
    for i in xrange(threshold, len(red_histogram)):
        counter += green_histogram[i][0] - red_histogram[i][0]
    if counter > 0:
        state = 'green'
    else:
        state = 'red'
    return state, abs(counter)

def detect_light_state(img, threshold=100):
    upper_img, lower_img = _split_image(img)
    upper_state, upper_diff = _heuristic_dominant_color(upper_img)
    lower_state, lower_diff = _heuristic_dominant_color(lower_img)

    possibly_red = (upper_state == 'red' and upper_diff > threshold)
    possibly_green = (lower_state =='green' and lower_diff > threshold)

    if possibly_green and not possibly_red:
        return 'green'
    else if possibly_red and not possibly_green:
        return 'red'
    else:
        return 'NAN'
    
