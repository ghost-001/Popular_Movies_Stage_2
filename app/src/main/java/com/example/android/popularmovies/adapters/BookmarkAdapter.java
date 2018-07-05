package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.helper_classes.MovieDetails;
import com.example.android.popularmovies.utility.onBookmarkClick;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.AllMovieViewHolder> {

    private final onBookmarkClick mOnBookmarkClick;
    private List<MovieDetails> mMovieList;
    private Context mContext;


    public BookmarkAdapter(List<MovieDetails> movies, Context context) {
        mContext = context;
        mMovieList = movies;
        mOnBookmarkClick = (onBookmarkClick) context;

    }

    @NonNull
    @Override
    public BookmarkAdapter.AllMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_movie, parent, false);
        return new AllMovieViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapter.AllMovieViewHolder holder, int position) {
        final MovieDetails movie = mMovieList.get(position);

        holder.movie_poster.setImageBitmap(movie.getImage_poster());
        holder.movie_name.setText(movie.getTitle());
        holder.movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnBookmarkClick.OnBookmarkPosterclicked(movie.getMovie_id(), movie.getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void addMovies(List<MovieDetails> movies) {
        for (MovieDetails m : movies) {
            mMovieList.add(m);
        }
        notifyDataSetChanged();
    }

    public static class AllMovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView movie_poster;
        TextView movie_name;
        Context cc;


        public AllMovieViewHolder(View itemView, Context context) {
            super(itemView);
            cc = context;
            movie_name = (TextView) itemView.findViewById(R.id.movie_name);
            movie_poster = (ImageView) itemView.findViewById(R.id.movie_poster);

        }

        public void setImage(String url) {

            Picasso.get().load(url)
                    .placeholder(R.drawable.photo).into(movie_poster);

        }
    }

}
