package com.example.andriod.restaurant_roulette;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class getRestaurants extends IntentService  {

    String LOG_TAG = this.getClass().toString();
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_GET_RESTAURANTS = "com.example.android.restaurant_roulette.action.GET_RESTAURANTS";
    public static final String ACTION_BAZ = "com.example.android.restaurant_roulette.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.android.restaurant_roulette.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.android.restaurant_roulette.extra.PARAM2";


    public getRestaurants() {
        super("getRestaurants");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("SENT", intent.getData().toString());

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_RESTAURANTS.equals(action)) {
                Uri uri = intent.getData();
                try {
                    JSONObject placesJSON = queryGoogleAPI(uri);
                    ArrayList<ArrayList<String>> nameID = getRestaurantDataFromJson(placesJSON);
                    ArrayList<String> names = nameID.get(0);
                    ArrayList<String> ids = nameID.get(1);
                    int myPlaceIndex = (int) Math.floor(Math.random() * names.size());
                    String myPlace = names.get(myPlaceIndex);
                    //return myPlace;
                    //comment

                } catch (JSONException e) {

                }
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
        //
    // Use GeoDataApi to get info about place based on ID
    // use PlaceDetectionApi to get device location
    //

    private JSONObject queryGoogleAPI(Uri uri) {
        String restaurantJsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            restaurantJsonStr = null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }
            restaurantJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Restaurant JSON String" + restaurantJsonStr);

        }catch (IOException e){
            Log.d(LOG_TAG, "Bad uri: " + uri + "\n" + e);

        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("getRestaurants", "Error closing stream", e);
                }
            }
        }

        //Process forcastJSONString
        //Process forcastJSONString
        try {
            JSONObject json = new JSONObject(restaurantJsonStr);
            return json;
        }catch(JSONException e){

            Log.e(LOG_TAG, e.getMessage(),e);
            e.printStackTrace();
        }


        return null;
    }

    public ArrayList<ArrayList<String>> getRestaurantDataFromJson(JSONObject placesJSON) throws JSONException{

        JSONArray results = placesJSON.getJSONArray("results");

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        for (int i=0;i<results.length();i++) {
            String name = (String) results.getJSONObject(i).get("name");
            String id = (String) results.getJSONObject(i).get("id");
            names.add(name);
            ids.add(id);

        }

        ArrayList<ArrayList<String>> nameID = new ArrayList<ArrayList<String>>();
        nameID.add(names);
        nameID.add(ids);

        return nameID;
    }
}
