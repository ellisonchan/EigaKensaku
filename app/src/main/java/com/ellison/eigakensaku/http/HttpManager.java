package com.ellison.eigakensaku.http;

import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.beans.MovieSearchResponse;
import com.ellison.eigakensaku.constants.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpManager {
    private static HttpManager mInstance;
    private final Retrofit retrofit;
    private final MovieRequestService movieInterface;

    private HttpManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.OMDB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        movieInterface = retrofit.create(MovieRequestService.class);
    }

    public static HttpManager getInstance() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    public void requestMovieList(final String keywords, int pageIndex, Subscriber<MovieSearchResponse<MovieList>> subscriber) {
        requestSearch(keywords, pageIndex, subscriber);
    }

    private void requestSearch(String keywords, int pageIndex, Subscriber<MovieSearchResponse<MovieList>> subscriber) {
        Observable<MovieSearchResponse<MovieList>> observable;
        if (pageIndex <= Constants.GET_REQUEST_NO_PAGE_INDEX) {
            observable = movieInterface.getMovie(keywords, Constants.OMDB_API_KEY);
        } else {
            observable = movieInterface.getMovie(keywords, pageIndex, Constants.OMDB_API_KEY);
        }

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}