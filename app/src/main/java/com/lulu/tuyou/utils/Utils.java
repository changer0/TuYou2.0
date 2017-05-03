package com.lulu.tuyou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import com.lulu.tuyou.view.CircleFragment;
import com.lulu.tuyou.view.TuYouActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
     * 裁剪图片
     * @param fragment
     * @param file
     * @param requestCode
     */
    public static void cropImage(Fragment fragment, File file, int requestCode) {
        String fileName = "new_" +  file.getName();
        Context context = fragment.getContext();
        File newFile = new File(context.getCacheDir(), fileName);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //有外置存储
            newFile = new File(context.getExternalCacheDir(), fileName);
        }
        //剪切完成后存放的位置
        Uri outputUri = FileProvider.getUriForFile(context, "com.lulu.tuyou.view.CircleMenuDialog", newFile);
        Uri imageUri = FileProvider.getUriForFile(context, "com.lulu.tuyou.view.CircleMenuDialog", file);
//        Uri imageUri = Uri.fromFile(mFile);
//        Uri outputUri = Uri.fromFile(newFile);

        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        //将存储图片的uri读写权限授权给剪裁工具应用
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        fragment.startActivityForResult(Intent.createChooser(intent, "选择剪裁工具"), requestCode);
    }

    /**
     * 跳转到相机拍照
     * @param fragment
     * @param requestCode
     * @return
     */
    public static File jumpToCamera(Fragment fragment, int requestCode) {
        Context context = fragment.getContext();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(context.getCacheDir(), fileName);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //有外置存储
            file = new File(context.getExternalCacheDir(), fileName);
        }

        //通过FileProvider去取
        Uri uri = FileProvider.getUriForFile(context, "com.lulu.tuyou.view.CircleMenuDialog", file);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//拍照Action
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//传入uri

        //将存储图片的uri读写权限授权给相机应用
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri , Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        fragment.startActivityForResult(intent, requestCode);
        return file;
    }

    /**
     * 跳转到相册
     * @param fragment
     * @param requestCode
     */
    public static void jumpToAlbum(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 通过Uri获取文件的路径
     */
    public static String getFilePathFromUri(Context context, Uri uri) {
        String path = "";
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme == null) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return path;
    }



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
        if (list == null || list.size() == 0) {
            return "";
        }
        JSONArray jsonArray = new JSONArray();
        for (String s : list) {
            jsonArray.put(s);
        }
        return jsonArray.toString();
    }
    //上一方法反向
    public synchronized static List<String> genStringListFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
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
