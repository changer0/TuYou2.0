package com.lulu.tuyou.view;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.FragmentMapBinding;
import com.lulu.tuyou.presenter.IMapPresenter;
import com.lulu.tuyou.presenter.MapPresenterImpl;
import com.lulu.tuyou.view.event_handler.EventMap;

/**
 * A simple {@link Fragment} subclass.
 * 地图Fragment
 */
public class MapFragment extends Fragment implements IMapView {

    private static MapFragment instance;

    public static MapFragment newInstance() {
        if (instance == null) {
            synchronized (MapFragment.class) {
                if (instance == null) {
                    instance = new MapFragment();
                }
            }
        }
        return instance;
    }

    public MapFragment() {
    }

    private MapView mMapView;
    private Context mContext;
    private IMapPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private ImageView mUpArrowsImg;
    private NestedScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //绑定布局
        FragmentMapBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        initView(binding);
        // 创建地图
        mMapView.onCreate(savedInstanceState);
        EventMap eventMap = new EventMap(mContext, mPresenter);
        binding.setEvent(eventMap);
        //加载RecyclerView
        mPresenter.loadRecycler(mRecyclerView);
        //初始化Presenter的数据
        mPresenter.initData();
        //开启定位
        mPresenter.startLocation();
        return binding.getRoot();
    }

    private void initView(FragmentMapBinding binding) {
        //获取地图的引用
        mMapView = binding.mapView;
        mRecyclerView = binding.mapRecycler;
        mUpArrowsImg = binding.mapIcUp;
        mScrollView = binding.mapScrollView;
        //给向上的箭头图片着色
        ImageView upImg = binding.mapIcUp;
        Drawable upDrawable = getResources().getDrawable(R.mipmap.ic_arrow_up, mContext.getTheme());
        Drawable tintUpDrawable = DrawableCompat.wrap(upDrawable);
        DrawableCompat.setTint(tintUpDrawable, getResources().getColor(R.color.colorApp1));
        upImg.setImageDrawable(tintUpDrawable);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mPresenter = new MapPresenterImpl(this, mContext);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mPresenter.refreshData(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public MapView onGetMapView() {
        return mMapView;
    }

    @Override
    public RecyclerView onGetRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public ImageView onGetUpArrowsImg() {
        return mUpArrowsImg;
    }

    @Override
    public NestedScrollView onGetNestedScroll() {
        return mScrollView;
    }


}
