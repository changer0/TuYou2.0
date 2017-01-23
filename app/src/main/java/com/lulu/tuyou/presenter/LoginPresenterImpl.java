package com.lulu.tuyou.presenter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.view.ILoginView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lulu on 2016/12/20.
 */
public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView mILoginView;
    private Context mContext;

    public LoginPresenterImpl(ILoginView iLoginView, Context context) {
        mILoginView = iLoginView;
        mContext = context;
    }

    @Override
    public void doLogin(String name, String passwd, final Dialog loginingDialog) {
        //登录前的检查工作
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passwd)) {
            mILoginView.dimissLoadingDialog(loginingDialog);
            Toast.makeText(mContext, "用户名密码为空!!", Toast.LENGTH_SHORT).show();
            return;
        }
        TuYouUser user = new TuYouUser();
        user.setUsername(name);
        user.setPassword(passwd);
        user.login(new SaveListener<TuYouUser>() {
            @Override
            public void done(TuYouUser user, BmobException e) {
                mILoginView.dimissLoadingDialog(loginingDialog);
                if (e == null) {
                    Constant.currentUser = user;
                    Constant.login_state = true;
                    mILoginView.onLoginResult(true, 0);
                } else {
                    mILoginView.onLoginResult(false, e.getErrorCode());
                }
            }
        });


    }

}
