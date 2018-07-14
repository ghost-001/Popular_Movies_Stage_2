package com.example.android.popularmovies.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.GenresAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.factory.AddMoviesViewModelFactory;
import com.example.android.popularmovies.model.Genre;
import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.utility.AppExecutors;
import com.example.android.popularmovies.viewModel.AddMoviesViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookmarksDetailActivity extends AppCompatActivity {
    private int movie_id;
    private String movie_name;
    private ImageView mPoster_image;
    private TextView mMovie_Rating;
    private TextView mMovie_Runtime;
    private TextView mMovie_Genre;
    private TextView mMovie_release;
    private TextView mMovie_synopsis;
    private FloatingActionButton mActionButton;
    private AppDatabase mAppDatabase;
    private ProgressBar mProgressBar;
    private RecyclerView mGenreRecyclerView;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private Boolean checkBookmark = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_detail);


        initViews();
        mProgressBar.setVisibility(View.VISIBLE);
        movie_id = getIntent().getIntExtra("movie_id", 0);
        movie_name = getIntent().getStringExtra("movie_name");
        final AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());

        AddMoviesViewModelFactory factory = new AddMoviesViewModelFactory(mDb, movie_id);
        final AddMoviesViewModel viewModel
                = ViewModelProviders.of(this, factory).get(AddMoviesViewModel.class);
        viewModel.getMovies().observe(this, new Observer<MovieDetails>() {


            @Override
            public void onChanged(@Nullable MovieDetails movieDetails) {
                viewModel.getMovies().removeObserver(this);
                initViewsWithData(movieDetails);
            }
        });


    }


    public void initViews() {

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());

        mAppBarLayout = findViewById(R.id.detail_app_bar_layout);
        mToolbar = findViewById(R.id.detail_toolbar);
        mProgressBar = findViewById(R.id.detail_progress);
        mMovie_Genre = findViewById(R.id.detail_genre);
        mGenreRecyclerView = findViewById(R.id.detail_genre_recycler);

        mPoster_image = findViewById(R.id.movie_poster);
        mMovie_Rating = findViewById(R.id.detail_rating_tv);
        mMovie_Runtime = findViewById(R.id.detail_runtime_tv);
        mMovie_release = findViewById(R.id.detail_release_tv);
        mMovie_synopsis = findViewById(R.id.detail_synopsis_tv);
        mActionButton = findViewById(R.id.detail_action);


    }


    public void initViewsWithData(final MovieDetails movieDetails) {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(movie_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setFab();
        mProgressBar.setVisibility(View.GONE);

        mPoster_image.setImageBitmap(movieDetails.getImage_poster());

        mMovie_Rating.setText(String.format(Locale.ENGLISH, "%1$,.2f", movieDetails.getVoteAverage()));

        List<Genre> mGenre = movieDetails.getGenres();
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mGenreRecyclerView.setLayoutManager(mLayoutManager);
        GenresAdapter mGenresAdapter = new GenresAdapter(this, mGenre, "black");
        mGenreRecyclerView.setAdapter(mGenresAdapter);

        mMovie_Runtime.setText(String.format(Locale.ENGLISH, "%d", movieDetails.getRuntime()));
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = fmt.parse(movieDetails.getReleaseDate());
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            String xx = fmtOut.format(date);
            mMovie_release.setText(xx);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mMovie_synopsis.setText(movieDetails.getOverview());


        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBookmark) {
                    deleteFromDatabase(movie_id);
                    checkBookmark = false;
                    setFab();
                } else {
                    saveLocalData(movieDetails);
                    checkBookmark = true;
                    setFab();
                }

            }
        });
    }

    public void saveLocalData(final MovieDetails mMovieDetails) {

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.FavMoviesDao().insertMovies(mMovieDetails);
            }
        });

        Toast.makeText(this, R.string.add, Toast.LENGTH_SHORT).show();
    }

    public void deleteFromDatabase(Integer id) {
        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        mAppDatabase.FavMoviesDao().deleteMovieByID(id);
        Toast.makeText(BookmarksDetailActivity.this, R.string.delete_this, Toast.LENGTH_SHORT).show();
    }

    public void setFab() {
        if (checkBookmark) {
            //setting white color as background
            mActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_fav, this.getTheme()));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_fav2, this.getTheme()));
            }
            //setting red color as background
            mActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
        }
    }

}

