package com.lulu.tuyou.view;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.amap.api.maps2d.MapView;

/**
 * Created by lulu on 2017/1/18.
 */

public interface IMapView {
    MapView onGetMapView();
    RecyclerView onGetRecyclerView();
    ImageView onGetUpArrowsImg();
    NestedScrollView onGetNestedScroll();
}
