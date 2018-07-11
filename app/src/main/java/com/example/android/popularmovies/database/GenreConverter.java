package com.example.android.popularmovies.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.popularmovies.model.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GenreConverter {

    @TypeConverter
    public static List<Genre> FromString(String xx) {
        Type listType = new TypeToken<List<Genre>>() {
        }.getType();

        return new Gson().fromJson(xx, listType);
    }

    @TypeConverter
    public static String ToString(List<Genre> genres) {
        Gson gson = new Gson();

        String json = gson.toJson(genres);

        return json;
    }


}
