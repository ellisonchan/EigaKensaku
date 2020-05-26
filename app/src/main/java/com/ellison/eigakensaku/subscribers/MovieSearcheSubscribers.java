package com.ellison.eigakensaku.subscribers;

import android.util.Log;

import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.beans.MovieSearchResponse;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.presenter.IMovieListener;

import rx.Subscriber;

public class MovieSearcheSubscribers extends Subscriber<MovieSearchResponse<MovieList>> {
    private final IMovieListener movieListener;
    private final String keywords;
    private final int pageIndex;

    public MovieSearcheSubscribers(IMovieListener movieListener, String keywords, int pageIndex) {
        this.movieListener = movieListener;
        this.keywords = keywords;
        this.pageIndex = pageIndex;
    }

    @Override
    public void onCompleted() {
        Log.i("ellison", "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.e("ellison", "onFailure() stack:", e);
        if (movieListener != null) {
            movieListener.onMovieFailed(keywords, Constants.SEARCH_ERROR_NETWORK, pageIndex);
        }
    }

    @Override
    public void onNext(MovieSearchResponse<MovieList> movieListMovieSearchResponse) {
        Log.d("ellison", "onNext() content:" + movieListMovieSearchResponse);
        if (movieListMovieSearchResponse == null && movieListener != null) {
            Log.e("ellison", "onNext() content NULL");
            movieListener.onMovieFailed(keywords, Constants.SEARCH_ERROR_UNDEFINED, pageIndex);
            return;
        }

        if (movieListMovieSearchResponse.getResponse() !=null) {
            if (!Boolean.valueOf(movieListMovieSearchResponse.getResponse())) {
                if (movieListMovieSearchResponse.getError() != null && !movieListMovieSearchResponse.getError().isEmpty()) {
                    if (Constants.SEARCH_ERROR_RESULT_LARGE.equals(movieListMovieSearchResponse.getError())) {
                        movieListener.onMovieFailed(keywords, Constants.SEARCH_ERROR_RESULT_LARGE_TIP, pageIndex);
                    } else if (Constants.SEARCH_ERROR_RESULT_KEY_INVALID.equals(movieListMovieSearchResponse.getError())) {
                        movieListener.onMovieFailed(keywords, Constants.SEARCH_ERROR_RESULT_KEY_INVALID_TIP, pageIndex);
                    } else if (Constants.SEARCH_ERROR_RESULT_KEY_NONE.equals(movieListMovieSearchResponse.getError())) {
                        movieListener.onMovieFailed(keywords, Constants.SEARCH_ERROR_RESULT_KEY_NONE_TIP, pageIndex);
                    } else {
                        movieListener.onMovieFailed(keywords, movieListMovieSearchResponse.getError(), pageIndex);
                    }
                    return;
                }
            } else if (movieListMovieSearchResponse.getSearch() != null){
                movieListener.onMovieSucceed(keywords, movieListMovieSearchResponse.getSearch(), pageIndex);
                return;
            }
        }
        movieListener.onMovieFailed(keywords, Constants.SEARCH_ERROR_UNDEFINED, pageIndex);
    }
}
