package com.example.user.blindsight.detection;

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
