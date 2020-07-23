package com.ellison.eigakensaku.presenter;

import android.content.Context;

import com.ellison.eigakensaku.beans.Movie;

import java.util.List;

public interface IMoviePresenter {
    void searchMovie(String keywords, int pageIndex);
    void starMovie(Movie movie, boolean isStarred, Context context);
    boolean isMovieStarred(Movie movie, Context context);
    List<Movie> getStarredMovie(Context context);
}
