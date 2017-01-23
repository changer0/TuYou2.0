package com.lulu.tuyou.view.event_handler;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.lulu.tuyou.presenter.IMapPresenter;
import com.lulu.tuyou.view.IMapView;

/**
 * Created by lulu on 2017/1/18.
 */

public class EventMap {
    private Context mContext;
    private IMapPresenter mPresenter;
    private IMapView mMapView;

    public EventMap(Context context, IMapPresenter presenter, IMapView mapView) {
        mContext = context;
        mPresenter = presenter;
        mMapView = mapView;
    }

    /**
     * 点击下方的箭头
     * @param view
     */
    public void clickBottomUpArrows(View view) {
        mMapView.hideUpArrows();
    }

    /**
     * 点击空白的View
     * @param view
     */
    public boolean touchEmptyView(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMapView.hideEmptyView();
                break;
        }
        return true;
    }
}
