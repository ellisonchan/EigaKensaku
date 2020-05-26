package com.ellison.eigakensaku.presenter;

import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.model.IMovieModel;
import com.ellison.eigakensaku.model.MovieModel;
import com.ellison.eigakensaku.ui.view.IMovieView;
import com.ellison.eigakensaku.beans.MovieList;

public class MoviePresenter implements IMoviePresenter, IMovieListener {
    private final IMovieModel mMovieModel;
    private final IMovieView mMovieView;

    public MoviePresenter(IMovieView movieView) {
        mMovieModel = new MovieModel(this);
        mMovieView = movieView;
    }

    @Override
    public void searchMovie(String keywords, int pageIndex) {
        if (mMovieModel != null) {
            if (mMovieView != null && pageIndex <= Constants.GET_REQUEST_NO_PAGE_INDEX + 1) {
                mMovieView.showProgress();
            }
            mMovieModel.searchMovie(keywords, pageIndex);
        }
    }

    @Override
    public void onMovieSucceed(String keywords, MovieList list, int pageIndex) {
        if (mMovieView != null) {
            if (pageIndex <= Constants.GET_REQUEST_NO_PAGE_INDEX + 1) {
                mMovieView.hideProgress();
            }
            mMovieView.showResult(list, pageIndex);
        }
    }

    @Override
    public void onMovieFailed(String keywords, String errorInfo, int pageIndex) {
        if (mMovieView != null) {
            if (pageIndex <= Constants.GET_REQUEST_NO_PAGE_INDEX + 1) {
                mMovieView.hideProgress();
            }
            mMovieView.showFailed(errorInfo, pageIndex);
        }
    }
}
