package com.lulu.tuyou;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by lulu on 2016/12/20.
 */
public class TuYouApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(), "1e3248953b2a7f8af68ba4214182e23a");
    }
}
