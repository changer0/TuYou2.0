package com.lulu.tuyou.presenter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by lulu on 2016/12/20.
 */

public interface ITuYouPresenter {
    void clickMessageFragment();
    void clickMapFragment();
    void clickCircleFragment();

    void saveInstanceState(Bundle state);
    void restoreInstanceState(Bundle state);
}