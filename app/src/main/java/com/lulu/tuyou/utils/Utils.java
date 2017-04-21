package com.lulu.tuyou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.view.TuYouActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by lulu on 2017/1/17.
 */

public class Utils {

    /**
     * 跳转到聊天界面
     * @param context 上下文
     * @param userId 对方用户ID
     */
    public static void jumpToChatUINewTask(Context context, String userId) {
        Intent intent = new Intent(context, LCIMConversationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(LCIMConstants.PEER_ID, userId);
        context.startActivity(intent);
    }

    /**
     * 推送
     * @param context
     * @param peerInstallationId
     * @param action
     * @param state
     * @param userId
     * @param sendCallback
     */
    public static void pushHiData(Context context, String peerInstallationId, String action, String state, String userId, SendCallback sendCallback) {
        AVPush push = new AVPush();
        AVQuery pushQuery = AVInstallation.getQuery();
        // 假设 THE_INSTALLATION_ID 是保存在用户表里的 installationId，
        // 可以在应用启动的时候获取并保存到用户表
        Log.d("lulu", "MapPresenterImpl-onChildClick  user的push id：" + peerInstallationId);
        pushQuery.whereEqualTo("installationId", peerInstallationId);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(context, Constant.PUSH_HI_CHANNEL, TuYouActivity.class);
        push.setQuery(pushQuery);
        push.setChannel(Constant.PUSH_HI_CHANNEL);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put(Constant.PUSH_HI_ACTION, action);
        if (!TextUtils.isEmpty(state)){
            jsonObject.put(Constant.PUSH_HI_STATE, state);
        }
        if (!TextUtils.isEmpty(userId)){
            jsonObject.put(Constant.PUSH_HI_USER_ID, userId);
        }

        push.setData(jsonObject);
        push.setPushToAndroid(true);
        push.sendInBackground(sendCallback);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 用于登出时删除保存的Push用的InstallionId
    ///////////////////////////////////////////////////////////////////////////
    public static void removeInstallationId() {
        final AVInstallation installation = AVInstallation.getCurrentInstallation();
        String id = installation.getInstallationId();
        if (id != null) {
            installation.remove(id);
            installation.deleteInBackground();
        }
        //清空我的InstallId
        TuYouUser currentUser = Constant.currentUser;
        currentUser.setInstallationId("");
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    Log.d("lulu", "Utils-done  出现问题：" + e.getMessage());
                }
            }
        });

    }
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

    //生成JsonString
    public synchronized static String getStringsJson(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (String s : list) {
            jsonArray.put(s);
        }
        return jsonArray.toString();
    }
    //上一方法反向
    public synchronized static List<String> genStringListFromJson(String json) {
        List<String> ret = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                ret.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
