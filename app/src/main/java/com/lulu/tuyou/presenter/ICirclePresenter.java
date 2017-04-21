package com.lulu.tuyou.presenter;

import android.support.v7.widget.RecyclerView;

import com.lulu.tuyou.databinding.FragmentCircleBinding;
import com.lulu.tuyou.utils.RefreshListener;

/**
 * Created by zhanglulu on 2017/3/3.
 */

public interface ICirclePresenter {
    ///////////////////////////////////////////////////////////////////////////
    // 绑定数据
    ///////////////////////////////////////////////////////////////////////////
    void bindData(FragmentCircleBinding binding);

    ///////////////////////////////////////////////////////////////////////////
    // 数据刷新
    ///////////////////////////////////////////////////////////////////////////
    void refreshData(RefreshListener listener);
}
