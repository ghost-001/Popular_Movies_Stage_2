package com.example.android.popularmovies.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.viewModel.DetailActivityMovieViewModel;

public class DetailActivityMovieFactory extends ViewModelProvider.NewInstanceFactory {
    private int movieId;

    public DetailActivityMovieFactory(int id) {
        this.movieId = id;

    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityMovieViewModel(movieId);
    }
}
