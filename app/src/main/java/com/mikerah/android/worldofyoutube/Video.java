package com.mikerah.android.worldofyoutube;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Mikerah on 2016-05-04.
 */
public class Video {

    private String mId;
    private String mTitle;
    private String mChannel;
    private String mDuration;
    private Date mUploadDate;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        mChannel = channel;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public Date getUploadDate() {
        return mUploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        mUploadDate = uploadDate;
    }

    public Uri getVideoUri() {
        return Uri.parse("http://www.youtube.com/watch?v=" + getId());
    }
}
