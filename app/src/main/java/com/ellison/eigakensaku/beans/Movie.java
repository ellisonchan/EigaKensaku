package com.ellison.eigakensaku.beans;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Movie {
    @SerializedName("Title")
    private String Title;
    @SerializedName("Poster")
    private String Poster;
    @SerializedName("Year")
    private String Year;
    @SerializedName("Type")
    private String Type;
    @SerializedName("imdbID")
    private String ID;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Movie[title:")
                .append(Title)
                .append(", postUrl:")
                .append(Poster)
                .append(", year:")
                .append(Year)
                .append(", type:")
                .append(Type)
                .append(", imdb:")
                .append(ID)
                .append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Movie) {
            Movie temp = (Movie) obj;

            if ((temp.getID() != null && temp.getID().equals(getID()))
                    || (temp.getTitle() != null && temp.getTitle().equals(getTitle())
                            && temp.getPoster() != null && temp.getPoster().equals(getPoster()))) {
                return true;
            }
        }
        return false;
    }
}

