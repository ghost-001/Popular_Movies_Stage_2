package com.example.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.helper_classes.MovieDetails;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<MovieDetails>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d("TAG", "Activitely retrieving the movies from database in Viewmodel");
        movies = database.FavMoviesDao().loadAllMovies();
    }

    public LiveData<List<MovieDetails>> getMovies() {
        return movies;
    }
}
