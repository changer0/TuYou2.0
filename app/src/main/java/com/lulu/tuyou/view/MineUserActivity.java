package com.lulu.tuyou.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MineUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MineUserActivity";
    private TextView mSetSex;
    private BmobUser mCurrentUser;
    private TextView mSetAge;
    private TuYouUser mTuYouUser;
    private TextView mSetCity;
    private ImageView mSetIcon;
    private TextView mSetName;
    private TextView mSetNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_user);
        ((TextView) findViewById(R.id.common_title_header)).setText("我");
        View backView = findViewById(R.id.common_title_back);
        backView.setOnClickListener(this);
        backView.setVisibility(View.VISIBLE);
        initView();//

    }

    private void initView() {
        mSetSex = (TextView) findViewById(R.id.user_tv_setSex);
        mSetAge = (TextView) findViewById(R.id.user_tv_setAge);
        mSetCity = (TextView) findViewById(R.id.user_tv_setCity);
        mSetIcon = (ImageView) findViewById(R.id.user_iv_icon);
        mSetName = (TextView) findViewById(R.id.user_tv_setName);
        mSetNickName = (TextView) findViewById(R.id.nick_tv_setName);


        CardView cardViewIcon = (CardView) findViewById(R.id.user_cv_icon);
        CardView cardViewUsername = (CardView) findViewById(R.id.user_cv_username);
        CardView cardViewNickName = (CardView) findViewById(R.id.nick_cv_username);
        CardView cardViewSex = (CardView) findViewById(R.id.user_cv_sex);
        CardView cardViewAge = (CardView) findViewById(R.id.user_cv_age);
        CardView cardViewSign = (CardView) findViewById(R.id.user_cv_city);

        cardViewIcon.setOnClickListener(this);
        cardViewUsername.setOnClickListener(this);
        cardViewNickName.setOnClickListener(this);
        cardViewSex.setOnClickListener(this);
        cardViewAge.setOnClickListener(this);
        cardViewSign.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = BmobUser.getCurrentUser();
        BmobQuery<TuYouUser> query = new BmobQuery<>();
        query.getObject(mCurrentUser.getObjectId(), new QueryListener<TuYouUser>() {
            @Override
            public void done(TuYouUser tuYouUser, BmobException e) {
                if (e == null) {
                    mTuYouUser = tuYouUser;
                    mSetSex.setText(tuYouUser.getSex());
                    mSetAge.setText(String.valueOf(tuYouUser.getAge()));
                    mSetCity.setText(tuYouUser.getCity());
                    mSetName.setText(tuYouUser.getUsername());
                    mSetNickName.setText(tuYouUser.getNickName());
                    Picasso.with(MineUserActivity.this).load(tuYouUser.getIcon()).config(Bitmap.Config.ARGB_8888).into(mSetIcon);
                } else {
                    Log.d(TAG, "done: e:" + e.getMessage());
                }
            }
        });

    }

    private void dialogSex() {
        final String[] items = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改性别");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                updateSex(items[which]);
            }
        });
        builder.create().show();
    }

    // 用于更新性别
    private void updateSex(final String sex) {
        TuYouUser tuYouUser = new TuYouUser();
        tuYouUser.setSex(sex);
        tuYouUser.update(mCurrentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mSetSex.setText(sex);
                    Snackbar.make(getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogAge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改年龄");
        final EditText newAge = new EditText(this);

        if (mTuYouUser != null) {
            int age = mTuYouUser.getAge();
            newAge.setText(String.valueOf(age));
        }
        builder.setView(newAge);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newAge != null) {
                    final String ageStr = newAge.getText().toString().trim();
                    int age = Integer.valueOf(ageStr);
                    if (age > 0 && age < 150) {
                        TuYouUser tuYouUser = new TuYouUser();
                        tuYouUser.setAge(age);
                        tuYouUser.update(mCurrentUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    mSetAge.setText(ageStr);
                                    Snackbar.make(getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Snackbar.make(getWindow().getDecorView(), "我靠!! 你年龄超了!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void dialogNickname() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText newName = new EditText(this);
        if (mTuYouUser != null) {
            String nickName = mTuYouUser.getNickName();
            newName.setText(nickName);
        }
        builder.setTitle("修改图友名").setView(newName);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = newName.getText().toString().trim();
                if (name != null) {
                    TuYouUser tuYouUser = new TuYouUser();
                    tuYouUser.setNickName(name);
                    tuYouUser.update(mCurrentUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mSetNickName.setText(name);
                                Snackbar.make(getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_cv_sex:
                dialogSex();
                break;
            case R.id.user_cv_age:
                dialogAge();
                break;
            case R.id.nick_cv_username:
                dialogNickname();
                break;

            case R.id.user_cv_icon:
                // TODO: 2016/11/8 跳转到修改头像
                Utils.jumpToAlbum(this, REQUEST_CODE);
                break;
            case R.id.common_title_back:
                finish();
                break;
        }
    }

    public static final int REQUEST_CODE = 0x1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == -1) {
                Uri uri = data.getData();
                String path = Utils.getFilePathFromUri(this, uri);
                updateIcon(path);
            } else {
                Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新头像
     * @param path
     */
    private void updateIcon(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    String url = bmobFile.getFileUrl();
                    Constant.currentUser.setIcon(url);
                    Constant.currentUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                Snackbar.make(getWindow().getDecorView(), "更新ICON失败", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
                                onResume();
                            }
                        }
                    });
                } else {
                    Snackbar.make(getWindow().getDecorView(), "更新ICON失败", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
