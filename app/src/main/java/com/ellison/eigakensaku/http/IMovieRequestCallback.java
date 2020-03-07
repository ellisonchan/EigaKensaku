package com.ellison.eigakensaku.http;

import com.ellison.eigakensaku.beans.MovieList;

public interface IMovieRequestCallback {
    void onRequestSucceed(String keywords, MovieList list);
    void onRequestFailed(String keywords, String errorInfo);
}
