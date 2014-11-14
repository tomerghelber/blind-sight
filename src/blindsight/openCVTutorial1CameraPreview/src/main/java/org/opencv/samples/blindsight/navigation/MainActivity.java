package org.opencv.samples.blindsight.navigation;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.opencv.samples.blindsight.R;
import org.opencv.samples.blindsight.detection.CameraActivity;
import org.opencv.samples.blindsight.handler.VocalHandler;

import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == geocoder) {
            geocoder = new Geocoder(getApplicationContext());
        }
        if (null == geoApiContext){
            geoApiContext = new GeoApiContext().setApiKey(getResources().getString(R.string.API_KEY));
        }

        Button detect = (Button) findViewById(R.id.detect_btn);

        handler = new VocalHandler(getApplicationContext());

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Getting a string of an address and returning it as a single address.
     *
     * @param addressString: the address to return.
     * @return {@link android.location.Address} of the addressString.
     */
    private Address stringToAddress(String addressString) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
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
        TextView textView = ((TextView) findViewById(R.id.myShit));
        String destinationText = ((EditText) findViewById(R.id.editTextDestination)).getText().toString();
        Log.e("my info:", destinationText);
        textView.setText("destination");
        String originText = ((EditText) findViewById(R.id.editTextOrigin)).getText().toString();
        Log.e("my info:", originText);
        textView.setText("origin");

        Address originAddress = stringToAddress(originText);
        textView.setText("origin address");
        if (null == originAddress || !originAddress.hasLongitude() || !originAddress.hasLatitude()) {
            return;
        }
        textView.setText("origin address fine");
        Address destinationAddress = stringToAddress(destinationText);
        textView.setText("destination address");
        if (null == destinationAddress || !destinationAddress.hasLongitude() || !destinationAddress.hasLatitude()) {
            return;
        }
        textView.setText("destination address fine");

        LatLng originalLatLng = new LatLng(originAddress.getLatitude(), originAddress.getLongitude());
        textView.setText("origin latlng");
        Log.e("my info:", originalLatLng.toString());
        LatLng destinationLatLng = new LatLng(destinationAddress.getLatitude(), destinationAddress.getLongitude());
        textView.setText("destination latlng");
        Log.e("my info:", destinationLatLng.toString());

        textView.setText("GeoApiContext");
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
            return;
        }
        if (0 == directionsRoutes.length) {
            textView.setText("no routes");
            return;
        }
        DirectionsRoute directionsRoute = directionsRoutes[0];

        textView.setText(directionsRoute.toString());
    }

    /*
    private GeocodingResult stringToGeocodingResult(String addressString) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(geocoderContext, addressString).await();
            if (0 == results.length) {
                return null;
            }
            return results[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // location calculation
    private Geocoder geocoder = null;
    private GeoApiContext geoApiContext = null;


    private VocalHandler handler;
}
