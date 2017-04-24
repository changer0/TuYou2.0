package com.lulu.tuyou.model;

import android.support.annotation.NonNull;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by lulu on 16/10/31.
 *
 */

public class TuYouTrack extends BmobObject implements Comparable<TuYouTrack>{
    public final static Integer TYPE_IMAGE = 1;
    public final static Integer TYPE_VIDEO = 2;
    public final static Integer TYPE_TEXT = 3;
    private TuYouUser user;
    private String text;//内容
    private List<String> images;//图片
    private String video;//视频
    private Integer type;//布局类型
    private String version = "0";//当前版本号


    public void setUpdatedAt(String updateTime) {
        super.setUpdatedAt(updateTime);
    }

    public static Integer getTypeVideo() {
        return TYPE_VIDEO;
    }

    public TuYouUser getUser() {
        return user;
    }

    public void setUser(TuYouUser user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVersionId() {
        return version;
    }

    public void setVersionId(String versionId) {
        this.version = versionId;
    }

    //var1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(createdAt).getTime();
    // < -
    // > +
    @Override
    public int compareTo(@NonNull TuYouTrack o) {
        long otherUpdate = BmobDate.getTimeStamp(o.getUpdatedAt());
        long curUpdate = BmobDate.getTimeStamp(getUpdatedAt());
        return (int) (otherUpdate - curUpdate);
    }
}
