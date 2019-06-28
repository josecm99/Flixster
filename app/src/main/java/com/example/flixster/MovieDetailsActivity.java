package com.example.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    //The movie that we will be displaying on the screen
    Movie movie;

    //The config that we will be using to generate the image
    Config config;

    //Creating a config object to get the info we need for displaying the image

    //XML Components
    ImageView ivBackdropImage;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName() ) );
        Log.d("MovieDetailsDebug", "Successfully unwrapped the Movie sent in the Intent using Parcels.wrap()");

        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName() ) );
        Log.d("MovieDetailsDebug","Successfully unwrapped the Config sent in the Intent using Parcels.wrap()");

        //Connect the XML Components using findViewById
        ivBackdropImage = (ImageView) findViewById(R.id.ivBackdropImage);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        //Set the text for the Title and the Overview
        tvTitle.setText(movie.getTitle() );
        tvOverview.setText(movie.getOverview() );

        String imageURL = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath() );

        //Load image into the ImageView using Glide
        Glide.with(this)
                .load(imageURL)
                .bitmapTransform(new RoundedCornersTransformation(this, 30, 0) )
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(ivBackdropImage);


        //Create the rating for the stars
        double voteAverage = movie.getMovieRating();

        float starRating = (float) (voteAverage / 2);

        rbVoteAverage.setRating(starRating);


    }// end onCreate
}// end class MovieDetailsAbility
