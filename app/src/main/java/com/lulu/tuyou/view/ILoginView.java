package com.lulu.tuyou.view;

import android.app.Dialog;

/**
 * Created by lulu on 2016/12/20.
 *
 */

public interface ILoginView {
    void onClearText();
    void onLoginResult(boolean result, int code);

    Dialog createLoginingDialog();
    void dimissLoginingDialog(Dialog dialog);
}
