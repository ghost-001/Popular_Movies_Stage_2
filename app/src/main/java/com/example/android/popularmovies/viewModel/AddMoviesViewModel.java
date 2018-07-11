package com.example.android.popularmovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.repository.ProjectRepository;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.MovieDetails;

public class AddMoviesViewModel extends ViewModel {

    private LiveData<MovieDetails> movies;

    public AddMoviesViewModel(AppDatabase database, int movieId) {
        Log.d("TAG", "Actively retriving database in ADDMOVIESViewmodel");

        this.movies = ProjectRepository.getInstance().getMoviesByIdFromDb(database, movieId);

    }

    public LiveData<MovieDetails> getMovies() {
        return movies;
    }
}
