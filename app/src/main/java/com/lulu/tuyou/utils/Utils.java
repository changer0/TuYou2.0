package com.lulu.tuyou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.SaveCallback;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lulu on 2017/1/17.
 */

public class Utils {
    ///////////////////////////////////////////////////////////////////////////
    // push 用于保存ID
    ///////////////////////////////////////////////////////////////////////////
    public static void saveInstallationId(final TuYouUser user) {
        // TODO: 2017/2/15 push用 一会儿需要修改
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    // 关联  installationId 到用户表等操作
                    user.setInstallationId(installationId);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                Log.d("lulu", "SplashActivity-done  保存失败" + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("lulu", "SplashActivity-done  保存installationId失败：" + e.getMessage());
                }
            }
        });
    }

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
