package com.ellison.eigakensaku.http;

import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.beans.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetRequest_Interface {
    @GET("http://omdbapi.com/")
     Call<MovieSearchResponse<MovieList>> getCall(@Query("s")String keywords, @Query("apikey")String apikey);
//    Call<ResponseBody> getCall(@Query("s")String keywords, @Query("apikey")String apikey);

}
