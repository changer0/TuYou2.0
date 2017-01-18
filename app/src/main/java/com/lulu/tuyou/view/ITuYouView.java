package com.lulu.tuyou.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;

/**
 * Created by lulu on 2016/12/20.
 */

public interface ITuYouView {
    FragmentManager onGetFragmentManger();
    Context onGetContext();
}
