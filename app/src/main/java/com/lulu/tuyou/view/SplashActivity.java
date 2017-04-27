package com.lulu.tuyou.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.model.TuYouVersion;
import com.lulu.tuyou.utils.Utils;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.leancloud.chatkit.LCChatKit;

public class SplashActivity extends AppCompatActivity {
    private static String[] PERMISSIONS_TUYOU = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };
    public static final int REQUEST_PERMISSIONS_CODE = 100;

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
                            //版本初始化
                            TuYouVersion.versionInit();
                            //权限申请
                            requestPermissionInList();

                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * 为方便起见，用户第一次安装的时候将所有需要的权限申请下来
     */
    private void requestPermissionInList() {
        if (Build.VERSION.SDK_INT < 23) {
            //检查是否登录
            checkIsLogin();
        } else {
            String[] needRequestPermissionList = checkPermissions(this, PERMISSIONS_TUYOU);
            if (needRequestPermissionList.length > 0) {
                requestTuYouPermissions(needRequestPermissionList);
            } else {
                //检查是否登录
                checkIsLogin();
            }
        }
    }

    /**
     * 请求权限列表
     * @param permissions
     */
    protected void requestTuYouPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
    }

    // >=23 权限问题 by zhanglulu

    /**
     * 检查没有赋予权限的权限都有哪些
     * @param context
     * @param permissions
     * @return
     */
    public String[] checkPermissions(Context context, String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            //checkSelfPermission
            //如果应用具有此权限，方法将返回 PackageManager.PERMISSION_GRANTED，并且应用可以继续操作。
            // 如果应用不具有此权限，方法将返回 PERMISSION_DENIED，且应用必须明确向用户要求权限
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissionList.add(permission);
            }
        }
        String[] needRequestPermissionArray = new String[needRequestPermissionList.size()];
        return needRequestPermissionList.toArray(needRequestPermissionArray);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 权限申请回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (verifyPermissions(grantResults)) {
                checkIsLogin();
            } else {
                createRequestDialog().show();
            }
        }

    }

    private AlertDialog createRequestDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_app)
                .setTitle("权限获取失败")
                .setMessage("软件需要获取必要的权限以保证你正常使用图友")
                .setPositiveButton(
                        "授予权限",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //再次请求权限
                                requestPermissionInList();
                                //finish();
                            }
                        }).setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }


    //校验权限
    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //检查是否登录
    private void checkIsLogin() {
        //判断是否登录逻辑
        BmobUser currentUser = BmobUser.getCurrentUser();
        if (currentUser != null) {
            //有过登录
            existUser(currentUser);
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    //存在本地用户，即原来登录过
    private void existUser(BmobUser currentUser) {
        BmobQuery<TuYouUser> query = new BmobQuery<>();
        query.getObject(currentUser.getObjectId(), new QueryListener<TuYouUser>() {
            @Override
            public void done( TuYouUser user, BmobException e) {
                if (e == null) {
                    loginForLCChatKit(user);
                } else {
                    e.printStackTrace();
                    Toast.makeText(SplashActivity.this, "出了点问题，联上网试试？？" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }

    //登录即时通讯API
    private void loginForLCChatKit(TuYouUser user) {
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
    }



//    private void initVersion() {
//        BmobQuery<TuYouVersion> vQuery = new BmobQuery<>();
//        vQuery.findObjects(new FindListener<TuYouVersion>() {
//            @Override
//            public void done(List<TuYouVersion> list, BmobException e) {
//                if (list == null || list.size() == 0) {
//                    TuYouVersion version = new TuYouVersion();
//                    version.save(new SaveListener<String>() {
//                        @Override
//                        public void done(String s, BmobException e) {
//
//                        }
//                    });
//                }
//
//            }
//        });
//    }

}
