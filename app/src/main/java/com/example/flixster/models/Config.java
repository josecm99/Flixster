package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    //Base Url for loading images
    String imageBaseUrl;

    //The Poster size to use when fetching images
    String posterSize;

    //The backdrop size used when the phone is in landscape mode
    String backdropSize;

    public Config(JSONObject object) throws JSONException {

        //Using this to go down to the level that we want to access secure_bse_url from.
        JSONObject images = object.getJSONObject("images");

        //get the image base URL
        imageBaseUrl = images.getString("secure_base_url");

        //Similarly, get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");

        //Now, use the object at index 3 OR default of w342
        posterSize = posterSizeOptions.optString(3, "w342");

        //Similarly, get the backdrop size
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");

        //Now, use the object at index 3 OR default of w342
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }// end constructor

    //Helper method for creating URLs
    public String getImageUrl(String size, String path){

        return String.format("%s%s%s", imageBaseUrl, size, path);

    }// end getImageUrl



    public String getImageBaseUrl() {
        return imageBaseUrl;
    }// end getImageBaseUrl

    public String getPosterSize() {
        return posterSize;
    } // end getPosterSize

    public String getBackdropSize() { return backdropSize; } // end getBackdropSize


}// end class Config
