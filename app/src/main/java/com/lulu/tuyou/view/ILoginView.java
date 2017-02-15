package com.lulu.tuyou.view;

import android.app.Dialog;

import com.lulu.tuyou.model.TuYouUser;

/**
 * Created by lulu on 2016/12/20.
 *
 */

public interface ILoginView {
    void onLoginResult(boolean isSuccess, TuYouUser user, int code);
    void dimissLoadingDialog(Dialog dialog);
}
