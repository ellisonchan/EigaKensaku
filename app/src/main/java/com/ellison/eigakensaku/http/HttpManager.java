package com.ellison.eigakensaku.http;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.beans.MovieSearchResponse;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.utils.Utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static HttpManager mInstance;
    private Handler mHandler;

    private HttpManager() {
        // mHandler = new Handler(Looper.getMainLooper());
        mHandler = new Handler();
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

    private void testFail(final String keywords, final int pageIndex,  final IMovieRequestCallback callback) {
        if (callback != null) {
            callback.onRequestFailed(keywords,"Search failed, try again please!", pageIndex);
        }
    }

    private void testSucceed(final String keywords, int pageIndex, final IMovieRequestCallback callback) {
        if (callback != null) {
            callback.onRequestSucceed(keywords, Utils.makeFakeList(), pageIndex);
        }
    }

    public void requestMovieList(final String keywords, int pageIndex, final String url, final IMovieRequestCallback callback) {
        requestSearch(keywords, pageIndex, url, callback);
//        new Thread(){
//            @Override
//            public void run() {
//                Log.e("ellison", "HttpManager#requestMovieList() current thread:" + Thread.currentThread());
//                requestSearch(keywords, url, callback);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // testSucceed(keywords, pageIndex, callback);
//                    }
//                });
//            }
//        }.start();
    }

    private void requestSearch(String keywords, int pageIndex, String url, final IMovieRequestCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        Call<MovieSearchResponse<MovieList>> call;
        if (pageIndex <= Constants.GET_REQUEST_NO_PAGE_INDEX) {
            call = request.getCall(keywords, Constants.OMDB_API_KEY);
        } else {
            call = request.getCall(keywords, pageIndex, Constants.OMDB_API_KEY);
        }

        call.enqueue(new Callback<MovieSearchResponse<MovieList>>() {
            @Override
            public void onResponse(Call<MovieSearchResponse<MovieList>> call, Response<MovieSearchResponse<MovieList>> response) {
                if (response == null || response.body() == null || callback == null) {
                    return;
                }

                MovieSearchResponse<MovieList> result = (MovieSearchResponse<MovieList>) response.body();
                Log.e("ellison", "onResponse() content:" + result);

                if (result.getResponse() !=null) {
                    if (!Boolean.valueOf(result.getResponse())) {
                        if (result.getError() != null && !result.getError().isEmpty()) {
                            if (Constants.SEARCH_ERROR_RESULT_LARGE.equals(result.getError())) {
                                callback.onRequestFailed(keywords, Constants.SEARCH_ERROR_RESULT_LARGE_TIP, pageIndex);
                            } else if (Constants.SEARCH_ERROR_RESULT_KEY_INVALID.equals(result.getError())) {
                                callback.onRequestFailed(keywords, Constants.SEARCH_ERROR_RESULT_KEY_INVALID_TIP, pageIndex);
                            } else if (Constants.SEARCH_ERROR_RESULT_KEY_NONE.equals(result.getError())) {
                                callback.onRequestFailed(keywords, Constants.SEARCH_ERROR_RESULT_KEY_NONE_TIP, pageIndex);
                            } else {
                                callback.onRequestFailed(keywords, result.getError(), pageIndex);
                            }
                            return;
                        }
                    } else if (result.getSearch() != null){
                        callback.onRequestSucceed(keywords, result.getSearch(), pageIndex);
                        return;
                    }
                }

                callback.onRequestFailed(keywords, Constants.SEARCH_ERROR_UNDEFINED, pageIndex);
            }

            @Override
            public void onFailure(Call<MovieSearchResponse<MovieList>> call, Throwable t) {
                Log.e("ellison", "onFailure() stack:", t);
                if (callback != null) {
                    // callback.onRequestFailed(keywords, t.getMessage());
                    callback.onRequestFailed(keywords, Constants.SEARCH_ERROR_NETWORK, pageIndex);
                }
            }
        });
    }
}
