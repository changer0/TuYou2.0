package com.lulu.tuyou;

import android.app.Application;
import android.util.DisplayMetrics;

import com.avos.avoscloud.AVOSCloud;
import com.lulu.tuyou.common.Constant;

import cn.bmob.v3.Bmob;

/**
 * Created by lulu on 2016/12/20.
 *
 */
public class TuYouApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Bmob云服务初始化
        Bmob.initialize(getApplicationContext(), "2482bb6e1055efb08aedcd42799133e7");
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"Rb3fPdUYpICYkO67j62sIlB5-gzGzoHsz","K0KTtgoOYxtBpzrq0z1V03UL");
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
