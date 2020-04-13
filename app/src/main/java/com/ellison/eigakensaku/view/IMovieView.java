package com.ellison.eigakensaku.view;

import com.ellison.eigakensaku.beans.MovieList;

public interface IMovieView {
    void showProgress();
    void hideProgress();
    void showResult(MovieList list, int pageIndex);
    void showFailed(String errorMsg, int pageIndex);
}
