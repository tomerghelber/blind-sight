package org.opencv.samples.blindsight.detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user-pc on 13/11/2014.
 */
public class TrafficLightsDetector {

    public TrafficLight getState(Mat img) {
        List<TrafficLight> trafs = getAllTrafficLights(img, TH1, TH2);
        if (trafs.size() == 0) {
            return null;
        }
        TrafficLight largest = null;
        for (TrafficLight tl : trafs) {
            TrafficLight.State state = tl.detectLightState();
            if ((state != TrafficLight.State.NA) &&
                    ((largest == null) ||
                    (largest.getRect().width < tl.getRect().width && largest.getRect().height < tl.getRect().height))) {
                largest = tl;
            }
        }
        return largest;
    }

    public List<TrafficLight> getAllTrafficLights(Mat img, double th1, double th2) {
        List<Rect> rects = filterRects(getEveryRect(img, th1, th2));
        List<TrafficLight> lights = new ArrayList<TrafficLight>();
        for (Rect rect : rects) {
            lights.add(new TrafficLight(img, rect));
        }
        return lights;
    }

    public List<MatOfPoint> getEveryRect(Mat img, double th1, double th2) {
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, th1, th2, 3, true);

        Mat structElm = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3.0, 3.0));
        Mat dilated = new Mat();
        Imgproc.dilate(edges, dilated, structElm);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilated, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;
    }

    private List<Rect> filterRects(List<MatOfPoint> contours) {
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

    private static final int TH1 = 30;
    private static final int TH2 = 90;

}
