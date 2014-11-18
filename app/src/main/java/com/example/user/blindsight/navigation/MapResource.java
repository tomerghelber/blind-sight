package com.example.user.blindsight.navigation;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class MapResource extends ActivityResource {

    public MapResource(Activity activity, MapView map) {
        super(activity);
        this.mapView = map;
    }

    public GoogleMap googleMap;

    public void onCreateView(Bundle savedInstanceState) {
// Gets the MapView from the XML layout and creates it
        mapView.onCreate(savedInstanceState);
// Gets to GoogleMap from the MapView and does initialization stuff
        googleMap = mapView.getMap();
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(true);

// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(activity);
// Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        googleMap.animateCamera(cameraUpdate);
    }


    private MapView mapView;
}
