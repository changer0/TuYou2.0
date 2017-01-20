package com.lulu.tuyou.view.event_handler;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.lulu.tuyou.presenter.IMapPresenter;

/**
 * Created by lulu on 2017/1/18.
 */

public class EventMap {
    private Context mContext;
    private IMapPresenter mPresenter;

    public EventMap(Context context, IMapPresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    /**
     * 点击下方的箭头
     * @param view
     */
    public void clickBottomUpArrows(View view) {
        mPresenter.hideUpArrows();
    }

    /**
     * 点击空白的View
     * @param view
     */
    public boolean touchEmptyView(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPresenter.hideEmptyView();
                break;
        }
        return true;
    }
}
