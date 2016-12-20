package com.lulu.tuyou.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lulu on 2016/10/31.
 */

public class TuYouChat extends BmobObject {
    private String ChatContent;
    private String time;
    private TuYouUser user;

    public String getChatContent() {
        return ChatContent;
    }

    public void setChatContent(String chatContent){
        ChatContent = chatContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TuYouUser getUser() {
        return user;
    }

    public void setUser(TuYouUser user) {
        this.user = user;
    }

}
