package org.opencv.samples.blindsight;

import org.opencv.core.Mat;

/**
 * Created by user-pc on 13/11/2014.
 */
public class TrafficLight {

    public TrafficLight(Mat img) {
        this.image = img;
    }

    public State detectLightState() {
        Mat upper = cropUpperHalf(image);
        Mat lower = cropLowerHalf(image);
        double upperDiff = heuristicColourDominance(upper, HUE_THRESHOLD);
        double lowerDiff = heuristicColourDominance(lower, HUE_THRESHOLD);

        boolean suspectedRed = (Math.abs(upperDiff) > STATURATION_THRESHOLD && upperDiff < 0);
        boolean suspectedGreen = (Math.abs(lowerDiff) > STATURATION_THRESHOLD && lowerDiff > 0);

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

    private double heuristicColourDominance(Mat img, int hueThreshold) {
        return 0.0;
    }

    private Mat image;

    public enum State {
        GREEN, RED, NA;
    }

    private static final int HUE_THRESHOLD = 230;

    private static final int STATURATION_THRESHOLD = 100;
}
