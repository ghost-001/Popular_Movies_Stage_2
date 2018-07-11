package com.example.android.popularmovies.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.viewModel.AddMoviesViewModel;

public class AddMoviesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int movies_id;

    public AddMoviesViewModelFactory(AppDatabase db, int movies_id) {
        mDb = db;
        this.movies_id = movies_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddMoviesViewModel(mDb, movies_id);
    }
}
