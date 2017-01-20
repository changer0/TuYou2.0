package com.lulu.tuyou;

import android.app.Application;
import android.util.DisplayMetrics;

import com.lulu.tuyou.common.Constant;

import cn.bmob.v3.Bmob;

/**
 * Created by lulu on 2016/12/20.
 */
public class TuYouApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Bmob云服务初始化
        Bmob.initialize(getApplicationContext(), "1e3248953b2a7f8af68ba4214182e23a");
        //获取设备参数
        getDeviceWidth();
    }

    public int getDeviceWidth() {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        Constant.screenWidth = dm.widthPixels;
        Constant.screenHeight = dm.heightPixels;
        Constant.screenDensity = dm.density;
        return 0;
    }
}
