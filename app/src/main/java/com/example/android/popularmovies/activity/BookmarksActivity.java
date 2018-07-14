package com.example.android.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.BookmarkAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.utility.onBookmarkClick;
import com.example.android.popularmovies.viewModel.MainViewModel;

import java.util.List;

public class BookmarksActivity extends AppCompatActivity implements onBookmarkClick {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private BookmarkAdapter mBookmarkAdapter;
    private AppDatabase mDb;

    private Context mContext;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        mDb = AppDatabase.getInstance((getApplicationContext()));
        mContext = this;

        mAppBarLayout = findViewById(R.id.detail_app_bar_layout);
        mToolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.favourites));

        Integer span = 2;
        if (getResources().getConfiguration().orientation == 2) {
            span = 4;
        }
        mLayoutManager = new GridLayoutManager(this, span);
        mRecyclerView = findViewById(R.id.bookmark_recycle);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getMovies().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(@Nullable List<MovieDetails> movieDetails) {

                mBookmarkAdapter = new BookmarkAdapter(movieDetails, mContext);
                mRecyclerView.setAdapter(mBookmarkAdapter);
            }
        });
    }

    @Override
    public void OnBookmarkPosterClicked(Integer movie_id, String movie_name) {

        Intent intent = new Intent(BookmarksActivity.this, BookmarksDetailActivity.class);
        intent.putExtra("movie_id", movie_id);
        intent.putExtra("movie_name", movie_name);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bookmark_detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clear_bookmarks) {
            Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
            mDb.FavMoviesDao().deleteTable();

        }
        return super.onOptionsItemSelected(item);
    }
}
