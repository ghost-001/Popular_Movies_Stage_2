package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.helper_classes.MovieDetails;

public class AddMoviesViewModel extends ViewModel {

    private LiveData<MovieDetails> movies;

    public LiveData<MovieDetails> getMovies() {
        return movies;
    }

    public AddMoviesViewModel(AppDatabase database, int movie_id) {

        this.movies = database.FavMoviesDao().loadMovieByID(movie_id);
    }
}
