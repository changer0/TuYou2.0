package com.lulu.tuyou.presenter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.maps2d.MapView;

/**
 * Created by lulu on 2017/1/18.
 */

public interface IMapPresenter {
    void initData();
    void startLocation();
    void onDestroy();
    void initRecycler(RecyclerView recyclerView);
}
