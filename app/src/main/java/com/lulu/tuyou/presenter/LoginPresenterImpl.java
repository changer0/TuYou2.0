package com.lulu.tuyou.presenter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.view.ILoginView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by lulu on 2016/12/20.
 */
public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView mILoginView;

    public LoginPresenterImpl(ILoginView iLoginView) {
        mILoginView = iLoginView;
    }

    @Override
    public void clear() {
        mILoginView.onClearText();
    }

    @Override
    public void doLogin(String name, String passwd, final Dialog loginingDialog) {
        //登陆前的检查工作
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passwd)) {

        }

        BmobUser.loginByAccount(name, passwd, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                mILoginView.dimissLoginingDialog(loginingDialog);
                if (e == null) {
                    Constant.login_state = true;
                    mILoginView.onLoginResult(true, 0);
                } else {
                    mILoginView.onLoginResult(false, e.getErrorCode());
                }
            }
        });


    }

}
