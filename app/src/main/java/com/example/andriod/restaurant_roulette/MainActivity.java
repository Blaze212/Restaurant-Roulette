package com.example.andriod.restaurant_roulette;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.IOException;

// API Key - AIzaSyC5V0sUyvMudH7dHu57uWICoJ3hjt7dQTc

public class MainActivity extends AppCompatActivity {
    Location location;
    String locationStr = "fort worth ";
    String APIkey = "AIzaSyC5V0sUyvMudH7dHu57uWICoJ3hjt7dQTc";
    String LOG_TAG = this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Start ");
        //getLocation();
        final Button button = (Button) findViewById(R.id.btn_search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String searchTerm = getCheckedSearchTerms();
                search(v, searchTerm);
                Log.d(LOG_TAG,"Search clicked");
            }
        });
    }

    public String getCheckedSearchTerms(){
        String searchTerm = "";
        String notChecked = "";
        CheckBox[] checkBoxes = new CheckBox[2];

        CheckBox chkFastFood = (CheckBox)findViewById(R.id.chk_fastfood);
        CheckBox chkFancyRest= (CheckBox)findViewById(R.id.chk_fancy);
        checkBoxes[0] = chkFancyRest;
        checkBoxes[1] = chkFastFood;

        for(int i = 0; i<checkBoxes.length;i++){
            if(checkBoxes[i].isChecked()){
                searchTerm += checkBoxes[i].getText();
            }
            if(!checkBoxes[i].isChecked()){
                notChecked += " -" + checkBoxes[i].getText();
            }
        }
        searchTerm += notChecked;
        Log.d("SearchTerm",searchTerm);
        return searchTerm;
    }

    public void search(View view, String searchTerm){
        //launchMap(searchTerm);
        Log.d(LOG_TAG, "Searching: ");
        try {
            Log.d(LOG_TAG, "try Searching: ");
            Uri uri = buildUri(searchTerm, "location", "maxprice", "radius");
            Log.d(LOG_TAG, "URI " + uri.toString() );
            Intent intent  = new Intent(this, getRestaurants.class);
            intent.setData(uri);
            intent.setAction("com.example.android.restaurant_roulette.action.GET_RESTAURANTS");
            startService(intent);
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Failed url/conn: " + e.getMessage());
        }

    }
    public void launchMap(String searchTerm){
        //launhes googlemaps with selected location
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(String.format("geo:0,0")).buildUpon()
                .appendQueryParameter("q",locationStr + searchTerm)
                .build();
        intent.setData(uri);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        else{
            Log.d("TAG","Map failed to launch" + location+ " " );
        }
    }
    public void getLocation(){
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            locationManager.removeUpdates(locationListener);
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        }
    }

    public Uri buildUri(String searchTerms, String location, String maxprice, String radius) throws IOException{
        location = "32.8045412,-97.1355528";
        maxprice = "4";
        radius = "20000";
        Uri.Builder builder = new Uri.Builder();


        Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json?").buildUpon()
                .appendQueryParameter("location", location)
                .appendQueryParameter("keyword", searchTerms)
                .appendQueryParameter("maxprice",maxprice)
                .appendQueryParameter("radius", radius)
                .appendQueryParameter("key", APIkey)
                .build();
        Log.d("Query Url", uri.toString());

        //URL url = new URL(uri.toString());


        return uri;
    }


}
