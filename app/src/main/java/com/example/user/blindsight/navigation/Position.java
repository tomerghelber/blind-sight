package com.example.user.blindsight.navigation;

import android.location.Location;

import com.google.maps.model.LatLng;

public class Position {

    public Position(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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
        return this.getDistance(pos) < MIN_DISTANCE;
    }

    public float getAngle(Position to) {
        double dx = to.longitude - this.longitude;
        double dy = to.latitude - this.latitude;
        double result;
        if (dx > 0) {
            result = Math.PI * 0.5 - Math.atan(dy / dx);
        } else {
            if (dx < 0) {
                result = Math.PI * 1.5 - Math.atan(dy / dx);
            } else {
                if (dy > 0) {
                    result = 0;
                } else {
                    result = Math.PI;
                }
            }
        }
        return (float) Math.toDegrees(result);
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position) || obj == null){
            return false;
        }
        Position pos = (Position) obj;
        if (pos.getLatitude() == latitude && pos.getLongitude() == longitude) {
            return true;
        }return false;
    }

    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }

    public double longitude;
    public double latitude;

    public static int MIN_DISTANCE = 20;
    public static float MIN_ANGLE = 30;
}
