package com.lulu.tuyou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Window;

import com.lulu.tuyou.R;
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

    public static Dialog createLoadingDialog(Context context, String title, String msg) {
        //创建正在登录的Dialog
        ProgressDialog loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setTitle(title);
        loadingDialog.setMessage(msg);
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


}
