package com.lulu.tuyou.view.event_handler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.view.View;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityLoginBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.presenter.ILoginPresenter;
import com.lulu.tuyou.view.ILoginView;
import com.lulu.tuyou.view.LoginActivity;

/**
 * Created by lulu on 2016/12/20.
 *
 */
public class EventLogin {
    private ILoginPresenter mLoginPresenter;
    private Context mContext;
    private ActivityLoginBinding mBinding;
    private ILoginView mLoginView;

    public EventLogin(Context context, ILoginView loginView, ILoginPresenter loginPresenter, ActivityLoginBinding binding) {
        mContext = context;
        mLoginPresenter = loginPresenter;
        mBinding = binding;
        mLoginView = loginView;
    }

    public void loginClick(View view) {
        TuYouUser user = mBinding.getUser();
        Dialog loginingDialog = mLoginView.createLoginingDialog();
        mLoginPresenter.doLogin(user.getUsername(), user.getPassword(), loginingDialog);

    }
}
