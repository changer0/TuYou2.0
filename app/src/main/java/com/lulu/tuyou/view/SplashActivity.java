package com.lulu.tuyou.view;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.CustomUserProvider;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.leancloud.chatkit.LCChatKit;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(this, true);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //判断是否登录逻辑
                            BmobUser currentUser = BmobUser.getCurrentUser();
                            if (currentUser != null) {
                                BmobQuery<TuYouUser> query = new BmobQuery<>();
                                query.getObject(currentUser.getObjectId(), new QueryListener<TuYouUser>() {
                                    @Override
                                    public void done( TuYouUser user, BmobException e) {
                                        if (e == null) {
                                            Constant.currentUser = user;
                                            Utils.saveInstallationId(user);
                                            LCChatKit.getInstance().open(user.getObjectId(), new AVIMClientCallback() {
                                                @Override
                                                public void done(AVIMClient client, AVIMException e) {
                                                    if (e == null) {
                                                        //Kit登录成功
                                                        startActivity(new Intent(SplashActivity.this, TuYouActivity.class));
                                                        finish();
                                                    } else {
                                                        Log.d("lulu", "SplashActivity-done  " + e.getMessage());
                                                        Toast.makeText(SplashActivity.this, "即时通讯出了问题" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            e.printStackTrace();
                                            Toast.makeText(SplashActivity.this, "出了点问题，联上网试试？？" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
