package com.ellison.eigakensaku.presenter;

import com.ellison.eigakensaku.model.IMovieModel;
import com.ellison.eigakensaku.model.MovieModel;
import com.ellison.eigakensaku.view.IMovieView;
import com.ellison.eigakensaku.beans.MovieList;

public class MoviePresenter implements IMoviePresenter, IMovieListener {
    private final IMovieModel mMovieModel;
    private final IMovieView mMovieView;

    public MoviePresenter(IMovieView movieView) {
        mMovieModel = new MovieModel(this);
        mMovieView = movieView;
    }

    @Override
    public void searchMovie(String keywords) {
        if (mMovieModel != null) {
            if (mMovieView != null) {
                mMovieView.showProgress();
            }
            mMovieModel.searchMovie(keywords);
        }
    }

    @Override
    public void onMovieSucceed(String keywords, MovieList list) {
        if (mMovieView != null) {
            mMovieView.hideProgress();
            mMovieView.showResult(list);
        }
    }

    @Override
    public void onMovieFailed(String keywords, String errorInfo) {
        if (mMovieView != null) {
            mMovieView.hideProgress();
            mMovieView.showFailed(errorInfo);
        }
    }
}
