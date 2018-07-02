package com.example.android.popularmovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.BookmarkAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.helper_classes.MovieDetails;
import com.example.android.popularmovies.utility.AppExecutors;

import java.util.List;

public class BookmarksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private BookmarkAdapter mBookmarkAdapter;
    private AppDatabase mDb;
    private List<MovieDetails> mMovieDetails;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        mDb = AppDatabase.getInstance((getApplicationContext()));
        mContext = this;
//        Toast.makeText(this, "xx" + mMovieDetails.size(), Toast.LENGTH_SHORT).show();

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView = findViewById(R.id.bookmark_recycle);
        mRecyclerView.setLayoutManager(mLayoutManager);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieDetails = mDb.FavMoviesDao().loadAllMovies();
                mBookmarkAdapter = new BookmarkAdapter(mMovieDetails, mContext);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mRecyclerView.setAdapter(mBookmarkAdapter);
                    }
                });

            }
        });


    }
}
