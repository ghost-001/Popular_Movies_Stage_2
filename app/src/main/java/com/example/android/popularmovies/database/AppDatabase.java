package com.example.android.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.helper_classes.MovieDetails;

@Database(entities = {MovieDetails.class}, version = 1, exportSchema = false)
@TypeConverters({GenreConverter.class, ImageConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = "TAG";
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies2.db";
    private static AppDatabase sInstance;


    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavMoviesDao FavMoviesDao();
}
