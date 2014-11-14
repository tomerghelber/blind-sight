package org.opencv.samples.blindsight.detection;

/**
 * Created by Shira-PC on 14/11/2014.
 */
public class HalfTrafficLight {

    public HalfTrafficLight(PrimaryColor color, int intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public enum PrimaryColor {
        RED, GREEN, NA;
    }

    public int intensity;

    public PrimaryColor color;

}
