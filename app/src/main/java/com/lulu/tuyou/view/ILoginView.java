package com.lulu.tuyou.view;

import android.app.Dialog;

/**
 * Created by lulu on 2016/12/20.
 *
 */

public interface ILoginView {
    void onLoginResult(boolean isSuccess, int code);
    void dimissLoadingDialog(Dialog dialog);
    void jumpToRegisterActivity();
}
