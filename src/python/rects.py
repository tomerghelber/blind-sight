import cv2
import numpy as np
import random
import sys

def show_image(i):
    cv2.imshow('image',i)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

def get_rects(img):
    gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    edges = cv2.Canny(gray,50,150,apertureSize = 3)
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
    if len(sys.argv) == 2:
        img = cv2.imread(sys.argv[1])
    else:
        print "Usage: %s image" % sys.argv[0]
        sys.exit(1)

    for ul, br in get_rects(img):
        cv2.rectangle(img,ul,br,(random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)),2)
    show_image(img)
