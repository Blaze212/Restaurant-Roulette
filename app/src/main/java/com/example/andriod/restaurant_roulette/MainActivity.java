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
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

// API Key - AIzaSyC5V0sUyvMudH7dHu57uWICoJ3hjt7dQTc

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {
    Location location;
    String locationStr = "fort worth ";
    String APIkey = "AIzaSyC5V0sUyvMudH7dHu57uWICoJ3hjt7dQTc";
    String LOG_TAG = this.getClass().toString();

    public MyResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Start ");
        //getLocation();
        final Button button = (Button) findViewById(R.id.btn_search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FoodPreferences foods = new FoodPreferences();
                String searchTerm = foods.getCheckedSearchTerms();
                search(v, searchTerm);
                Log.d(LOG_TAG,"Search clicked");
            }
        });

        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            //startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        if (id == R.id.food_categories) {
            startActivity(new Intent(this,FoodPreferences.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void search(View view, String searchTerm){
        //launchMap(searchTerm);
        Log.d(LOG_TAG, "Searching: ");
        try {
            Log.d(LOG_TAG, "try Searching: " + searchTerm);
            Uri uri = buildUri(searchTerm, "location", "maxprice", "radius");
            Log.d(LOG_TAG, "URI " + uri.toString() );
            Intent intent  = new Intent(this, getRestaurants.class);
            intent.setData(uri);
            intent.setAction("com.example.android.restaurant_roulette.action.GET_RESTAURANTS");
            intent.putExtra("receiverTag", mReceiver);
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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if(resultCode == 0){
            String restaurant = resultData.getString("restaurant");
            TextView t = (TextView) findViewById(R.id.txt_picked_restaurant);
            t.setText(restaurant);
        }

        Log.d("Reciever","received result from Service="+resultData.getString("ServiceTag"));

    }




}
