package com.ellison.eigakensaku.constants;

public class Constants {
    public static final boolean DEBUGGABLE = true;

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

    public static final int COLOR_TINT_FAB_DISABLE = 0xff424242;
    public static final int COLOR_TINT_FAB_ENABLE = 0xff03dac5;
//    public static final int COLOR_TINT_FAB_DISABLE = -12434878;
//    public static final int COLOR_TINT_FAB_ENABLE = -16524603;

    public static final int ALPHA_FAB_DISABLE = 100;
    public static final int ALPHA_FAB_ENABLE = 255;

    public static final int DURATION_FAB_DISABLE = 400;
    public static final int DURATION_FAB_ENABLE = 1000;
    public static final int DURATION_FAB_CLICKED = 1000;

    public static final float SCALE_FAB_NORMAL = 1.0f;
    public static final float SCALE_FAB_CLICKABLE = 1.03f;
    public static final float SCALE_FAB_CLICKED = 1.1f;

    public static final String BUNDLE_KEY_MOVIES_SAVED_LIST = "movie_saved_list";
    public static final String MOVIES_SAVED_SERIALED_FILE = "movie_saved";
}