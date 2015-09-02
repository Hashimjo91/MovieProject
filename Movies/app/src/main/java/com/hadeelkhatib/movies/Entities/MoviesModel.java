package com.hadeelkhatib.movies.Entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Hadeel on 4/16/2015.
 */


public class MoviesModel extends Entity implements Serializable,Parcelable {


    private String id;
    private String title;
    private String poster_path;
    private String release_date;
    private String overview;
    private String vote_average;
    private String faved;

    public MoviesModel() {
    }

    public MoviesModel(String id, String title, String poster_path, String release_date, String overview,String vote_average) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.overview = overview;
        this.vote_average = vote_average;
        faved="";
    }


    public static final Creator<MoviesModel> CREATOR = new Creator<MoviesModel>() {
        @Override
        public MoviesModel createFromParcel(Parcel in) {
            return new MoviesModel(in);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getFaved() {
        return faved;
    }

    public void setFaved(String faved) {
        this.faved = faved;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }




    protected MoviesModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        overview = in.readString();
        vote_average = in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(overview);
        dest.writeString(vote_average);
    }
}