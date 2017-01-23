package com.lulu.tuyou.presenter;

import android.app.Dialog;

/**
 * Created by lulu on 2016/12/20.
 *
 */

public interface ILoginPresenter {
    void clear();
    void doLogin(String name, String passwd, Dialog loginingDialog);
}
