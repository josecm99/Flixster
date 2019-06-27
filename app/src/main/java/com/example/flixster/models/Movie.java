package com.example.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    //Values accessed from the API

    private String title;
    private String overview;
    private String posterPath; //Not the FULL Poster URL
    private String backdropPath; // NOt the FULL Backdrop URL


    //Initialize from JSON Data
    public Movie(JSONObject object ) throws JSONException {

        title = object.getString("title");

        overview = object.getString("overview");

        posterPath = object.getString("poster_path");

        backdropPath = object.getString("backdrop_path");
    }// end constructor

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}// end class Movie
