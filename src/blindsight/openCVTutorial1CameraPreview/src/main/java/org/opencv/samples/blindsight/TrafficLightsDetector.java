package org.opencv.samples.blindsight;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by user-pc on 13/11/2014.
 */
public class TrafficLightsDetector {

    public List<TrafficLight> getAllTrafficLights(Mat img) {
        List<Rect> rects = getEveryRect(img);
        List<TrafficLight> lights = new ArrayList<TrafficLight>();
        for (Rect rect : rects) {
            Mat roi = new Mat(img, rect);
            lights.add(new TrafficLight(img));
        }
        return lights;
    }

    public List<Rect> getEveryRect(Mat img) {
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50.0, 150.0, 3.0);

        Mat structElm = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3.0, 3.0));
        Mat dilated = new Mat();
        Imgproc.dilate(edges, dilated, structElm);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilated, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Rect> result = new ArrayList<Rect>();
        for (MatOfPoint cnt : contours) {
            Rect rect = Imgproc.boundingRect(cnt);
            if (isValidRect(rect)) {
                result.add(rect);
            }
        }
        return result;
    }

    private boolean isValidRect(Rect rect) {
        if (rect.height < MIN_VALID_RECT_DIMENTIONS || rect.width < MIN_VALID_RECT_DIMENTIONS) {
            return false;
        } else if (rect.height > 3 * rect.width || rect.height < 1.5 * rect.width) {
            return false;
        } else {
            return true;
        }
    }

    private static final int MIN_VALID_RECT_DIMENTIONS = 15;

}
