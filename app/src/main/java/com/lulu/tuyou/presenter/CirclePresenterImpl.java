package com.lulu.tuyou.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.lulu.tuyou.adapter.CircleAdapter;
import com.lulu.tuyou.databinding.FragmentCircleBinding;
import com.lulu.tuyou.db.DbConstant;
import com.lulu.tuyou.db.TuYouTrackDbHandler;
import com.lulu.tuyou.db.TuYouVersionDbHandler;
import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouVersion;
import com.lulu.tuyou.utils.RefreshListener;
import com.lulu.tuyou.utils.SpManager;
import com.lulu.tuyou.utils.WeakReferenceHandler;
import com.lulu.tuyou.view.ICircleView;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhanglulu on 2017/3/3.
 */

public class CirclePresenterImpl implements ICirclePresenter, Handler.Callback {
    private static final String TAG = "CirclePresenterImpl";
    private ICircleView mCircleView;
    private Context mContext;
    private CircleAdapter mAdapter;
    private TuYouTrackDbHandler dbTrackHandler;
    private TuYouVersionDbHandler dbVersionHandler;
    private WeakReferenceHandler mHandler;

    public CirclePresenterImpl(ICircleView circleView, Context context) {
        mCircleView = circleView;
        mContext = context;
        dbTrackHandler = new TuYouTrackDbHandler(context);
        dbVersionHandler = new TuYouVersionDbHandler(context);
        mHandler = new WeakReferenceHandler(this);
    }

    @Override
    public void bindData(FragmentCircleBinding binding) {
        binding.circleRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CircleAdapter(mContext);
        binding.circleRecycle.setAdapter(mAdapter);
        //binding.circleUsername.setText(Constant.currentUser.getNickName());
        //Glide.with(mContext).load(Constant.currentUser.getIcon()).into(binding.circleIcon);
        initData();
    }

    private void initData() {
        boolean isInit = SpManager.getInitCircleData(mContext);
        if (isInit) {
            refreshData(null);
        } else {
            getTrackDataFromNet(0);
            //已经初始化过了不用再刷新了
            SpManager.setInitCircleData(mContext);
        }
    }

    @Override
    public void refreshData(final RefreshListener listener) {
        mRefreshListener = listener;
        //修改逻辑为:
        //1. 对比版本号:如果发现 本地版本号 < 网络版本号 更新数据, 反之不需要管
        //2. 从而后从本地数据库加载数据
        //3.
        BmobQuery<TuYouVersion> versionQueey = new BmobQuery<>();
        versionQueey.findObjects(new FindListener<TuYouVersion>() {
            @Override
            public void done(List<TuYouVersion> list, BmobException e) {
                if (e == null) {
                    TuYouVersion tuYouVersion = list.get(0);
                    final int netVersion = Integer.parseInt(tuYouVersion.getTuyouTrackVersion());
                    Message msg = Message.obtain();
                    int version = dbVersionHandler.getTrackVersion(msg);
                    if (msg.arg1 == DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS) {
                        if (netVersion > version) {
                            //网络上的数据更新了,需要重新下载
                            getTrackDataFromNet(netVersion);
                        } else {
                            //直接显示本地数据库的内容
                            loadLocalTrackData();
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.refreshEnd();
                    }
                    Toast.makeText(mContext, "网络获取版本号失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //直接加载本地数据
    private void loadLocalTrackData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                List<TuYouTrack> tracks = dbTrackHandler.getTracks(msg);
                msg.what = GET_FROM_DB;
                msg.obj = tracks;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    //从网上获取数据，加入到本地数据中，并显示
    private void getTrackDataFromNet(final int netVersion) {
        BmobQuery<TuYouTrack> query = new BmobQuery<>();
        query.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                if (e == null) {
                    //清空数据库，并添加数据
                    clearLocalDb(list, netVersion);
                } else {
                    Toast.makeText(mContext, "获取云端数据出错："+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //清空数据库，并添加数据
    private void clearLocalDb(final List<TuYouTrack> list, final int netVersion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.obj = list;
                msg.what = CLEAR_DB;
                int state = dbTrackHandler.clearTracks();
                msg.arg1 = state;
                //更新本地的版本号
                dbVersionHandler.setTrackVersion(netVersion);
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    //添加数据
    private void addTracksToDb(final List<TuYouTrack> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.arg1 = dbTrackHandler.addTracks(list);
                msg.what = ADD_DB;
                mHandler.sendMessage(msg);
            }
        }).start();
    }
    ///////////////////////////////////////////////////////////////////////////
    // Handler处理
    ///////////////////////////////////////////////////////////////////////////
    public static final int CLEAR_DB = 0x1;
    public static final int ADD_DB = 0x2;
    public static final int GET_FROM_DB = 0x3;
    private RefreshListener mRefreshListener = null;
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CLEAR_DB:
                if (msg.arg1 == DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS) {
                    List<TuYouTrack> list = (List<TuYouTrack>) msg.obj;
                    addTracksToDb(list);
                } else {
                    Toast.makeText(mContext, "数据库清空失败", Toast.LENGTH_SHORT).show();
                }
                return true;
            case ADD_DB:
                if (msg.arg1 == DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS) {
                    loadLocalTrackData();
                } else {
                    Toast.makeText(mContext, "数据库添加失败失败", Toast.LENGTH_SHORT).show();
                }
                return true;
            case GET_FROM_DB:
                if (msg.arg1 == DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS) {
                    List<TuYouTrack> list = (List<TuYouTrack>) msg.obj;
                    Collections.sort(list);
                    mAdapter.setDataList(list);
                    if (mRefreshListener != null) {
                        mRefreshListener.refreshEnd();
                    }
                } else {
                    Toast.makeText(mContext, "从数据库加载数据失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }


}
