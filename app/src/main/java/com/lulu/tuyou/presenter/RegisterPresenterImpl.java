package com.lulu.tuyou.presenter;

import android.app.Dialog;
import android.text.TextUtils;

import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.view.IRegisterView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lulu on 2017/1/23.
 */

public class RegisterPresenterImpl implements IRegisterPresenter {
    private IRegisterView mRegisterView;

    public RegisterPresenterImpl(IRegisterView registerView) {
        mRegisterView = registerView;
    }

    @Override
    public void doRegister(String username, String pwd, String pwd2, final Dialog dialog) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
            mRegisterView.toast("用户名或密码为空！！");
            mRegisterView.dismissDialog(dialog);
            return;
        }
        //判断二次密码是否一致
        if (!pwd.equals(pwd2)) {
            mRegisterView.toast("两次密码不一致");
            return;
        }
        TuYouUser user = new TuYouUser();
        user.setUsername(username);
        user.setNickName(username);
        user.setPassword(pwd);
        //默认头像
        user.setIcon("http://download.easyicon.net/png/1192335/64/");
        user.signUp(new SaveListener<TuYouUser>() {
            @Override
            public void done(TuYouUser user, BmobException e) {
                mRegisterView.dismissDialog(dialog);
                if (e == null) {
                    //注册成功后
                    Constant.currentUser = user;
                    mRegisterView.onResult(true, 0);
                } else {
                    mRegisterView.onResult(false, e.getErrorCode());
                }
            }
        });
    }
}
