package com.ellison.eigakensaku.presenter;

import com.ellison.eigakensaku.beans.MovieList;

public interface IMovieListener {
    void onMovieSucceed(String keywords, MovieList list, int pageIndex);
    void onMovieFailed(String keywords, String errorInfo, int pageIndex);
}
