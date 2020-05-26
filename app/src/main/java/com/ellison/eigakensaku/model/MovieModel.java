package com.ellison.eigakensaku.model;

import android.util.Log;

import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.http.HttpManager;
import com.ellison.eigakensaku.http.IMovieRequestCallback;
import com.ellison.eigakensaku.presenter.IMovieListener;
import com.ellison.eigakensaku.subscribers.MovieSearcheSubscribers;

public class MovieModel implements IMovieModel, IMovieRequestCallback {
    private final IMovieListener mMovieListener;

    public MovieModel(IMovieListener movieListener) {
        mMovieListener = movieListener;
    }

    @Override
    public void searchMovie(String keywords, int pageIndex) {
        Log.d("ellison", "searchMovie() keywords:" + keywords + " pageIndex:" + pageIndex);
        HttpManager.getInstance().requestMovieList(keywords, pageIndex, new MovieSearcheSubscribers(mMovieListener, keywords, pageIndex));
        // HttpManager.getInstance().requestMovieList(keywords, pageIndex, this);
    }

    @Override
    public void onRequestSucceed(String keywords, MovieList list, int pageIndex) {
        if (mMovieListener != null) {
            mMovieListener.onMovieSucceed(keywords, list, pageIndex);
        }
    }

    @Override
    public void onRequestFailed(String keywords, String errorInfo, int pageIndex) {
        if (mMovieListener != null) {
            mMovieListener.onMovieFailed(keywords, errorInfo, pageIndex);
        }
    }
}