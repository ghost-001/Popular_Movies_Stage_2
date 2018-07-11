package com.example.android.popularmovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.repository.ProjectRepository;

import java.util.List;

public class MainActivityMovieListViewModel extends ViewModel {
    private final LiveData<List<Movie>> movieListObservable;
    private final ProjectRepository mProjectRepository = ProjectRepository.getInstance();
    ;


    //1 for top rated
    //0 for popular
    public MainActivityMovieListViewModel(int page, int category) {


        Log.d("TAG", "Actively retrieving from MainActivityViewModel");

        movieListObservable = mProjectRepository.getAllMoviesList(page, category);
    }


    public LiveData<List<Movie>> getMovieListObservable() {
        return movieListObservable;
    }
}
