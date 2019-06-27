package com.example.flixster;

import android.content.Context;
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

import java.util.ArrayList;

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
        Movie movie = movieList.get(i);

        //Update the information on the activity
        viewHolder.tvTitle.setText(movie.getTitle() );
        viewHolder.tvOverview.setText(movie.getOverview() );

        //Build URL for Poster Image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath() );

        //Load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(viewHolder.ivPosterImage);


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
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Lookup view objects by ID
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);


        }
    } // end class ViewHolder


} // end class MovieAdapter