package com.ellison.eigakensaku.beans;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;

public class MovieList extends ArrayList<Movie> implements Serializable {
    private static final long serialVersionUID = 7897982L;

    public MovieList() {
        super();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("MovieList[");
        for (Movie movie : this) {
            sb.append(movie);
        }
        sb.append("]");

        return sb.toString();
    }
}
