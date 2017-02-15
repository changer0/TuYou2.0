package com.lulu.tuyou.view;

import android.app.Dialog;

import com.lulu.tuyou.model.TuYouUser;

/**
 * Created by lulu on 2017/1/23.
 */

public interface IRegisterView {
    void onResult(boolean isSuccess, TuYouUser user, int resultCode);

    void toast(String msg);

    void dismissDialog(Dialog dialog);
}
