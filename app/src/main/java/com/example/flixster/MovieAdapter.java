package com.example.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //List of movies
    ArrayList<Movie> movieList;

    //Config object needed for image urls
    Config config;

    //Context for rendering image
    Context context;


    //Initialize with list
    public MovieAdapter(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    //Creates and inflates a new view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Get the context from the parent and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Create the view using the item_movie layout we made in XML
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);

        //Now return a new ViewHolder
        return new ViewHolder(movieView);

    }

    @Override
    //Associates an inflated or created view to a new item
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Get the movie data at the specified position
            //Made final so that it can be accessed inside of the onClickListener
        final Movie movie = movieList.get(i);

        //Update the information on the activity
        viewHolder.tvTitle.setText(movie.getTitle() );
        viewHolder.tvOverview.setText(movie.getOverview() );

        //Determine the current orientation to see if we need to use the poster or the backdrop
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        double movieRating = movie.getMovieRating();

        if (movieRating > 7.50){
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorGoodMovie) );
        }else if (movieRating > 5.00){
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorOkMovie) );
        }else{
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorBadMovie)  );
        }



        //Build URL for Poster Image
        String imageUrl = null;

        //If in portrait mode, load the poster image
        if (isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath() );
        }
        else{
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath() );
        }

        //get the correct placeholder and imageView for the current orientation
        int placeholderID = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;




        //Load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 30, 0) )
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(imageView);

        // TODO LOOK UP INFORMATION ON HOW THIS IS WORKING EXACTLY



    } // end onBindViewHolder

    @Override
    //Returns the total number of items in the list
    public int getItemCount() {
        return movieList.size();
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    //Now create the ViewHolder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Lookup view objects by ID
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {


            int position = getAdapterPosition();

            //If the position exists in the view, continue
            if (position != RecyclerView.NO_POSITION){
                Movie movie = movieList.get(position);

                //Create an intent to send the correct information over to the new Activity.
                Intent seeMovieDetails = new Intent(context, MovieDetailsActivity.class);

                //Use the Parceler to pass the movie through in the Intent
                seeMovieDetails.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie) );

                //Also try to pass in Config using Parcels to try and have the image displayed
                seeMovieDetails.putExtra(Config.class.getSimpleName(), Parcels.wrap(config) );


                //Finally, show the new Activity
                context.startActivity(seeMovieDetails);


            }// end if

        }
    } // end class ViewHolder


} // end class MovieAdapter
