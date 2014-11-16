package com.example.user.blindsight;

import android.location.Location;

import com.google.maps.model.LatLng;

/**
 * Created by michelle on 14/11/2014.
 */
public class Position {

    public Position(double x, double y) {
        this.longitude = x;
        this.latitude = y;
    }

    public float[] comparePositions(Position pos) {
        float[] distanceAndAngle = new float[3];
        Location.distanceBetween(this.latitude, this.longitude, pos.latitude, pos.longitude, distanceAndAngle);
        return distanceAndAngle;
    }

    public float getDistance(Position pos) {
        float[] distanceAndAngle = this.comparePositions(pos);
        return distanceAndAngle[0];
    }

    public boolean isNear(Position pos) {
        if (this.getDistance(pos) < MIN_DISTANCE) {
            return true;
        } return false;
    }

    public float getAngle(Position pos) {
        float[] distanceAndAngle = this.comparePositions(pos);
        return distanceAndAngle[1];
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    public double longitude;
    public double latitude;

    public static int MIN_DISTANCE = 20;
    public static float MIN_ANGLE = 30;
}
