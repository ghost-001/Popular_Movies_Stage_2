package com.example.android.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.ReviewResults;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.AllReviewsViewHolder> {
    private List<ReviewResults> mReviews;

    public ReviewsAdapter(List<ReviewResults> reviews) {
        this.mReviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.AllReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reviews, parent, false);
        return new AllReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.AllReviewsViewHolder holder, int position) {
        final ReviewResults reviews = mReviews.get(position);

        holder.mAuthor_tv.setText(reviews.getAuthor());
        holder.mExpandable_tv.setText(reviews.getContent());

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class AllReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView mAuthor_tv;
        TextView mExpandable_tv;

        public AllReviewsViewHolder(View itemView) {
            super(itemView);
            mAuthor_tv = itemView.findViewById(R.id.author_name);
            mExpandable_tv = itemView.findViewById(R.id.content_text);
        }

        public void setAuthor(String genre_name) {
            mAuthor_tv.setText(genre_name);
        }
    }


}
