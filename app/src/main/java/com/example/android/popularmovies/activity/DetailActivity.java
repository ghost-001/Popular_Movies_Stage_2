package com.example.android.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.viewModel.DetailActivityMovieViewModel;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.GenresAdapter;
import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.factory.DetailActivityMovieFactory;
import com.example.android.popularmovies.model.Genre;
import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.model.ReviewResults;
import com.example.android.popularmovies.model.Reviews;
import com.example.android.popularmovies.model.VideoResults;
import com.example.android.popularmovies.model.Videos;
import com.example.android.popularmovies.utility.AppExecutors;
import com.example.android.popularmovies.utility.OnYoutubeClickListener;
import com.example.android.popularmovies.viewModel.MainViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends AppCompatActivity implements OnYoutubeClickListener {

    private String TAG = "DetailActivity";
    private Integer color = Color.parseColor("#000000");
    private int mDarkMutedColor;
    private int mMutedColor;
    private int movieId;
    private String movieName;
    private String backdropUrl;
    private String posterUrl;
    private ImageView mToolbarImage;
    private ImageView mPosterImage;
    private TextView mMovieName;
    private TextView mMovieRating;
    private TextView mMovieRuntime;
    private TextView mMovieGenre;
    private TextView mMovieRelease;
    private TextView mMovieSynopsis;
    private View mView;
    private ImageView mRatingStar;
    private ProgressBar mProgressBar;
    private RelativeLayout mLayoutRating, mLayoutRuntime, mLayoutRelease, mLayoutCard, mLayoutTrailer;
    private RelativeLayout mLayoutReviews;
    private ImageView mReviewImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private NestedScrollView mNested;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mGenreRecyclerView, mReviewRecyclerView;
    private FloatingActionButton mActionButton;
    private AppDatabase mAppDatabase;
    private Boolean REVIEWS_SHOW = true;
    private Boolean checkBookmark = false;
    private Integer movieDbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mProgressBar = findViewById(R.id.detail_progress);
        mProgressBar.setVisibility(View.VISIBLE);

        if (!checkInternet()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            builder.create();
            builder.show();
        } else {

            movieId = getIntent().getIntExtra("movie_id", 0);
            movieName = getIntent().getStringExtra("movie_name");
            backdropUrl = getIntent().getStringExtra("backdrop_url");
            posterUrl = getIntent().getStringExtra("poster_url");
            initViews();
            mProgressBar.setVisibility(View.VISIBLE);
            loadImageColorInToolbar();
            loadMovieDetails();

            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset == -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight()) {
                        mCollapsingToolbarLayout.setTitle(movieName);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    } else {
                        mCollapsingToolbarLayout.setTitle(" ");
                    }
                }
            });
        }

    }


    public Boolean checkInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    public void initViews() {


        mToolbar = findViewById(R.id.detail_toolbar);
        mNested = findViewById(R.id.detail_nested);
        mAppBarLayout = findViewById(R.id.detail_app_bar_layout);
        mCollapsingToolbarLayout = findViewById(R.id.detail_collapseToolbarLayout);
        setSupportActionBar(mToolbar);


        mToolbarImage = findViewById(R.id.detail_collapse_iv);


        mGenreRecyclerView = findViewById(R.id.detail_genre_recycler);
        mReviewRecyclerView = findViewById(R.id.detail_reviews_recycler);
        mLayoutRating = findViewById(R.id.detail_relative_rating);
        mLayoutRuntime = findViewById(R.id.detail_relative_runtime);
        mLayoutRelease = findViewById(R.id.detail_relative_release);
        mLayoutCard = findViewById(R.id.detail_card_relative);
        mLayoutTrailer = findViewById(R.id.detail_trailer_relative);
        mLayoutReviews = findViewById(R.id.detail_reviews_relative);
        mMovieGenre = findViewById(R.id.detail_genre);


        mPosterImage = findViewById(R.id.movie_poster);
        mMovieName = findViewById(R.id.detail_movie_name);
        mMovieRating = findViewById(R.id.detail_rating_tv);
        mMovieRuntime = findViewById(R.id.detail_runtime_tv);
        mMovieRelease = findViewById(R.id.detail_release_tv);
        mMovieSynopsis = findViewById(R.id.detail_synopsis_tv);
        mReviewImage = findViewById(R.id.detail_review_image);
        mRatingStar = findViewById(R.id.detail_rating_star);
        mView = findViewById(R.id.detail_view_1);

        mActionButton = findViewById(R.id.detail_action);


    }

    public void loadImageColorInToolbar() {


        Picasso.get().load(backdropUrl)
                .placeholder(R.drawable.image_loading)
                .into(new Target() {


                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                              mToolbarImage.setImageBitmap(bitmap);
                              Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                  @Override
                                  public void onGenerated(@NonNull Palette palette) {
                                      Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                      if (textSwatch != null) {
                                          color = textSwatch.getRgb();
                                      }
                                      mDarkMutedColor = palette.getDarkMutedColor(color);
                                      mMutedColor = palette.getMutedColor(color);
                                      setBackgroundColor();
                                  }
                              });

                          }

                          @Override
                          public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                              Log.e(TAG, e.getMessage());

                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {

                          }
                      }
                );
    }

    public void setBackgroundColor() {


        mCollapsingToolbarLayout.setBackgroundColor(color);
        mCollapsingToolbarLayout.setStatusBarScrimColor(mDarkMutedColor);
        mCollapsingToolbarLayout.setContentScrimColor(mDarkMutedColor);

        mNested.setBackgroundColor(mMutedColor);

        final GradientDrawable gradientDrawable_name = (GradientDrawable) mMovieName.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_rating = (GradientDrawable) mLayoutRating.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_runtime = (GradientDrawable) mLayoutRuntime.getBackground().mutate();
        final GradientDrawable gradientDrawable_genre = (GradientDrawable) mMovieGenre.getBackground().mutate();
        final GradientDrawable gradientDrawable_genre_recycler = (GradientDrawable) mGenreRecyclerView.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_release = (GradientDrawable) mLayoutRelease.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_card = (GradientDrawable) mLayoutCard.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_trailer = (GradientDrawable) mLayoutTrailer.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_review = (GradientDrawable) mLayoutReviews.getBackground().mutate();

        gradientDrawable_name.setColor(mDarkMutedColor);
        gradientDrawable_relative_rating.setColor(mDarkMutedColor);
        gradientDrawable_relative_runtime.setColor(mDarkMutedColor);
        gradientDrawable_genre.setColor(mDarkMutedColor);
        gradientDrawable_genre_recycler.setColor(mDarkMutedColor);
        gradientDrawable_relative_release.setColor(mDarkMutedColor);
        gradientDrawable_relative_card.setColor(mDarkMutedColor);
        gradientDrawable_relative_trailer.setColor(mDarkMutedColor);
        gradientDrawable_relative_review.setColor(mDarkMutedColor);
    }

    public void loadMovieDetails() {

        DetailActivityMovieFactory factory = new DetailActivityMovieFactory(movieId);
        final DetailActivityMovieViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailActivityMovieViewModel.class);

        viewModel.getDetailsLiveData().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(@Nullable MovieDetails movieDetails) {
                initViewsWithData(movieDetails);
            }
        });

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

    public void initViewsWithData(final MovieDetails mMovieDetails) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Picasso.get().load(posterUrl)
                .placeholder(R.drawable.image_loading)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        mPosterImage.setImageBitmap(bitmap);
                        mPosterImage.setVisibility(View.VISIBLE);
                        mMovieDetails.setImage_poster(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });




        checkIfBookmark();
        setFab();
        mActionButton.setVisibility(View.VISIBLE);
        mMovieName.setText(movieName);
        mMovieName.setVisibility(View.VISIBLE);
        mMovieName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mMovieName.setSelected(true);
                }
            }
        });


        mMovieRating.setText(String.format(Locale.ENGLISH, "%1$,.2f", mMovieDetails.getVoteAverage()));
        mLayoutRating.setVisibility(View.VISIBLE);
        mRatingStar.setVisibility(View.VISIBLE);
        mMovieRating.setVisibility(View.VISIBLE);


        mMovieRuntime.setText(String.format(Locale.ENGLISH, "%d", mMovieDetails.getRuntime()));
        mLayoutRuntime.setVisibility(View.VISIBLE);


        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = fmt.parse(mMovieDetails.getReleaseDate());
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            String xx = fmtOut.format(date);
            mMovieRelease.setText(xx);
            mMovieRelease.setVisibility(View.VISIBLE);
            mLayoutRelease.setVisibility(View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
            mLayoutRelease.setVisibility(View.VISIBLE);
        }

        List<Genre> mGenre = mMovieDetails.getGenres();
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mGenreRecyclerView.setLayoutManager(mLayoutManager);
        GenresAdapter mGenresAdapter = new GenresAdapter(this, mGenre, "white");
        mGenreRecyclerView.setAdapter(mGenresAdapter);

        mMovieGenre.setVisibility(View.VISIBLE);
        mGenreRecyclerView.setVisibility(View.VISIBLE);


        mMovieSynopsis.setText(mMovieDetails.getOverview());
        mLayoutCard.setVisibility(View.VISIBLE);
        mMovieSynopsis.setVisibility(View.VISIBLE);
        mView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        Videos mVideos = mMovieDetails.getVideos();


        Reviews mReviews = mMovieDetails.getReviews();
        final List<ReviewResults> reviewResults = mReviews.getResults();
        ReviewsAdapter mReviewsAdapter = new ReviewsAdapter(reviewResults);
        LinearLayoutManager mLayout = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(mLayout);
        mReviewRecyclerView.setAdapter(mReviewsAdapter);

        mLayoutReviews.setVisibility(View.VISIBLE);
        mLayoutReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (REVIEWS_SHOW) {
                    mReviewImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down, getTheme()));
                    if (!reviewResults.isEmpty()) {
                        mReviewRecyclerView.setVisibility(View.VISIBLE);

                        REVIEWS_SHOW = false;
                    } else {
                        TextView tv = findViewById(R.id.detail_no_reviews);
                        tv.setVisibility(View.VISIBLE);
                        REVIEWS_SHOW = false;
                    }
                } else if (!REVIEWS_SHOW) {
                    mReviewImage.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up, getTheme()));
                    if (!reviewResults.isEmpty()) {
                        mReviewRecyclerView.setVisibility(View.GONE);
                        REVIEWS_SHOW = true;
                    } else {
                        TextView tv = findViewById(R.id.detail_no_reviews);
                        tv.setVisibility(View.GONE);
                        REVIEWS_SHOW = true;
                    }
                }
            }
        });


        List<VideoResults> mVideoResults = mVideos.getResults();
        List<String> youtubeKeys = getYoutubeKeys(mVideoResults);
        AutoScrollViewPager viewPager = findViewById(R.id.detail_youTubePlay);
        viewPager.setInterval(3000);
        viewPager.startAutoScroll();
        viewPager.setCycle(true);
        TrailerAdapter mTrailerAdapter = new TrailerAdapter(this, youtubeKeys);
        viewPager.setAdapter(mTrailerAdapter);


        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        mLayoutTrailer.setVisibility(View.VISIBLE);
        mTrailerAdapter.registerDataSetObserver(indicator.getDataSetObserver());


        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBookmark) {
                    saveLocalData(mMovieDetails);
                    checkBookmark = true;
                    setFab();
                } else {
                    deleteFromDatabase(movieDbId);
                    checkBookmark = false;
                    setFab();

                }
            }
        });
        mAppBarLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);


    }

    public void checkIfBookmark() {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(@Nullable List<MovieDetails> movieDetails) {
                if (movieDetails.size() != 0) {
                    for (MovieDetails m : movieDetails) {
                        if (m.getId() == movieId) {
                            checkBookmark = true;
                            movieDbId = m.getMovie_id();
                        }
                    }
                }

            }
        });

    }
    public void deleteFromDatabase(Integer id){
        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        mAppDatabase.FavMoviesDao().deleteMovieByID(id);
        Toast.makeText(DetailActivity.this,"Deleted From Favourites",Toast.LENGTH_SHORT).show();
    }
    public void saveLocalData(final MovieDetails mMovieDetails) {

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.FavMoviesDao().insertMovies(mMovieDetails);
            }
        });

        Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show();
    }

    public List<String> getYoutubeKeys(List<VideoResults> results) {
        int i;
        List<String> keys = new ArrayList<>();
        for (i = 0; i < results.size(); i++) {
            keys.add(results.get(i).getKey());
        }

        return keys;
    }


    @Override
    public void OnYoutubeClicked(String youtubeKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + youtubeKey));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }
}

