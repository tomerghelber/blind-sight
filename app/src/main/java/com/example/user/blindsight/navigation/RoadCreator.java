package com.example.user.blindsight.navigation;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blindsight.R;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;


public class RoadCreator extends ActivityResource {

    public RoadCreator(Activity activity, UserLocation userLocation) {
        super(activity);
        this.userLocation = userLocation;
    }

    public void onCreate() {
        if (null == geocoder) {
            geocoder = new Geocoder(activity.getApplicationContext());
        }
        if (null == geoApiContext){
            geoApiContext = new GeoApiContext().setApiKey(activity.getResources().getString(R.string.API_KEY));
        }
    }

    /**
     * Getting a string of an address and returning it as a single address.
     *
     * @param addressString: the address to return.
     * @return {@link Address} of the addressString.
     */
    public Address stringToAddress(String addressString) {
        Geocoder geocoder = new Geocoder(activity.getApplicationContext());
        try {
            List<Address> addressesList = geocoder.getFromLocationName(addressString, 1);
            if(addressesList.isEmpty()) {
                return null;
            }
            Address address = addressesList.get(0);
            if (address.hasLatitude() && address.hasLongitude()) {
                return address;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param v: a view
     */
    public void findPath(View v) {
        TextView textView = ((TextView) activity.findViewById(R.id.myShit));
        String destinationText = ((EditText) activity.findViewById(R.id.editTextDestination)).getText().toString();
        Log.e("my info:", destinationText);
        textView.setText("destination");
        String originText = ((EditText) activity.findViewById(R.id.editTextOrigin)).getText().toString();
        Log.e("my info:", originText);
        Address destinationAddress = stringToAddress(destinationText);
        textView.setText("destination address");
        if (null == destinationAddress || !destinationAddress.hasLongitude() || !destinationAddress.hasLatitude()) {
            Toast.makeText(activity.getApplicationContext(), "write a destination", Toast.LENGTH_LONG).show();
            return;
        }
        textView.setText("destination address fine");
        textView.setText("origin");
        Address originAddress = stringToAddress(originText);
        textView.setText("origin address");
        LatLng originalLatLng;
        if (null == originAddress || !originAddress.hasLongitude() || !originAddress.hasLatitude()) {
            if (userLocation.getLatitude() == 0 && userLocation.getLongitude() == 0) {
                Toast.makeText(activity.getApplicationContext(),
                        "No GPS signal. your current location could not be found", Toast.LENGTH_LONG).show();
                return;
            }
            originalLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        } else {
            originalLatLng = new LatLng(originAddress.getLatitude(), originAddress.getLongitude());
        }
        textView.setText("origin address fine");
        textView.setText("origin latlng");
        Log.e("my info:", originalLatLng.toString());
        LatLng destinationLatLng = new LatLng(destinationAddress.getLatitude(), destinationAddress.getLongitude());
        textView.setText("destination latlng");
        Log.e("my info:", destinationLatLng.toString());
        textView.setText("GeoApiContext");
        try {
            this.directionsRoute = getRoute(originalLatLng, destinationLatLng);
            textView.setText(directionsRoute.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(),
                    "can't connect to the internet", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * Getting route from original to destination.
     *
     * @param originalLatLng
     * @param destinationLatLng
     * @return A route
     * @throws Exception
     */
    public DirectionsRoute getRoute(LatLng originalLatLng, LatLng destinationLatLng)
            throws Exception{

        TextView textView = ((TextView) activity.findViewById(R.id.myShit));
        DirectionsApiRequest directionsApiRequest = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.WALKING)
                .alternatives(false)
                .optimizeWaypoints(true)
                .origin(originalLatLng)
                .destination(destinationLatLng);
        textView.setText("directionsApiRequest");
        DirectionsRoute[] directionsRoutes;
        try {
            directionsRoutes = directionsApiRequest.await();
            textView.setText("directionsApiRequest");
        } catch (Exception e) {
            textView.setText(e.toString());
            e.printStackTrace();
            throw e;
        }
        if (0 == directionsRoutes.length) {
            textView.setText("no routes");
            throw new Exception("null");
        }
        return directionsRoutes[0];
    }

    public DirectionsRoute getRoute() {
        return directionsRoute;
    }

    // location calculation
    private Geocoder geocoder = null;
    private GeoApiContext geoApiContext = null;
    private DirectionsRoute directionsRoute = null;
    private UserLocation userLocation;

}
