package com.lulu.tuyou;

import android.app.Application;
import android.util.DisplayMetrics;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.CustomUserProvider;
import com.lulu.tuyou.view.SplashActivity;
import com.lulu.tuyou.view.TuYouActivity;

import cn.bmob.v3.Bmob;
import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by lulu on 2016/12/20.
 */
public class TuYouApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Bmob云服务初始化
        Bmob.initialize(getApplicationContext(), "2482bb6e1055efb08aedcd42799133e7");
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(getApplicationContext(), "Rb3fPdUYpICYkO67j62sIlB5-gzGzoHsz", "K0KTtgoOYxtBpzrq0z1V03UL");
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), "Rb3fPdUYpICYkO67j62sIlB5-gzGzoHsz", "K0KTtgoOYxtBpzrq0z1V03UL");

        // 设置默认打开的 Activity Leancloud
        PushService.setDefaultPushCallback(this, TuYouActivity.class);
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
