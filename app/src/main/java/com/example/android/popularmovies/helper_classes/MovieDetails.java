
package com.example.android.popularmovies.helper_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.android.popularmovies.database.GenreConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "FavMovies")
public class MovieDetails {



    @PrimaryKey(autoGenerate = true)
    private Integer movie_id;


    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public Bitmap getImage_poster() {
        String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

        Picasso.get().load(BASE_IMAGE_URL + posterPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image_poster = bitmap;
                        Log.d("BITMAP","CONVERTED BITMAP");
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.d("BITMAP","CONVERTED  FAILED BITMAP");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        return image_poster;
    }

    public void setImage_poster(Bitmap image_poster) {

        this.image_poster = image_poster;
    }

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)

    private Bitmap image_poster;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    private String overview;
    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    @Expose
    private Integer runtime;

    @ColumnInfo(name = "genre")
    @TypeConverters(GenreConverter.class)
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = new ArrayList<>();

    @Ignore
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @Ignore
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @Ignore
    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @Ignore
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @Ignore
    @SerializedName("belongs_to_collection")
    @Expose
    private Object belongsToCollection;

    @Ignore
    @SerializedName("budget")
    @Expose
    private Integer budget;

    @Ignore
    @SerializedName("homepage")
    @Expose
    private String homepage;

    @Ignore
    @SerializedName("id")
    @Expose
    private Integer id;

    @Ignore
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @Ignore
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @Ignore
    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @Ignore
    @SerializedName("revenue")
    @Expose
    private Integer revenue;

    @Ignore
    @SerializedName("status")
    @Expose
    private String status;

    @Ignore
    @SerializedName("tagline")
    @Expose
    private String tagline;

    @Ignore
    @SerializedName("video")
    @Expose
    private Boolean video;




    @Ignore
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @Ignore
    @SerializedName("videos")
    @Expose
    private Videos videos;

    @Ignore
    @SerializedName("reviews")
    @Expose
    private Reviews reviews;

    @Ignore
    @SerializedName("credits")
    @Expose
    private Credits credits;

    @Ignore
    public MovieDetails(String title, Integer runtime,
                        String releaseDate, List<Genre>genres,
                        Double voteAverage, String overview){
        this.title = title;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public MovieDetails(int movie_id, String title, Integer runtime,
                        String releaseDate, List<Genre>genres,
                        Double voteAverage,String overview){
       this.movie_id = movie_id;
        this.title = title;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.voteAverage = voteAverage;
        this.overview = overview;

    }


    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Object getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(Object belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public Credits getCredits() {
        return credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

}



