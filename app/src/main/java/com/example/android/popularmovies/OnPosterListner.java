package com.example.android.popularmovies;

import android.widget.ImageView;

public interface OnPosterListner {
    public void OnPosterclicked(ImageView image, Integer movie_id, String movie_name, String poster_url);
}
