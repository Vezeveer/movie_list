package com.bignerdranch.android.movielist;


import java.util.Date;
import java.util.UUID;

public class ModelMovie {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mWatched;

    public ModelMovie(){
        this(UUID.randomUUID());
    }

    public ModelMovie(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismWatched() {
        return mWatched;
    }

    public void setmWatched(boolean mWatched) {
        this.mWatched = mWatched;
    }

}
