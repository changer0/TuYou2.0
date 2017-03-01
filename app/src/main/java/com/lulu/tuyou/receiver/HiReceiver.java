package com.lulu.tuyou.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SendCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.leancloud.chatkit.LCChatKit;


public class HiReceiver extends BroadcastReceiver {
    private static final String TAG = "lulu";
    private AlertDialog mDialog;
    public HiReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("lulu", "HiReceiver-onReceive  这个Receiver执行了");
        try {
            if (intent != null) {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                if (Constant.PUSH_HI_ACTION_REQUEST.equals(action)) {
                    JSONObject json = JSONObject.parseObject(bundle.getString("com.avos.avoscloud.Data"));
                    final String userId = json.getString(Constant.PUSH_HI_USER_ID);
                    Log.d("lulu", "HiReceiver-onReceive  userId:" + userId);
                    BmobQuery<TuYouUser> query = new BmobQuery<>();
                    query.getObject(userId, new QueryListener<TuYouUser>() {
                        @Override
                        public void done(final TuYouUser user, BmobException e) {
                            if (e == null) {
                                Log.d("lulu", "HiReceiver-done  进入了");
//                                intent.setClass(context, TuYouActivity.class);
//                                intent.setAction(Constant.PUSH_HI_START_ACTIVITY_ACTION);
//                                intent.putExtra(Constant.PUSH_HI_START_ACTIVITY_USER, user);
//                                intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                context.startActivity(intent);
                                if (mDialog != null) {
                                    if (mDialog.isShowing()) {
                                        mDialog.dismiss();
                                    }
                                }
                                mDialog = new AlertDialog.Builder(context)
                                        .setTitle("图友提示")
                                        .setMessage(user.getNickName() + "正在跟你打招呼，同意后进入聊天界面。。。")
                                        .setCancelable(false)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                LCChatKit.getInstance().open(Constant.currentUser.getObjectId(), new AVIMClientCallback() {
                                                    @Override
                                                    public void done(AVIMClient client, AVIMException e) {
                                                        if (e == null) {
                                                            Utils.pushHiData(context, user.getInstallationId(), Constant.PUSH_HI_ACTION_RESPONSE, Constant.PUSH_HI_STATE_AGREE, Constant.currentUser.getObjectId(),
                                                                    new SendCallback() {
                                                                        @Override
                                                                        public void done(AVException e) {
                                                                            if (e == null) {
                                                                                Toast.makeText(context, "已同意", Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                            dialog.dismiss();
                                                            Utils.jumpToChatUINewTask(context, user.getObjectId());
                                                        } else {
                                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Utils.pushHiData(context, user.getInstallationId(), Constant.PUSH_HI_ACTION_RESPONSE, Constant.PUSH_HI_STATE_DISAGREE, null
                                                        , new SendCallback() {
                                                            @Override
                                                            public void done(AVException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(context, "已拒绝", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        }).create();
                                //弹出系统的AlertDialog
                                mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                mDialog.show();
                            } else {
                                Log.d("lulu", "HiReceiver-done  " + e.getMessage());
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else if (Constant.PUSH_HI_ACTION_RESPONSE.equals(action)) {
                    //状态返回
                    JSONObject json = JSONObject.parseObject(bundle.getString("com.avos.avoscloud.Data"));
                    final String state = json.getString(Constant.PUSH_HI_STATE);
                    final String userId = json.getString(Constant.PUSH_HI_USER_ID);
                    if (Constant.PUSH_HI_STATE_AGREE.equals(state)) {
                        Utils.jumpToChatUINewTask(context, userId);
                    } else {
                        Toast.makeText(context, "对方拒绝并向你放了个屁！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }




}
