package com.lulu.tuyou.view;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.FragmentCircleBinding;
import com.lulu.tuyou.presenter.CirclePresenterImpl;
import com.lulu.tuyou.presenter.ICirclePresenter;
import com.lulu.tuyou.utils.RefreshListener;
import com.lulu.tuyou.utils.Utils;

import java.io.File;

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
                mMenuDialog = CircleMenuDialog.getInstance(CircleFragment.this);
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


    private File mFile;
    public static final int REQUREST_CODE_CAMERA = 100;
    public static final int REQUREST_CODE_CROP = 101;
    public static final int REQUREST_CODE_ALBUM= 102;
    @Override
    public void onClickMenu(View v) {
        switch (v.getId()) {
            case R.id.common_title_menu_camera:
                //Toast.makeText(mContext, "相机", Toast.LENGTH_SHORT).show();
                //跳转到相机进行拍照取图
                mFile = Utils.jumpToCamera(this, REQUREST_CODE_CAMERA);
                break;
            case R.id.common_title_menu_photo_text:
                //Toast.makeText(mContext, "照片", Toast.LENGTH_SHORT).show();
                Utils.jumpToAlbum(this, REQUREST_CODE_ALBUM);
                break;
        }
    }

    public static String CIRCLE_FRAGMENT_IMAGE_PATH = "path";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);   //this
        switch (requestCode) {
            case REQUREST_CODE_CAMERA:
                if (resultCode == -1) {
                   //拍照完成
                    jumpToCircleImageActivity(mFile.getAbsolutePath());
                } else {
                    Toast.makeText(mContext, "取消拍照", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUREST_CODE_ALBUM:
                if (resultCode == -1) {
                    //选择完成
                    if (data != null) {
                        Uri uri = data.getData();
                        String path = Utils.getFilePathFromUri(mContext, uri);
                        jumpToCircleImageActivity(path);
                    }
                } else {
                    Toast.makeText(mContext, "取消选择", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void jumpToCircleImageActivity(String path) {
        Intent intent = new Intent();
        intent.putExtra(CIRCLE_FRAGMENT_IMAGE_PATH,path);
        intent.setClass(mContext, CircleImageActivity.class);
        startActivity(intent);
    }
}
