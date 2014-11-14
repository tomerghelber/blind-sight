import cv2
from detect_light_state import detect_light_state, show_image
import numpy as np
import random
import sys

def get_rects(img, th1=40, th2=120):
    gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    edges = cv2.Canny(gray,th1,th2,apertureSize = 3, L2gradient=True)

    mat = cv2.getStructuringElement(cv2.MORPH_RECT, (3,3))
    dilated = cv2.dilate(edges, mat)


    contours, hierarchy = cv2.findContours(dilated,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)

    for cnt in contours:
        x,y,w,h = cv2.boundingRect(cnt)
        if w < 15 or h < 15:
            continue
        if h > 3*w or h < 1.5 * w:
            continue
        yield (x,y),(x+w,y+h)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "Usage: %s image [image...]" % sys.argv[0]
        sys.exit(1)

    for f in sys.argv[1:]:
        img = cv2.imread(f)
        cimg = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        for ul, br in get_rects(img):
            i = cimg[ul[1]:br[1], ul[0]:br[0]]
            res = detect_light_state(i)
            if res == 'green':
                col = (0, 255, 0)
            elif res == 'red':
                col = (0, 0, 255)
            else:
                col = (100, 100, 100)
            cv2.rectangle(img, ul, br, col, 2)
        show_image(img)
