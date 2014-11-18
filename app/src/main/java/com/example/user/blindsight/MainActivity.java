package com.example.user.blindsight;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.maps.model.DirectionsRoute;


public class MainActivity extends UpdateableActivity  {

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
            navigation = new Navigation(this);
        }
        if (roadCreator == null) {
            roadCreator = new RoadCreator(this, userLocation);
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
        navigation.stopVibrating();
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
        Position currentPosition = userLocation.getCurrentPosition();
        if (!currentPosition.equals(new Position(0,0))) {
            try {
                navigation.walk(currentPosition, userRotation.getAngle());
            } catch (Exception e) {
                print(e.getClass().getName() + ": " + e.getMessage());
            }
            //text.setText("x :" + userLocation.getLongitude());
            //text.append("y :" + userLocation.getLatitude());
            //text.append("rotation :" + userRotation.getAngle());
        }
    }

    /* --- Buttons --- */

    public void findPath(View v) {
        roadCreator.findPath(v);
        DirectionsRoute route = roadCreator.getRoute();
        if (route != null) {
            navigation.setWay(route);
        } else {
            Toast.makeText(getApplicationContext(), "can't connect to the internet", Toast.LENGTH_LONG).show();
        }
    }

    public void nextStep(View v) {
        navigation.nextStep();
    }

    public void findTrafficLight(View v) {
    }

    public void startDemo(View v) {
        navigation = new Navigation(this, true);
        userLocation.currentPosition = new Position(34.8187833, 32.14632264);
    }


    /* --- Rotation --- */

    private Navigation navigation = null;
    private UserRotation userRotation = null;
    private UserLocation userLocation = null;
    private RoadCreator roadCreator = null;

}
