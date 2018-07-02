package com.example.android.popularmovies.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.api_manage.Movie_api;
import com.example.android.popularmovies.api_manage.RetrofitInterface;
import com.example.android.popularmovies.helper_classes.Movie;
import com.example.android.popularmovies.helper_classes.MovieResponse_first;
import com.example.android.popularmovies.utility.OnPosterListner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnPosterListner {

    private String API_KEY;
    private String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private MoviesAdapter mMoviesAdapter;
    private int mpage = 1;

    private RetrofitInterface retrofit_interface;
    private List<Movie> movies = null;

    private Boolean isLoading = true;
    private Integer pastVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private int view_threshold = 20;
    private Context context;
    // category = 1 will give top rated movies
    // category = 0  will give popular movies
    private Integer category = 1;
    private SharedPreferences sharedPref;
    private ImageView imageNoInternet;
    private TextView textNoInternet;
    private TextView mtextRetry;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageNoInternet = findViewById(R.id.main_no_internet);
        textNoInternet = findViewById(R.id.main_no_internet_tv);
        mtextRetry = findViewById(R.id.main_retry);
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mProgressBar = findViewById(R.id.main_progress);
        mProgressBar.setVisibility(View.VISIBLE);

        if (checkInternet()) {
            getPref();
            startMovies();
        } else {
            mProgressBar.setVisibility(View.GONE);
            imageNoInternet.setVisibility(View.VISIBLE);
            textNoInternet.setVisibility(View.VISIBLE);
            mtextRetry.setVisibility(View.VISIBLE);
            mtextRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Boolean b = checkInternet();
                    if (b) {
                        startMovies();
                    }
                    Toast.makeText(MainActivity.this, "Loading . . .", Toast.LENGTH_SHORT).show();
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

    public void getPref() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        category = sharedPref.getInt(getString(R.string.CATEGORY_SELECTED), 1);
    }

    public void startMovies() {
        imageNoInternet.setVisibility(View.GONE);
        textNoInternet.setVisibility(View.GONE);
        mtextRetry.setVisibility(View.GONE);
        API_KEY = getResources().getString(R.string.API_KEY);
        context = this;


        Integer span = 2;
        if (getResources().getConfiguration().orientation == 2) {
            span = 4;
        }
        mLayoutManager = new GridLayoutManager(this, span);
        mRecyclerView = findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(mLayoutManager);

        retrofit_interface = Movie_api.getClient().create(RetrofitInterface.class);


        load_Movies();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItem + view_threshold)) {
                    ++mpage;
                    load_Movies();
                    isLoading = false;
                }
            }


        });

    }

    public void load_Movies() {
        mProgressBar.setVisibility(View.VISIBLE);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.CATEGORY_SELECTED), category);
        editor.apply();
        Call<MovieResponse_first> call;
        if (category == 1) {
            call = retrofit_interface.getTopRatedMovies(API_KEY, mpage);
        } else {
            call = retrofit_interface.getPopularMovies(API_KEY, mpage);
        }

        call.enqueue(new Callback<MovieResponse_first>() {

            @Override
            public void onResponse(Call<MovieResponse_first> call, Response<MovieResponse_first> response) {
                if (response.isSuccessful()) {
                    if (movies == null) {
                        movies = response.body().getReults();
                        mMoviesAdapter = new MoviesAdapter(movies, context);
                        mRecyclerView.setAdapter(mMoviesAdapter);
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        movies = response.body().getReults();
                        mMoviesAdapter.addMovies(movies);
                        mProgressBar.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<MovieResponse_first> call, Throwable t) {
                Log.e(TAG, t.toString());
            }

        });

    }


    @Override
    public void OnPosterclicked(ImageView mm, Integer movie_id, String movie_name, String poster_url) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie_id", movie_id);
        intent.putExtra("movie_name", movie_name);
        intent.putExtra("poster_url", poster_url);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MainActivity.this,
                        mm,
                        getString(R.string.poster_transition));
        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.main_menu_bookmarks) {
            Intent intent = new Intent(this, BookmarksActivity.class);
            startActivity(intent);
            return true;
        } else if (checkInternet()) {
            movies = null;
            mpage = 1;
            mMoviesAdapter = new MoviesAdapter(movies, this);
            switch (item.getItemId()) {

                case R.id.main_menu_top_rated:
                    category = 1;

                    startMovies();
                    Toast.makeText(this, "Top Rated Movies", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.main_menu_popular:
                    category = 0;

                    startMovies();
                    Toast.makeText(this, "Popular Movies", Toast.LENGTH_SHORT).show();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.create();
            builder.show();
            return true;
        }
    }
}
