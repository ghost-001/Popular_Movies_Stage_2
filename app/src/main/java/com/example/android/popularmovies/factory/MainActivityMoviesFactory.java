package com.example.android.popularmovies.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.viewModel.MainActivityMovieListViewModel;

public class MainActivityMoviesFactory extends ViewModelProvider.NewInstanceFactory {

    private int page;
    private int category;

    public MainActivityMoviesFactory(int page, int category) {
        this.page = page;
        this.category = category;
        Log.d("TAG FAC", "in factory");
    }

    public void setValues(int page, int category) {
        this.page = page;
        this.category = category;
        Log.d("TAG FAC", "in factory");
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityMovieListViewModel(page, category);
    }
}

