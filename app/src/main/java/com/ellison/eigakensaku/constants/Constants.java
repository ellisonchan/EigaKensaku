package com.ellison.eigakensaku.constants;

public class Constants {
    public static final String OMDB_URL = "http://omdbapi.com/";
    // public static final String OMDB_API_KEY = "19b0bce5";
    public static final String OMDB_API_KEY = "9a5d98e2";

    public static final String OMDB_API_PARAM_KEY = "apikey";
    public static final String OMDB_API_PARAM_KEYWORDS = "s";
    public static final String OMDB_API_PARAM_PAGE = "page";

    public static final int MOVIE_LIST_ROW_NUMBER = 2;
    public static final int MOVIE_LIST_ITEM_SPACE = 50;

    public static final String SEARCH_ERROR_RESULT_LARGE = "Too many results.";
    public static final String SEARCH_ERROR_RESULT_LARGE_TIP = "Too many results!\nTry entering some more specific keywords please!";
    public static final String SEARCH_ERROR_RESULT_KEY_INVALID = "Invalid API key!";
    public static final String SEARCH_ERROR_RESULT_KEY_INVALID_TIP = "Unavailable.\nContact the developer and check their access permission!";
    public static final String SEARCH_ERROR_RESULT_KEY_NONE = "Movie not found!";
    public static final String SEARCH_ERROR_RESULT_KEY_NONE_TIP = "Movie not found!\nTry entering some other keywords please!";

    public static final String SEARCH_ERROR_UNDEFINED = "Unknown error!\nTry later!";

    public static final String SEARCH_ERROR_NETWORK = "Network problem.\nCheck your wi-fi or phone please!";

    public static final int TYPE_RV_CONTENT = 1;
    public static final int TYPE_RV_LOAD = 2;

    public static final int GET_REQUEST_NO_PAGE_INDEX = 0;
}