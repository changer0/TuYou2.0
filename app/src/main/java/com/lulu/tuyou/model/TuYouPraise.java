package com.lulu.tuyou.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lulu on 2016/10/31.
 * 点赞
 */
public class TuYouPraise extends BmobObject {

    private String mTrackId;
    private String mFromUserId;

    public String getTrackId() {
        return mTrackId;
    }

    public void setTrackId(String trackId) {
        mTrackId = trackId;
    }

    public String getFromUserId() {
        return mFromUserId;
    }

    public void setFromUserId(String fromUserId) {
        mFromUserId = fromUserId;
    }
}
