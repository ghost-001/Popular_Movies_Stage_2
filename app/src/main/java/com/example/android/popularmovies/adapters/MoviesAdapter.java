package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utility.OnPosterListner;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.AllMovieViewHolder> {

    private final OnPosterListner mOnPosterListner;
    private List<Movie> mMovieList;
    private Context mContext;
    private String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private String BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/w500";


    public MoviesAdapter(List<Movie> movie, Context context) {
        mContext = context;
        this.mMovieList = movie;
        mOnPosterListner = (OnPosterListner) context;
    }

    public void updateMovieList(List<Movie> newlist) {

        mMovieList.addAll(newlist);
    }

    @NonNull
    @Override
    public MoviesAdapter.AllMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_movie, parent, false);
        return new AllMovieViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.AllMovieViewHolder holder, int position) {
        Log.d("Main", "BIND");
        final Movie movie = mMovieList.get(position);
        final String image_url = BASE_IMAGE_URL + movie.getPoster_path();
        final String backdrop_url = BASE_BACKDROP_URL + movie.getBackdrop_path();
        holder.setImage(image_url);
        holder.movie_name.setText(mMovieList.get(position).getTitle());
        holder.movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnPosterListner.OnPosterclicked(holder.movie_poster, movie.getId(), movie.getTitle(), backdrop_url, image_url);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            return 0;
        }
        return mMovieList.size();
    }

    public static class AllMovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView movie_poster;
        TextView movie_name;
        Context cc;


        public AllMovieViewHolder(View itemView, Context context) {
            super(itemView);
            cc = context;
            movie_poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name);

        }

        public void setImage(String url) {

            Picasso.get().load(url)
                    .placeholder(R.drawable.movie).into(movie_poster);

        }
    }

}
