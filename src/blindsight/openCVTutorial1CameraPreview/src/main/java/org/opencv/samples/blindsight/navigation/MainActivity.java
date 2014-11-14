package org.opencv.samples.blindsight.navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.opencv.samples.blindsight.R;


public class MainActivity extends UpdateableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (userLocation == null) {
            userLocation = new UserLocation(this);
            userLocation.createLocationProviders();
        }
        if (userRotation == null) {
            userRotation = new UserRotation(this);
            userRotation.onCreate();
        }
        if (navigation == null) {
            navigation = new Navigation(this, true);
        }
        if (roadCreator == null) {
            roadCreator = new RoadCreator(this);
            roadCreator.onCreate();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        navigation.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userRotation.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRotation.onPause();
    }

    public void print(String str) {
        TextView text = (TextView) findViewById(R.id.edit_message);
        text.append(str);
        System.out.println(str);
    }

    public void clear() {
        TextView text = (TextView) findViewById(R.id.edit_message);
        text.setText("");
    }

    public void update() {
        double x = userLocation.getX();
        double y = userLocation.getY();
        if (x != 0 || y != 0) {
            try {
                navigation.walk(x, y, userRotation.getAngle());
            } catch (Exception e) {
                print(e.getClass().getName() + ": " + e.getMessage());
            }
            //text.setText("x :" + userLocation.getX());
            //text.append("y :" + userLocation.getY());
            //text.append("rotation :" + userRotation.getAngle());
        }
    }

    /* --- Buttons --- */

    public void findPath(View v) {
        roadCreator.findPath(v);
        navigation.setWay(roadCreator.getRoute());
    }

    public void nextStep(View v) {
        navigation.nextStep();
    }

    public void findTrafficLight(View v) {
    }


    /* --- Rotation --- */

    private Navigation navigation = null;
    private UserRotation userRotation = null;
    private UserLocation userLocation = null;
    private RoadCreator roadCreator = null;

}
