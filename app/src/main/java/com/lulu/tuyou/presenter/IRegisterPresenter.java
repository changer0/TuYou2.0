package com.lulu.tuyou.presenter;

import android.app.Dialog;

/**
 * Created by lulu on 2017/1/23.
 */

public interface IRegisterPresenter {
    void doRegister(String username, String pwd, String pwd2, Dialog dialog);
}
