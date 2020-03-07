package com.ellison.eigakensaku.view;

import com.ellison.eigakensaku.beans.MovieList;

public interface IMovieView {
    void showProgress();
    void hideProgress();
    void showResult(MovieList list);
    void showFailed(String errorMsg);
}
