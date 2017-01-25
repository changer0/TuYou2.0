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

import com.avos.avoscloud.AVOSCloud;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(this, true);
        setContentView(R.layout.activity_splash);
        //Bmob云服务初始化
        Bmob.initialize(getApplicationContext(), "2482bb6e1055efb08aedcd42799133e7");
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(getApplicationContext(), "Rb3fPdUYpICYkO67j62sIlB5-gzGzoHsz", "K0KTtgoOYxtBpzrq0z1V03UL");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    BmobUser currentUser = BmobUser.getCurrentUser();

                    if (currentUser != null) {
                        BmobQuery<TuYouUser> query = new BmobQuery<>();
                        query.getObject(currentUser.getObjectId(), new QueryListener<TuYouUser>() {
                            @Override
                            public void done(TuYouUser user, BmobException e) {
                                if (e == null) {
                                    Constant.currentUser = user;
                                    startActivity(new Intent(SplashActivity.this, TuYouActivity.class));
                                    finish();
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(SplashActivity.this, "出了点问题，联上网试试？？", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
