package com.example.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //CONSTANTS

    //Base URL used for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    //The parameter name for the API Key
    public final static String API_KEY_PARAM = "api_key";

    //Tag we will be using for this activity
    public final static String MAIN_ACTIVITY_TAG = "MainActivity";

    //INSTANCE FIELDS

    //Client we'll be using to communicate with the API
    AsyncHttpClient client;

        //TWO OF THE THREE PARTS OF OUR IMAGE URLS BELOW

    //Base Url for loading images
    String imageBaseUrl;

    //The Poster size to use when fetching images
    String posterSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the client
        client = new AsyncHttpClient();

        //Get the configuration from JSON on app creation
        getConfiguration();


    } //end onCreate


    //Get the configuration from the API
        //Will give us the secure Base URL and the Poster Width (via JSON)
    private void getConfiguration(){

        //Create the URL
        String url = API_BASE_URL + "/configuration";

        //Set the request parameters (USING RequestParams Object)
        RequestParams configParams = new RequestParams();

        //API Key always required
        configParams.put(API_KEY_PARAM, getString(R.string.api_key) );

        //Now, execute the GET Request, expecting a JSON Response Object
        client.get(url, configParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    //Using this to go down to the level that we want to access secure_bse_url from.
                    JSONObject images = response.getJSONObject("images");

                    //get the image base URL
                    imageBaseUrl = images.getString("secure_base_url");

                    //Similarly, get the poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");

                    //Now, use the object at index 3 OR default of w342
                    posterSize = posterSizeOptions.optString(3, "w342");

                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }


            } //end onSuccess

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                //Call logError with the appropriate data
                logError("Failure with configuration", throwable, true);

            }// end onFailure

        }); // end JSONHttpResponseHandler



    } // end getConfiguration


    //Handle errors by logging and alerting the user.
    private void logError(String message, Throwable error, boolean alertUser){

        Log.e(MAIN_ACTIVITY_TAG, message, error);

        //Based on the boolean, alert the user to avoid a silent error
        if (alertUser){

            //Show long Toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } //end if

    } // end logError



} //end class MainActivity
