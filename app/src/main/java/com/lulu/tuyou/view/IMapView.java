package com.lulu.tuyou.view;

import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

/**
 * Created by lulu on 2017/1/18.
 */

public interface IMapView {

    ///////////////////////////////////////////////////////////////////////////
    // 隐藏向上的箭头
    ///////////////////////////////////////////////////////////////////////////
    void hideUpArrows();

    ///////////////////////////////////////////////////////////////////////////
    // 隐藏空白的区域
    ///////////////////////////////////////////////////////////////////////////
    void hideEmptyView();

    ///////////////////////////////////////////////////////////////////////////
    // 用于再次点击Map页面时的数据刷新用
    ///////////////////////////////////////////////////////////////////////////
    void refreshFragment(Fragment fragment);

    ///////////////////////////////////////////////////////////////////////////
    // 给Presenter提供的AMap
    ///////////////////////////////////////////////////////////////////////////
    AMap onGetAMap();

    ///////////////////////////////////////////////////////////////////////////
    //用户是否正在滚动
    ///////////////////////////////////////////////////////////////////////////
    boolean onGetIsUserScolling();

    ///////////////////////////////////////////////////////////////////////////
    // 使其滚回旧的位置
    ///////////////////////////////////////////////////////////////////////////
    void scrollToCurrentY();
}
