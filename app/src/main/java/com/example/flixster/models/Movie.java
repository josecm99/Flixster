package com.example.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {

    //Values accessed from the API

    public String title;
    public String overview;
    public String posterPath; //Not the FULL Poster URL
    public String backdropPath; // NOt the FULL Backdrop URL
    public double movieRating;


    public Movie(){

    }// end default constructor


    //Initialize from JSON Data
    public Movie(JSONObject object ) throws JSONException {

        title = object.getString("title");

        overview = object.getString("overview");

        posterPath = object.getString("poster_path");

        backdropPath = object.getString("backdrop_path");

        movieRating = object.getDouble("vote_average");
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

    public double getMovieRating(){
        return movieRating;
    }
}// end class Movie
