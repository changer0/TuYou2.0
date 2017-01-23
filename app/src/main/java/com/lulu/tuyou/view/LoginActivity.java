package com.lulu.tuyou.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lulu.tuyou.MainActivity;
import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityLoginBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.presenter.ILoginPresenter;
import com.lulu.tuyou.presenter.LoginPresenterImpl;
import com.lulu.tuyou.view.event_handler.EventLogin;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    ILoginPresenter mLoginPresenter;
    ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //init login presenter
        mLoginPresenter = new LoginPresenterImpl(this);
        //init DataBinding
        mBinding.setEventHandler(new EventLogin(this, this, mLoginPresenter, mBinding));


    }

    @Override
    public void onClearText() {

    }

    @Override
    public void onLoginResult(boolean result, int code) {
        if (result) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, TuYouActivity.class));
        } else {
            Toast.makeText(this, "登录失败" + code, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Dialog createLoginingDialog() {
        //创建正在登陆的Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle(R.string.login_text)
                .setMessage(R.string.login_logining).create();
        return dialog;
    }

    @Override
    public void dimissLoginingDialog(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
