package com.lulu.tuyou.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityTuYouBinding;
import com.lulu.tuyou.presenter.ITuYouPresenter;
import com.lulu.tuyou.presenter.TuYouPresenterImpl;
import com.lulu.tuyou.utils.Utils;
import com.lulu.tuyou.view.event_handler.EventTuYou;

/**
 * 最基本的Activity
 */
public class TuYouActivity extends AppCompatActivity implements ITuYouView {
    private ITuYouPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lulu", "TuYouActivity-onCreate  执行");
        mPresenter = new TuYouPresenterImpl(this);
        //设置沉浸式状态栏
        Utils.setTranslucentStatusBar(this, true);
        ActivityTuYouBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tu_you);

        //init presenter
        //init DataBinding
        EventTuYou eventTuyou = new EventTuYou(mPresenter, this);
        binding.setEventTuyou(eventTuyou);
        NavigationView navigation = binding.mainNavigation;

        //进入时手动点击 消息 的Fragment(暂时没有什么好的办法来解决这个问题)
        if (savedInstanceState == null) {
            RadioButton rbMsg = binding.mainRbMsg;
            rbMsg.setChecked(true);
            eventTuyou.clickBottomNavigation(rbMsg);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresenter.restoreInstanceState(savedInstanceState);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ITuYouView接口方法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public FragmentManager onGetFragmentManger() {
        return getSupportFragmentManager();
    }

    @Override
    public Context onGetContext() {
        return this;
    }
}