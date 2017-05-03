package com.lulu.tuyou.model;

import com.yzs.imageshowpickerview.ImageShowPickerBean;

/**
 * Created by zhanglulu on 2017/5/3.
 * 用于发送图友圈的Bean
 */

public class CircleImageShowBean extends ImageShowPickerBean {
    private String url;
    private int resId;

    public CircleImageShowBean(String url) {
        this.url = url;
    }

    public CircleImageShowBean(int resId) {
        this.resId = resId;
    }

    @Override
    public String setImageShowPickerUrl() {
        return url;
    }

    @Override
    public int setImageShowPickerDelRes() {
        return resId;
    }
}
