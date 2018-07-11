package com.example.android.popularmovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.repository.ProjectRepository;
import com.example.android.popularmovies.model.MovieDetails;

public class DetailActivityMovieViewModel extends ViewModel {
    private final ProjectRepository mProjectRepository = ProjectRepository.getInstance();
    private LiveData<MovieDetails> mDetailsLiveData;

    public DetailActivityMovieViewModel(int movieId) {
        mDetailsLiveData = mProjectRepository.getMovieDetails(movieId);

    }

    public LiveData<MovieDetails> getDetailsLiveData() {
        return mDetailsLiveData;
    }
}
