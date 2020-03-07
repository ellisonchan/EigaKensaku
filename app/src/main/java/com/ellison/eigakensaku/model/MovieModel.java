package com.ellison.eigakensaku.model;

import android.util.Log;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.http.HttpManager;
import com.ellison.eigakensaku.http.IMovieRequestCallback;
import com.ellison.eigakensaku.presenter.IMovieListener;

public class MovieModel implements IMovieModel, IMovieRequestCallback {
    private final IMovieListener mMovieListener;

    public MovieModel(IMovieListener movieListener) {
        mMovieListener = movieListener;
    }

    @Override
    public void searchMovie(String keywords) {
        Log.e("ellison", "MovieModel#searchMovie() current thread:" + Thread.currentThread());
        HttpManager.getInstance().requestMovieList(keywords, Constants.OMDB_URL, this);
    }

    @Override
    public void onRequestSucceed(String keywords, MovieList list) {
        if (mMovieListener != null) {
            mMovieListener.onMovieSucceed(keywords, list);
        }
    }

    @Override
    public void onRequestFailed(String keywords, String errorInfo) {
        if (mMovieListener != null) {
            mMovieListener.onMovieFailed(keywords, errorInfo);
        }
    }
}
