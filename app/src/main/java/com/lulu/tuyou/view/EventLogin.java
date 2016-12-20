package com.lulu.tuyou.view;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Toast;

import com.lulu.tuyou.databinding.ActivityLoginBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.presenter.ILoginPresenter;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by lulu on 2016/12/20.
 *
 */
public class EventLogin extends BaseObservable{
    private ILoginPresenter mLoginPresenter;
    private Context mContext;
    private ActivityLoginBinding mBinding;

    public EventLogin(Context context, ILoginPresenter loginPresenter, ActivityLoginBinding binding) {
        mContext = context;
        mLoginPresenter = loginPresenter;
        mBinding = binding;
    }

    public void loginClick(View view) {
        TuYouUser user = mBinding.getUser();
        mLoginPresenter.doLogin(user.getUsername(), user.getPassword());
    }
}
