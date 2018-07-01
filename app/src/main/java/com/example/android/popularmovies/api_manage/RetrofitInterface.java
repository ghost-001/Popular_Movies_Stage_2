package com.example.android.popularmovies.api_manage;

import com.example.android.popularmovies.helper_classes.MovieDetails;
import com.example.android.popularmovies.helper_classes.MovieResponse_first;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitInterface {


    @GET("movie/top_rated")
    Call<MovieResponse_first> getTopRatedMovies(@Query("api_key") String API_KEY, @Query("page") int num);

    @GET("movie/popular")
    Call<MovieResponse_first> getPopularMovies(@Query("api_key") String API_KEY, @Query("page") int num);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id")int id, @Query("api_key") String API_KEY, @Query("append_to_response") String details);

}
