package com.example.user.blindsight;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by michelle on 13/11/2014.
 */
public class Navigation extends ActivityResource {

    public Navigation(Activity activity) {
        super(activity);
        this.way = new LinkedList<Position>();
        this.passed = new LinkedList<Position>();
        this.vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        this.attractions = new LinkedList<Position>();
    }

    public Navigation(Activity activity, Queue<Position> positions) {
        this(activity);
        this.way = positions;
    }

    public Navigation(UpdateableActivity activity, boolean stam) {
        this(activity);
        this.updateable = activity;
        if (!stam) {
            this.way.add(new Position(32.11324305, 34.81849653));
            this.way.add(new Position(32.11289161, 34.81800239));
            this.way.add(new Position(32.11309088, 34.81792111));
            this.way.add(new Position(32.11313755, 34.81774711));
            this.way.add(new Position(32.11340843, 34.81778721));
        } else {
            this.way.add(new Position(30.81829653, 40.11290305));
            this.way.add(new Position(50.81800239, 32.11289161));
            this.way.add(new Position(45.81792111, 45.11309088));
            this.way.add(new Position(45.81774711, 90.11313755));
            this.way.add(new Position(60.81778721, 12.11340843));
        }
    }

    public void walk(Position position, float angle) {
        Position next = way.peek();
        if (next == null) {
            updateable.clear();
            stopVibrating();
            return;
        }
        float[] distanceAndAngle = position.comparePositions(next);


        updateable.clear();
        updateable.print(distanceAndAngle[0] + " --- " + getRightAngel(distanceAndAngle[1]) + "\n");
        updateable.print("next:\n" + next.longitude + " , " + next.latitude + "\n");
        updateable.print("current:\n" + position.longitude + " , " + position.latitude + "\n" + angle);


        if (distanceAndAngle.length >= 1) {
            //distance
            if (distanceAndAngle[0] < Position.MIN_DISTANCE) {
                nextStep();
                updateable.print("next position");
                walk(position, angle);
            } else {
                if (distanceAndAngle.length >= 2) {
                    //angle
                    if (Math.abs(getRightAngel(distanceAndAngle[1]) - angle) > Position.MIN_ANGLE) {
                        vibrate();
                    } else {
                        stopVibrating();
                    }
                }
            }

        }
    }

    public void nextStep() {
        Position position = way.poll();
        if (position != null) {
            passed.add(position);
        }
    }

    public void setAttractions(DirectionsRoute directionsRoute) {
        DirectionsStep[] directionsSteps = directionsRoute.legs[0].steps;
        for (DirectionsStep directionsStep : directionsSteps) {
            attractions.add(new Position(directionsStep.startLocation.lng, directionsStep.startLocation.lat));
        }
    }

    public void setWay(DirectionsRoute directionsRoute) {
        way = new LinkedList<Position>();
        DirectionsStep[] directionsSteps = directionsRoute.legs[0].steps;
        for (DirectionsStep directionsStep : directionsSteps) {
            way.add(new Position(directionsStep.startLocation.lng, directionsStep.startLocation.lat));
        }
    }

    //transfer from -180 - 180 Angel to 0 - 360 Angel
    private float getRightAngel(float angel) {
        if (angel < 0) {
            return 180 - angel;
        }
        return angel;
    }

    public void walk(double x, double y, float angle) {
        walk(new Position(x, y), angle);
    }

    public void onDestroy() {
        stopVibrating();
    }

    public void vibrate() {
        updateable.print("vibrate");
        if (vibratorFinish < System.currentTimeMillis()) {
            int runningTime = 50000;
            vibratorFinish = System.currentTimeMillis() + runningTime;
            vibrator.vibrate(runningTime);
        }
    }

    public void stopVibrating() {
        updateable.print("quite");
        vibratorFinish = System.currentTimeMillis();
        vibrator.cancel();
    }

    public boolean isNearAttraction(Position currentPosition) {
        for (Position attraction : attractions) {
            if (currentPosition.isNear(attraction)) {
                return true;
            }
        }
        return false;
    }

    private Updateable updateable;
    private Vibrator vibrator;
    private Queue<Position> way;
    private Queue<Position> passed;
    private List<Position> attractions;
    private long vibratorFinish = 0;
}
