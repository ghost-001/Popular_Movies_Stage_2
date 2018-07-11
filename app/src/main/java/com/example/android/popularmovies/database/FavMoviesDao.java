package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popularmovies.model.MovieDetails;

import java.util.List;

@Dao
public interface FavMoviesDao {
    @Query("SELECT * FROM FavMovies")
    LiveData<List<MovieDetails>> loadAllMovies();

    @Insert
    void insertMovies(MovieDetails movieDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovies(MovieDetails movieDetails);

    @Delete
    void deleteTask(MovieDetails movieDetails);

    @Query("SELECT * FROM FavMovies WHERE movie_id=:id")
    LiveData<MovieDetails> loadMovieByID(int id);

    @Query("DELETE FROM FavMovies")
    void deleteTable();

    @Query("Delete FROM FavMovies where movie_id =:id")
    void deleteMovieByID(int id);
}
