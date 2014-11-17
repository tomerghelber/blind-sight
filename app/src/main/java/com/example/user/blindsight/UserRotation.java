package com.example.user.blindsight;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class UserRotation extends UpdateableActivityResource implements SensorEventListener {

    public UserRotation(UpdateableActivity activity) {
        super(activity);
    }

    public UserRotation(Activity activity, Updateable updateable) {
        super(activity, updateable);
    }

    private SensorManager mSensorManager;
    private Sensor mOrientation;

    public void onCreate() {
        if (mSensorManager == null || mOrientation == null) {
            mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
            mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    public void onResume() {
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        angle = event.values[0];
        updateable.update();
    }

    public float getAngle() { return angle; }

    private float angle = 0;
}
