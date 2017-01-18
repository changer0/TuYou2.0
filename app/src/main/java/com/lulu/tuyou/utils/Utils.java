package com.lulu.tuyou.utils;

import android.app.Activity;
import android.view.Window;

/**
 * Created by lulu on 2017/1/17.
 */

public class Utils {
    /**
     * 沉浸式状态栏，白色titlebar推荐设置透明状态栏，游戏中心专用，解决刷新后UIbug
     *
     * @param activity
     */
    public static void setTranslucentStatusBar(Activity activity,
                                                        boolean translucentStatusBar) {
        if (translucentStatusBar) {
            Window window = activity.getWindow();
            window.clearFlags(0x80000000);
            window.addFlags(0x04000000);
        } else {
            activity.getWindow().clearFlags(0x04000000);
        }
    }
}
