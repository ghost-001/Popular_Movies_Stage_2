package com.example.android.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popularmovies.helper_classes.MovieDetails;

import java.util.List;

@Dao
public interface FavMoviesDao {
    @Query("SELECT * FROM FavMovies")
    List<MovieDetails> loadAllMovies();

    @Insert
    void insertMovies(MovieDetails movieDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovies(MovieDetails movieDetails);

    @Delete
    void deleteTask(MovieDetails movieDetails);

}
