package com.lulu.tuyou.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lulu on 2016/11/7.
 */

public class TuYouRelation extends BmobObject {
    private TuYouUser fromUser;
    private TuYouUser toUser;
    private String remark;
    private String relationType;

    public TuYouUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(TuYouUser fromUser) {
        this.fromUser = fromUser;
    }

    public TuYouUser getToUser() {
        return toUser;
    }

    public void setToUser(TuYouUser toUser) {
        this.toUser = toUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
