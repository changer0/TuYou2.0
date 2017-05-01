package com.lulu.tuyou.view;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.FragmentCircleBinding;
import com.lulu.tuyou.presenter.CirclePresenterImpl;
import com.lulu.tuyou.presenter.ICirclePresenter;
import com.lulu.tuyou.utils.RefreshListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends Fragment implements ICircleView, SwipeRefreshLayout.OnRefreshListener {
    private static CircleFragment instance;
    private ICirclePresenter mPresenter;
    private Context mContext;
    private FragmentCircleBinding mBinding;
    public static final String CIRCLE_MENU_DIALOG_TAG = "circle_menu_dialog";
    private CircleMenuDialog mMenuDialog;

    public static CircleFragment newInstance() {
        if (instance == null) {
            synchronized (CircleFragment.class) {
                if (instance == null) {
                    instance = new CircleFragment();
                }
            }
        }
        return instance;
    }

    public CircleFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mPresenter = new CirclePresenterImpl(this, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_circle, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.circleSwipeRefresh.setColorSchemeColors(mContext.getResources().getColor(R.color.colorApp2));
        mBinding.circleSwipeRefresh.setOnRefreshListener(this);
        mPresenter.bindData(mBinding);
        mBinding.commonTitle.commonTitleMenu.setVisibility(View.VISIBLE);
        mBinding.commonTitle.commonTitleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDialog = CircleMenuDialog.getInstance();
                mMenuDialog.show(getFragmentManager(), CIRCLE_MENU_DIALOG_TAG);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMenuDialog != null && mMenuDialog.isVisible()) {
            mMenuDialog.dismiss();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 下拉刷新回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onRefresh() {
        mPresenter.refreshData(new RefreshListener() {
            @Override
            public void refreshStart() {

            }

            @Override
            public void refreshEnd() {
                SwipeRefreshLayout swipeRefresh = mBinding.circleSwipeRefresh;
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }
}
