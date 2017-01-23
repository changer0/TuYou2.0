package com.lulu.tuyou.view;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
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
public class MapFragment extends Fragment implements IMapView{
    private static final int DURATION = 500;
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
    private NestedScrollView mNestedScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //绑定布局
        FragmentMapBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        initView(binding);
        // 创建地图
        mMapView.onCreate(savedInstanceState);
        EventMap eventMap = new EventMap(mContext, mPresenter, this);
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
        mNestedScrollView = binding.mapScrollView;
        //监听事件注册

        //给向上的箭头图片着色
        ImageView upImg = binding.mapIcUp;
        Drawable upDrawable = getResources().getDrawable(R.mipmap.ic_arrow_up, mContext.getTheme());
        Drawable tintUpDrawable = DrawableCompat.wrap(upDrawable);
        DrawableCompat.setTint(tintUpDrawable, getResources().getColor(R.color.colorApp1));
        upImg.setImageDrawable(tintUpDrawable);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 隐藏向上的箭头
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void hideUpArrows() {
        //防止RecyclerView滚动到顶部
        mNestedScrollView.setVisibility(View.VISIBLE);
        mNestedScrollView.scrollTo(0, 0);
        float emptyH = mContext.getResources().getDimension(R.dimen.map_empty_view_height);
        //向上箭头缓慢消失
        AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
        alphaAnim.setDuration(DURATION);
        alphaAnim.start();
        mUpArrowsImg.setAnimation(alphaAnim);
        //动画移动上去
        TranslateAnimation animation = new TranslateAnimation(
                0, 0, (Constant.screenHeight - emptyH), 0);
        animation.setDuration(DURATION);
        animation.start();
        mNestedScrollView.setAnimation(animation);
        mUpArrowsImg.setVisibility(View.INVISIBLE);

    }

    ///////////////////////////////////////////////////////////////////////////
    // 隐藏空白的区域
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void hideEmptyView() {
        mUpArrowsImg.setVisibility(View.VISIBLE);
        float emptyH = mContext.getResources().getDimension(R.dimen.map_empty_view_height);
        //向上箭头缓慢消失
        AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
        alphaAnim.setDuration(DURATION);
        alphaAnim.start();
        mUpArrowsImg.setAnimation(alphaAnim);
        //动画移动上去
        TranslateAnimation animation = new TranslateAnimation(
                0, 0, 0, (Constant.screenHeight - emptyH));
        animation.setDuration(DURATION);
        animation.start();
        mNestedScrollView.setAnimation(animation);
        mNestedScrollView.setVisibility(View.INVISIBLE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 用于再次点击Map页面时的数据刷新用
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void refreshFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            mNestedScrollView.scrollTo(0, 0);
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // 给Presenter提供的AMap
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public AMap onGetAMap() {
        return mMapView.getMap();
    }



    ///////////////////////////////////////////////////////////////////////////
    // 生命周期方法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mPresenter = new MapPresenterImpl(this, mContext);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refreshFragment(this);
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
    // ----------------------------------------------------
}
