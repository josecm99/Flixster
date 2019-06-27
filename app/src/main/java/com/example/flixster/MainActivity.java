package com.example.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    //The list of currently playing movies
    ArrayList<Movie> movieList;

    //The RecyclerView
    RecyclerView rvMovies;

    //The adapter wired to the RecyclerView
    MovieAdapter adapter;

    //Image Configuration
    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the client
        client = new AsyncHttpClient();

        //Initialize the movies list
        movieList = new ArrayList<>();

        //Initialize the adapter after the movieList has been created
        //Movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movieList);

        //Resolve the RecyclerView and connect BOTH the Layout Manager AND Adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this) );
        rvMovies.setAdapter(adapter);

        //Get the configuration from JSON on app creation
        getConfiguration();


    } //end onCreate



    //Get the list of currently playing movies from the API
    private void getCurrentlyPlayingMovies(){
        //Create the URL
        String url = API_BASE_URL + "/movie/now_playing";

        //Set the request parameters (USING RequestParams Object)
        RequestParams nowPlayingParams = new RequestParams();

        //API Key always required
        nowPlayingParams.put(API_KEY_PARAM, getString(R.string.api_key) );

        //Now execute the GET request
        client.get(url, nowPlayingParams, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    //Load the results into movieList
                    JSONArray results = response.getJSONArray("results");

                    //Iterate through results and create Movie objects
                    for (int i = 0; i < results.length(); i++){

                        //Get the JSON Object
                        Movie movie = new Movie(results.getJSONObject(i) );

                        //Add this movie to our movieList
                        movieList.add(movie);
                        //Notify the adapter that the data set has CHANGED
                        adapter.notifyItemInserted(movieList.size() - 1);
                    } // end for

                    Log.i(MAIN_ACTIVITY_TAG, String.format("Loaded %s movies", results.length() ) );


                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }


            }//end onSuccess

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                logError("Failed to get data from now_playing endpoint.", throwable, true);

            }//end onFailure
        });


    } // end getCurrently PlayingMovies



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
                    config = new Config(response);

                    Log.i(MAIN_ACTIVITY_TAG, String.format("Loaded configuration with baseImageURL %s and posterSize %s",
                            config.getImageBaseUrl(), config.getPosterSize() ) );

                    //Now pass the config to the adapter
                    adapter.setConfig(config);

                    //Get the movie data into the movieList
                    getCurrentlyPlayingMovies();

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
