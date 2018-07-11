package com.example.android.popularmovies.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.repository.ProjectRepository;
import com.example.android.popularmovies.model.MovieDetails;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<MovieDetails>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movies = ProjectRepository.getInstance().getAllMoviesFromDatabase(application);
    }

    public LiveData<List<MovieDetails>> getMovies() {
        return movies;
    }
}
