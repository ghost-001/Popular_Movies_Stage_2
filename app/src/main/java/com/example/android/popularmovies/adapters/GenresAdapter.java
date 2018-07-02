package com.example.android.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.helper_classes.Genre;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.AllGenresViewHolder> {
    private List<Genre> mGenres;

    public GenresAdapter(List<Genre> mGenre) {
        this.mGenres = mGenre;
    }

    @NonNull
    @Override
    public GenresAdapter.AllGenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_single, parent, false);
        return new AllGenresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresAdapter.AllGenresViewHolder holder, int position) {
        final Genre genre = mGenres.get(position);
        holder.mGenre_tv.setText(genre.getName());

    }

    @Override
    public int getItemCount() {
        return mGenres.size();
    }

    public class AllGenresViewHolder extends RecyclerView.ViewHolder {
        TextView mGenre_tv;

        public AllGenresViewHolder(View itemView) {
            super(itemView);
            mGenre_tv = itemView.findViewById(R.id.genre_1);
        }

        public void setGenre(String genre_name) {
            mGenre_tv.setText(genre_name);
        }
    }


}
