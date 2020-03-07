package com.ellison.eigakensaku.beans;

import androidx.annotation.NonNull;

public class MovieSearchResponse<T> {
    private int totalResults;
    private String Response;
    private String Error;
    private T Search;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        this.Response = response;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        this.Error = error;
    }

    public T getSearch() {
        return Search;
    }

    public void setSearch(T search) {
        Search = search;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Result(totalResults:" + totalResults + ";Response:" + Response + ";Error:" + Error + ";List:" + Search);
        return sb.toString();
    }

    //    private String search;
//
//    private String title;
//    private String year;
//    private String typer;
//    private String poster;

}
