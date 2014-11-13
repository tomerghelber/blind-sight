
# coding: utf-8

# In[1]:

import cv2
import os
import math
import numpy as np
import matplotlib
from matplotlib import pyplot as plt


# In[2]:

def split_image(img):
    height, width, _ = img.shape
    return img[0 : (height / 2), 0 : width], img[(height / 2) : height, 0 : width]


# In[3]:

DOWNLOAD_PATH = r'C:\\Users\\Shira-PC\\Downloads'
bins = np.arange(256).reshape(256,1)
img_g = cv2.imread(os.path.join(DOWNLOAD_PATH, 'tlight_green.jpg'))
img_r = cv2.imread(os.path.join(DOWNLOAD_PATH, 'tlight_red.jpg'))


# In[4]:

upper_g, lower_g = split_image(img_g)
upper_r, lower_r = split_image(img_r)


# In[5]:

def show_hist_curve(img):
    color = ('b','g','r')
    for i,col in enumerate(color):
        histr = cv2.calcHist([img],[i],None,[256],[0,256])
        plt.plot(histr,color = col)
        plt.xlim([0,256])
    plt.show()


# In[6]:

def heuristic_dominant_color(img):
    counter = 0
    green_histogram = cv2.calcHist([img],[1],None,[256],[0,256])
    red_histogram = cv2.calcHist([img],[2],None,[256],[0,256])
    for i in xrange(230, len(red_histogram)):
        counter += green_histogram[i][0] - red_histogram[i][0]
    return counter


# In[10]:

print heuristic_dominant_color(upper_g)


# In[44]:

show_hist_curve(lower_g)


# In[ ]:



