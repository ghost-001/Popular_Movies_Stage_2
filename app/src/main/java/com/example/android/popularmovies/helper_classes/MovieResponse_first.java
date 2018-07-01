package com.example.android.popularmovies.helper_classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse_first {
    @SerializedName("page")
private Integer page;
    @SerializedName("results")
private List<Movie> results;
    @SerializedName("total_results")
private Integer total_results;
    @SerializedName("total_pages")
private Integer total_pages;

    public MovieResponse_first(Integer page, List<Movie> reults, Integer total_results, Integer total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getReults() {
        return results;
    }

    public void setReults(List<Movie> results) {
        this.results = results;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }
}
