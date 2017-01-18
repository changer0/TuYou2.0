package com.lulu.tuyou.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.lulu.tuyou.R;
import com.lulu.tuyou.view.CircleFragment;
import com.lulu.tuyou.view.ITuYouView;
import com.lulu.tuyou.view.MapFragment;
import com.lulu.tuyou.view.MessageFragment;

/**
 * Created by lulu on 2016/12/20.
 * 所有的逻辑实现
 */
public class TuYouPresenterImpl implements ITuYouPresenter {
    private ITuYouView mTuYouView;
    private FragmentManager mManager;
    private Context mContext;
    private MessageFragment mMessageFragment;
    private MapFragment mMapFragment;
    private CircleFragment mCircleFragment;
    private Fragment currentFragment; //当前的Fragment


    public TuYouPresenterImpl(ITuYouView tuYouView) {
        mTuYouView = tuYouView;
        mContext = mTuYouView.onGetContext();
        mManager = mTuYouView.onGetFragmentManger();
    }

    /**
     * 添加或显示Fragment
     *
     * @param fragment
     */
    private void addOrShowFragment(Fragment fragment, FragmentTransaction transaction) {

        if (currentFragment == null) {
            transaction.add(R.id.tuyou_container, fragment).commit();
            currentFragment = fragment;
            return;
        }
        if (currentFragment == fragment) {
            return;
        }
        // 如果当前的fragment没有被添加，则添加
        if (!fragment.isAdded()) {
            transaction.hide(currentFragment)
                    .add(R.id.tuyou_container, fragment)
                    .commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    @Override
    public void clickMessageFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = MessageFragment.newInstance();
        }

        addOrShowFragment(mMessageFragment, mManager.beginTransaction());
        Log.d("lulu", "TuYouPresenterImpl-clickMessageFragment  ");
    }

    @Override
    public void clickMapFragment() {
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
        }
        addOrShowFragment(mMapFragment, mManager.beginTransaction());
    }

    @Override
    public void clickCircleFragment() {
        if (mCircleFragment == null) {
            mCircleFragment = CircleFragment.newInstance();
        }
        addOrShowFragment(mCircleFragment, mManager.beginTransaction());
    }
}
