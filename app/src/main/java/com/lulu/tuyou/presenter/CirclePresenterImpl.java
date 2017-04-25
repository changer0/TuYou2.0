package com.lulu.tuyou.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.lulu.tuyou.adapter.CircleAdapter;
import com.lulu.tuyou.databinding.FragmentCircleBinding;
import com.lulu.tuyou.db.TuYouTrackDbHandler;
import com.lulu.tuyou.db.TuYouVersionDbHandler;
import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouVersion;
import com.lulu.tuyou.utils.RefreshListener;
import com.lulu.tuyou.utils.SpManager;
import com.lulu.tuyou.view.ICircleView;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhanglulu on 2017/3/3.
 */

public class CirclePresenterImpl implements ICirclePresenter {
    private static final String TAG = "CirclePresenterImpl";
    private ICircleView mCircleView;
    private Context mContext;
    private CircleAdapter mAdapter;
    private TuYouTrackDbHandler dbTrackHandler;
    private TuYouVersionDbHandler dbVersionHandler;

    public CirclePresenterImpl(ICircleView circleView, Context context) {
        mCircleView = circleView;
        mContext = context;
        dbTrackHandler = new TuYouTrackDbHandler(context);
        dbVersionHandler = new TuYouVersionDbHandler(context);
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
            getTrackDataFromNet(null, 0);
        }
    }

    @Override
    public void refreshData(final RefreshListener listener) {
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
                    dbVersionHandler.getTrackVersionFromDb(new TuYouVersionDbHandler.onGetTrackVersionListener() {
                        @Override
                        public void onGetTrackVersion(int version) {
                            if (netVersion > version) {
                                //网络上的数据更新了,需要重新下载
                                getTrackDataFromNet(listener, netVersion);
                            } else {
                                //直接显示本地数据库的内容
                                loadLocalTrackData();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadLocalTrackData() {
        dbTrackHandler.getTracksFromDb(new TuYouTrackDbHandler.OnDataReceiveListener() {

            @Override
            public void onReceive(int type, List<TuYouTrack> tracks, int state) {
                Collections.sort(tracks);
                mAdapter.setDataList(tracks);
            }
        });
    }

    private void getTrackDataFromNet(final RefreshListener listener, final int newVersion) {
        //从网络上获取图友圈
        BmobQuery<TuYouTrack> query = new BmobQuery<>();

        query.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(final List<TuYouTrack> list, BmobException e) {
                if (e == null) {
                    dbTrackHandler.clearTracks(new TuYouTrackDbHandler.OnDataReceiveListener() {
                        @Override
                        public void onReceive(int type, List<TuYouTrack> tracks, int state) {
                            dbTrackHandler.addTracks(list, new TuYouTrackDbHandler.OnDataReceiveListener() {

                                @Override
                                public void onReceive(int type, List<TuYouTrack> tracks, int state) {
                                    Log.d("lulu", "onState: state:" + state);
                                    dbVersionHandler.setTrackVersion(newVersion);
                                    if (listener != null) {
                                        Message msg = Message.obtain();
                                        msg.obj = listener;
                                        mHandler.sendMessage(msg);
                                    }
                                }
                            });
                        }
                    });

                } else {

                    Log.d(TAG, "done: 错误信息: " + e.getMessage());
                    //从网络上获取失败了
                    e.printStackTrace();
                }
            }
        });
    }
    public static final int CIRCLE_REFRESH_END = 0x1;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CIRCLE_REFRESH_END:
                    RefreshListener listener = (RefreshListener) msg.obj;
                    listener.refreshEnd();
                    break;
            }
        }
    };

}
