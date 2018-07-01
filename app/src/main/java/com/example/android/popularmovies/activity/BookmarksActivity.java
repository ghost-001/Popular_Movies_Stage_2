package com.example.android.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.BookmarkAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.helper_classes.MovieDetails;

import java.util.List;

public class BookmarksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private BookmarkAdapter mBookmarkAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        mDb = AppDatabase.getInstance((getApplicationContext()));

        List<MovieDetails> mMovieDetails = mDb.FavMoviesDao().loadAllMovies();
        Toast.makeText(this,"xx" + mMovieDetails.size(),Toast.LENGTH_SHORT).show();
        mBookmarkAdapter = new BookmarkAdapter(mMovieDetails,this);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView = findViewById(R.id.bookmark_recycle);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBookmarkAdapter);
    }
}
