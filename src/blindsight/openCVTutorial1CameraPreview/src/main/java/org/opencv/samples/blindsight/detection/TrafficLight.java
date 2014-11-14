package org.opencv.samples.blindsight.detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * Created by user-pc on 13/11/2014.
 */
public class TrafficLight {

    public TrafficLight(Mat img, Rect roi) {
        this.image = img;
        this.roi = roi;
    }

    public State detectLightState() {
        Mat upper = cropUpperHalf(image);
        Mat lower = cropLowerHalf(image);
        HalfTrafficLight upperHalf = heuristicColourDominance(upper, MIN_HUE_THRESHOLD, MAX_HUE_THRESHOLD);
        HalfTrafficLight lowerHalf = heuristicColourDominance(lower, MIN_HUE_THRESHOLD, MAX_HUE_THRESHOLD);

        boolean suspectedRed = upperHalf.color == HalfTrafficLight.PrimaryColor.RED && upperHalf.intensity > MATCH_THRESHOLD && (lowerHalf.intensity < OFF_THRESHOLD || lowerHalf.intensity * 6 < upperHalf.intensity);
        boolean suspectedGreen = lowerHalf.color == HalfTrafficLight.PrimaryColor.GREEN && lowerHalf.intensity > MATCH_THRESHOLD && (upperHalf.intensity < OFF_THRESHOLD || upperHalf.intensity * 6 < lowerHalf.intensity);

        if (suspectedGreen && !suspectedRed) {
            return State.GREEN;
        } else if (suspectedRed && !suspectedGreen) {
            return State.RED;
        } else {
            return State.NA;
        }
    }

    private Mat cropUpperHalf(Mat img) {
        return img.submat(0, img.height() / 2, 0, img.width());
    }

    private Mat cropLowerHalf(Mat img) {
        return img.submat(img.height() / 2, img.height(), 0, img.width());
    }

    private HalfTrafficLight heuristicColourDominance(Mat img, int minHueThreshold, int maxHueThreshold) {
        Mat histRed = new Mat();
        Mat histGreen = new Mat();
        Mat histBlue = new Mat();
        Imgproc.calcHist(Arrays.asList(img), new MatOfInt(0), new Mat(), histRed, new MatOfInt(256), new MatOfFloat(0, 256));
        Imgproc.calcHist(Arrays.asList(img), new MatOfInt(1), new Mat(), histGreen, new MatOfInt(256), new MatOfFloat(0, 256));
        Imgproc.calcHist(Arrays.asList(img), new MatOfInt(2), new Mat(), histBlue, new MatOfInt(256), new MatOfFloat(0, 256));

        int redSum = sumMat(histRed, minHueThreshold, maxHueThreshold);
        int greenSum = sumMat(histGreen, minHueThreshold, maxHueThreshold);
        int blueSum = sumMat(histBlue, minHueThreshold, maxHueThreshold);

        if (redSum > blueSum && redSum > greenSum) {
            return new HalfTrafficLight(HalfTrafficLight.PrimaryColor.RED, redSum - Math.max(greenSum, blueSum));
        } else if (greenSum > blueSum && greenSum > redSum) {
            return new HalfTrafficLight(HalfTrafficLight.PrimaryColor.GREEN, greenSum - Math.max(redSum, blueSum));
        } else {
            return new HalfTrafficLight(HalfTrafficLight.PrimaryColor.NA, 0);
        }
    }

    private int sumMat(Mat hist, int lowerThreshold, int upperThreshold) {
        int sum = 0;
        for (int i = lowerThreshold; i < upperThreshold; i++) {
            sum += hist.get(i, 0)[0];
        }
        return sum;
    }

    public Rect getRect() {
        return roi;
    }

    private Mat image;

    private Rect roi;

    public enum State {
        GREEN, RED, NA;
    }

    private static final int MIN_HUE_THRESHOLD = 100;

    private static final int MAX_HUE_THRESHOLD = 250;

    private static final int MATCH_THRESHOLD = 180;

    private static final int OFF_THRESHOLD = 40;
}
