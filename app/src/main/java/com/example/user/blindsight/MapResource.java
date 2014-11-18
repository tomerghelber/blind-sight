package com.example.user.blindsight;

import android.app.Activity;
import com.google.android.gms.maps.MapView;

public class MapResource extends ActivityResource {
    public MapResource(Activity activity) {
        super(activity);
    }

    public void onCreate(MapView map) {
    }



    private MapView map;
}
