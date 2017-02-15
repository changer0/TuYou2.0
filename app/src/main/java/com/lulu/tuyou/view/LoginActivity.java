package com.lulu.tuyou.view;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityLoginBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.presenter.ILoginPresenter;
import com.lulu.tuyou.presenter.LoginPresenterImpl;
import com.lulu.tuyou.utils.Utils;

import java.util.logging.Logger;

import cn.leancloud.chatkit.LCChatKit;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {

    ILoginPresenter mLoginPresenter;
    ActivityLoginBinding mBinding;
    private EditText mEtUserName;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private TextView mRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mLoginPresenter = new LoginPresenterImpl(this, this);
        //init view
        initView();
    }

    private void initView() {
        mEtUserName = mBinding.loginAccountName;
        mEtPwd = mBinding.loginAccountPassword;
        mBtnLogin = mBinding.loginAccountLogin;
        mRegister = mBinding.loginAccountReg;
        TextView clearAll = mBinding.loginAccountClearAll;
        clearAll.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_account_login:
                Dialog dialog = Utils.createLoadingDialog(
                        this, getResources().getString(R.string.login_text),
                        getResources().getString(R.string.login_logining)
                );
                dialog.show();
                mLoginPresenter.
                        doLogin(mEtUserName.getText().toString(), mEtPwd.getText().toString(), dialog);
                break;
            case R.id.login_account_reg:
                //跳转到注册页面
                jumpToRegisterActivity();
                break;
            case R.id.login_account_clear_all:
                clearAll();
                break;
        }
    }

    public void clearAll() {
        mEtUserName.setText("");
        mEtPwd.setText("");
    }

    @Override
    public void onLoginResult(boolean isSuccess, TuYouUser user, int code) {
        if (isSuccess) {
            LCChatKit.getInstance().open(user.getObjectId(), new AVIMClientCallback() {
                @Override
                public void done(AVIMClient client, AVIMException e) {
                    if (e == null) {
                        //Kit登录成功
                        Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, TuYouActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "即时通讯出了问题", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, getString(R.string.login_failed) + code, Toast.LENGTH_SHORT).show();
        }
    }

    public void dimissLoadingDialog(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void jumpToRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }


}
