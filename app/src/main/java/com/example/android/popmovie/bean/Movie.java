package com.example.android.popmovie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Happy on 2017/2/6.
 * 电影bean
 */

public class Movie implements Parcelable{
    private String mId;
    private String mPoster_path;
    private boolean mAdult;
    private String mOverview;
    private String mRelease_date;
//    private String[] mGenre_ids;
    private String mOriginal_title;
    private String mOriginal_language;
    private String mTitle;
    private String mBackdrop_path;
    private double mPopularity;
    private long mVote_count;
    private boolean mVideo;
    private double mVote_average;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPoster_path() {
        return mPoster_path;
    }

    public void setPoster_path(String poster_path) {
        mPoster_path = poster_path;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean adult) {
        mAdult = adult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public void setRelease_date(String release_date) {
        mRelease_date = release_date;
    }

    public String getOriginal_title() {
        return mOriginal_title;
    }

    public void setOriginal_title(String original_title) {
        mOriginal_title = original_title;
    }

    public String getOriginal_language() {
        return mOriginal_language;
    }

    public void setOriginal_language(String original_language) {
        mOriginal_language = original_language;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdrop_path() {
        return mBackdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        mBackdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public long getVote_count() {
        return mVote_count;
    }

    public void setVote_count(long vote_count) {
        mVote_count = vote_count;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean video) {
        mVideo = video;
    }

    public double getVote_average() {
        return mVote_average;
    }

    public void setVote_average(double vote_average) {
        mVote_average = vote_average;
    }

    public Movie() {
    }

    private Movie(Parcel in){
        mId = in.readString();
        mPoster_path=in.readString();
        mAdult = in.readByte()!=0;
        mOverview=in.readString();
        mRelease_date=in.readString();
        mOriginal_title=in.readString();
        mOriginal_language=in.readString();
        mTitle=in.readString();
        mBackdrop_path=in.readString();
        mPopularity=in.readDouble();
        mVote_count=in.readLong();
        mVideo=in.readByte()!=0;
        mVote_average=in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mPoster_path);
        dest.writeByte((byte) (mAdult ? 1:0));
        dest.writeString(mOverview);
        dest.writeString(mRelease_date);
        dest.writeString(mOriginal_title);
        dest.writeString(mOriginal_language);
        dest.writeString(mTitle);
        dest.writeString(mBackdrop_path);
        dest.writeDouble(mPopularity);
        dest.writeLong(mVote_count);
        dest.writeByte((byte) (mVideo?1:0));
        dest.writeDouble(mVote_average);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };



}
