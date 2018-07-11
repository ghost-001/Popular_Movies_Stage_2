package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Genre;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.AllGenresViewHolder> {
    private List<Genre> mGenres;
    private String TEXT_COLOR;
    private Context mContext;

    public GenresAdapter(Context context, List<Genre> mGenre, String textColor) {
        this.mContext = context;
        this.mGenres = mGenre;
        this.TEXT_COLOR = textColor;
    }

    @NonNull
    @Override
    public GenresAdapter.AllGenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_genre, parent, false);
        return new AllGenresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresAdapter.AllGenresViewHolder holder, int position) {
        final Genre genre = mGenres.get(position);
        if (TEXT_COLOR.equals("black")) {
            holder.mGenre_tv.setTextColor(mContext.getResources().getColor(R.color.black));
        }
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


    }


}
