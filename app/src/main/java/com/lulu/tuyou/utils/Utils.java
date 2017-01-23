package com.lulu.tuyou.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Window;

import com.lulu.tuyou.common.Constant;

/**
 * Created by lulu on 2017/1/17.
 */

public class Utils {
    /**
     * 沉浸式状态栏，
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

    public static int dip2px(float dipValue) {
        return (int) (Constant.screenDensity * dipValue + 0.5f);
    }

    public static String getIMEI(Context context) {
        String imei = null;
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        return imei;
    }
}
