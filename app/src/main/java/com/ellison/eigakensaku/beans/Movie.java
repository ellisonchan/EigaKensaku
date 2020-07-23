package com.ellison.eigakensaku.beans;

import com.ellison.eigakensaku.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Movie implements Serializable {
    private static final String TAG = Movie.class.getSimpleName();
    private static final long serialVersionUID = 7897981L;

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
        return new StringBuilder(300)
                .append("Movie@" + Integer.toHexString(super.hashCode()))
                .append("[title:")
                .append(Title)
                .append(", postUrl:")
                .append(Poster)
                .append(", year:")
                .append(Year)
                .append(", type:")
                .append(Type)
                .append(", imdb:")
                .append(ID)
                .append("]")
                .toString();
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Utils.logDebug(TAG, "equals this:" + this + " target:" + obj);

        if (this == obj) {
            Utils.logDebug(TAG, "equals ==");
            return true;
        }

        if (obj instanceof Movie) {
            Movie temp = (Movie) obj;

            if (temp.getID() != null && temp.getID().equals(getID())) {
                Utils.logDebug(TAG, "equals equals");
//                    || (temp.getTitle() != null && temp.getTitle().equals(getTitle())
//                            && temp.getPoster() != null && temp.getPoster().equals(getPoster()))) {
                return true;
            }
        }
        return false;
    }
}

