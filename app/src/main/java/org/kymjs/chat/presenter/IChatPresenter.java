package org.kymjs.chat.presenter;

import android.content.Intent;

/**
 * Created by lulu on 2017/1/22.
 */

public interface IChatPresenter {
    void initWidget();
    void initPresenterData();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
