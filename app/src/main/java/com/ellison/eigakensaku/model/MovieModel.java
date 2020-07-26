package com.ellison.eigakensaku.model;

import android.content.Context;
import android.util.Log;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.http.HttpManager;
import com.ellison.eigakensaku.http.IMovieRequestCallback;
import com.ellison.eigakensaku.presenter.IMovieListener;
import com.ellison.eigakensaku.star.StarSystem;
import com.ellison.eigakensaku.subscribers.MovieSearcheSubscribers;
import com.ellison.eigakensaku.utils.Utils;

import java.util.List;

public class MovieModel implements IMovieModel, IMovieRequestCallback {
    private static final String TAG = MovieModel.class.getSimpleName();
    private final IMovieListener mMovieListener;

    public MovieModel() {
        mMovieListener = null;
    }

    public MovieModel(IMovieListener movieListener) {
        mMovieListener = movieListener;
    }

    @Override
    public void searchMovie(String keywords, int pageIndex) {
        Utils.logDebug(TAG, "searchMovie() keywords:" + keywords + " pageIndex:" + pageIndex);
        HttpManager.getInstance().requestMovieList(keywords, pageIndex, new MovieSearcheSubscribers(mMovieListener, keywords, pageIndex));
        // HttpManager.getInstance().requestMovieList(keywords, pageIndex, this);

        // Make fake list instead of http get.
        // mMovieListener.onMovieSucceed(keywords, Utils.makeFakeList(), pageIndex);
    }

    @Override
    public void starMovie(Movie movie, boolean isStarred, Context context) {
        Utils.logDebug(TAG, "starMovie movie:" + movie + " isStarred:" + isStarred);
        StarSystem.getInstance(context).starMoive(movie, isStarred);
    }

    @Override
    public boolean isMovieStarred(Movie movie, Context context) {
        Utils.logDebug(TAG, "isMovieStarred movie:" + movie);
        return StarSystem.getInstance(context).isMoiveStarred(movie);
    }

    @Override
    public List<Movie> getStarredMovie(Context context) {
        Utils.logDebug(TAG, "getStarredMovie");
        return StarSystem.getInstance(context).getStarredMovie();
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