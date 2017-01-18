package com.lulu.tuyou.view.event_handler;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.lulu.tuyou.databinding.ActivityLoginBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.presenter.ILoginPresenter;

/**
 * Created by lulu on 2016/12/20.
 *
 */
public class EventLogin {
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
