package com.lulu.tuyou.view.event_handler;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.presenter.ITuYouPresenter;
import com.lulu.tuyou.view.CircleFragment;
import com.lulu.tuyou.view.MapFragment;
import com.lulu.tuyou.view.MessageFragment;

import hugo.weaving.DebugLog;

/**
 * Created by lulu on 2016/12/20.
 * 事件触发类，配合Databinding使用
 */
public class EventTuYou {
    private ITuYouPresenter mPresenter;
    private Context mContext;

    public EventTuYou(ITuYouPresenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
    }

    /**
     * 点击下方的导航条
     * @param view
     */
    public void clickBottomNavigation(View view) {
        switch (view.getId()) {
            case R.id.main_rb_msg:
                mPresenter.clickMessageFragment();
                break;
            case R.id.main_rb_map:
                mPresenter.clickMapFragment();
                break;
            case R.id.main_rb_circle:
                mPresenter.clickCircleFragment();
                break;
        }
    }



}
