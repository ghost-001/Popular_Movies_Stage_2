package com.example.android.popularmovies.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.GenresAdapter;
import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.api_manage.Movie_api;
import com.example.android.popularmovies.api_manage.RetrofitInterface;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.helper_classes.Credits;
import com.example.android.popularmovies.helper_classes.Genre;
import com.example.android.popularmovies.helper_classes.MovieDetails;
import com.example.android.popularmovies.helper_classes.ReviewResults;
import com.example.android.popularmovies.helper_classes.Reviews;
import com.example.android.popularmovies.helper_classes.VideoResults;
import com.example.android.popularmovies.helper_classes.Videos;
import com.example.android.popularmovies.utility.AppExecutors;
import com.example.android.popularmovies.utility.OnYoutubeClickListener;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements OnYoutubeClickListener {
    private final String getVideos = "videos,reviews,credits";
    private String API_KEY;
    private String TAG = "DetailActivity";
    private Integer color = Color.parseColor("#000000");
    private int dark_muted_color;
    private int muted_color;
    private int movie_id;
    private String movie_name;
    private String backdrop_url;
    private ImageView toolbar_image;
    private ImageView mPoster_image;
    private TextView mMovie_name;
    private TextView mMovie_Rating;
    private TextView mMovie_Runtime;
    private TextView mMovie_Genre;
    private TextView mMovie_release;
    private TextView mMovie_synopsis;
    private ProgressBar mProgressBar;
    private RelativeLayout mLayout_rating, mLayout_runtime, mLayout_release, mLayout_card, mLayout_trailer;
    private RelativeLayout mLayout_reviews;
    private ImageView mReview_image;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private NestedScrollView mNested;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mGenreRecyclerView, mReviewRecylerView;
    private FloatingActionButton mActionButton;
    private AppDatabase mAppDatabase;
    private RetrofitInterface retrofitInterface;
    private MovieDetails mMovieDetails;
    private Boolean REVIEWS_SHOW = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
            API_KEY = getResources().getString(R.string.API_KEY);
            movie_id = getIntent().getIntExtra("movie_id", 0);
            movie_name = getIntent().getStringExtra("movie_name");
            backdrop_url = getIntent().getStringExtra("poster_url");
            retrofitInterface = Movie_api.getClient().create(RetrofitInterface.class);
            initViews();
            mProgressBar.setVisibility(View.VISIBLE);
            loadImageColorInToolbar();
            loadMovieDetails();

            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset == -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight()) {
                        mCollapsingToolbarLayout.setTitle(movie_name);
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

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        mToolbar = findViewById(R.id.detail_toolbar);
        mNested = findViewById(R.id.detail_nested);
        mAppBarLayout = findViewById(R.id.detail_app_bar_layout);
        mCollapsingToolbarLayout = findViewById(R.id.detail_collapseToolbarLayout);
        setSupportActionBar(mToolbar);

        mProgressBar = findViewById(R.id.detail_progress);
        toolbar_image = findViewById(R.id.detail_collapse_iv);


        mGenreRecyclerView = findViewById(R.id.detail_genre_recycler);
        mReviewRecylerView = findViewById(R.id.detail_reviews_recycler);
        mLayout_rating = findViewById(R.id.detail_relative_rating);
        mLayout_runtime = findViewById(R.id.detail_relative_runtime);
        mLayout_release = findViewById(R.id.detail_relative_release);
        mLayout_card = findViewById(R.id.detail_card_relative);
        mLayout_trailer = findViewById(R.id.detail_trailer_relative);
        mLayout_reviews = findViewById(R.id.detail_reviews_relative);
        mMovie_Genre = findViewById(R.id.detail_genre);


        mPoster_image = findViewById(R.id.movie_poster);
        mMovie_name = findViewById(R.id.detail_movie_name);
        mMovie_Rating = findViewById(R.id.detail_rating_tv);
        mMovie_Runtime = findViewById(R.id.detail_runtime_tv);
        mMovie_release = findViewById(R.id.detail_release_tv);
        mMovie_synopsis = findViewById(R.id.detail_synopsis_tv);
        mReview_image = findViewById(R.id.detail_review_image);

        mActionButton = findViewById(R.id.detail_action);


    }

    public void loadImageColorInToolbar() {

        Picasso.get().load(backdrop_url)
                .placeholder(R.drawable.photo)
                .into(new Target() {


                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                              toolbar_image.setImageBitmap(bitmap);
                              Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                  @Override
                                  public void onGenerated(@NonNull Palette palette) {
                                      Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                      if (textSwatch != null) {
                                          color = textSwatch.getRgb();
                                      }
                                      dark_muted_color = palette.getDarkMutedColor(color);
                                      muted_color = palette.getMutedColor(color);
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
        mCollapsingToolbarLayout.setStatusBarScrimColor(dark_muted_color);
        mCollapsingToolbarLayout.setContentScrimColor(dark_muted_color);

        mNested.setBackgroundColor(muted_color);

        final GradientDrawable gradientDrawable_name = (GradientDrawable) mMovie_name.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_rating = (GradientDrawable) mLayout_rating.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_runtime = (GradientDrawable) mLayout_runtime.getBackground().mutate();
        final GradientDrawable gradientDrawable_genre = (GradientDrawable) mMovie_Genre.getBackground().mutate();
        final GradientDrawable gradientDrawable_genre_recycler = (GradientDrawable) mGenreRecyclerView.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_release = (GradientDrawable) mLayout_release.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_card = (GradientDrawable) mLayout_card.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_trailer = (GradientDrawable) mLayout_trailer.getBackground().mutate();
        final GradientDrawable gradientDrawable_relative_review = (GradientDrawable) mLayout_reviews.getBackground().mutate();

        gradientDrawable_name.setColor(dark_muted_color);
        gradientDrawable_relative_rating.setColor(dark_muted_color);
        gradientDrawable_relative_runtime.setColor(dark_muted_color);
        gradientDrawable_genre.setColor(dark_muted_color);
        gradientDrawable_genre_recycler.setColor(dark_muted_color);
        gradientDrawable_relative_release.setColor(dark_muted_color);
        gradientDrawable_relative_card.setColor(dark_muted_color);
        gradientDrawable_relative_trailer.setColor(dark_muted_color);
        gradientDrawable_relative_review.setColor(dark_muted_color);
    }

    public void loadMovieDetails() {
        Call<MovieDetails> call = retrofitInterface.getMovieDetails(movie_id, API_KEY, getVideos);
        call.enqueue((new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful()) {
                    mMovieDetails = response.body();
                    initViewsWithData();
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {

            }
        }));
    }

    public void initViewsWithData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
        mProgressBar.setVisibility(View.GONE);

        Picasso.get().load(BASE_IMAGE_URL + mMovieDetails.getPosterPath())
                .placeholder(R.drawable.photo)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mPoster_image.setImageBitmap(bitmap);
                        mMovieDetails.setImage_poster(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        mMovie_name.setText(mMovieDetails.getTitle());
        mMovie_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mMovie_name.setSelected(true);
                }
            }
        });


        mMovie_Rating.setText(String.format(Locale.ENGLISH, "%1$,.2f", mMovieDetails.getVoteAverage()));

        List<Genre> mGenre = mMovieDetails.getGenres();
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mGenreRecyclerView.setLayoutManager(mLayoutManager);
        GenresAdapter mGenresAdapter = new GenresAdapter(mGenre);
        mGenreRecyclerView.setAdapter(mGenresAdapter);

        mMovie_Runtime.setText(String.format(Locale.ENGLISH, "%d", mMovieDetails.getRuntime()));
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = fmt.parse(mMovieDetails.getReleaseDate());
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            String xx = fmtOut.format(date);
            mMovie_release.setText(xx);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mMovie_synopsis.setText(mMovieDetails.getOverview());
        Credits mCast = mMovieDetails.getCredits();
        Videos mVideos = mMovieDetails.getVideos();


        Reviews mReviews = mMovieDetails.getReviews();
        final List<ReviewResults> reviewResults = mReviews.getResults();
        ReviewsAdapter mReviewsAdapter = new ReviewsAdapter(reviewResults);
        LinearLayoutManager mLayout = new LinearLayoutManager(this);
        mReviewRecylerView.setLayoutManager(mLayout);
        mReviewRecylerView.setAdapter(mReviewsAdapter);

        mLayout_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (REVIEWS_SHOW) {
                    mReview_image.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down, getTheme()));
                    if (!reviewResults.isEmpty()) {
                        mReviewRecylerView.setVisibility(View.VISIBLE);
                        REVIEWS_SHOW = false;
                    } else {
                        TextView tv = findViewById(R.id.detail_no_reviews);
                        tv.setVisibility(View.VISIBLE);
                        REVIEWS_SHOW = false;
                    }
                } else if (!REVIEWS_SHOW) {
                    mReview_image.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up, getTheme()));
                    if (!reviewResults.isEmpty()) {
                        mReviewRecylerView.setVisibility(View.GONE);
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
        mTrailerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocalData();
            }
        });
    }

    public void saveLocalData() {


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.FavMoviesDao().insertMovies(mMovieDetails);
            }
        });
        List<MovieDetails> cc = mAppDatabase.FavMoviesDao().loadAllMovies();
        Toast.makeText(this, "ADDED" + mAppDatabase.FavMoviesDao().loadAllMovies(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ADDED" + cc.size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ADDED", Toast.LENGTH_SHORT).show();
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
    public void OnYoutubeclicked(String youtubeKey) {
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

