package com.lulu.tuyou.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.lulu.tuyou.adapter.CircleAdapter;
import com.lulu.tuyou.databinding.FragmentCircleBinding;
import com.lulu.tuyou.db.TuYouTrackDbHandler;
import com.lulu.tuyou.db.TuYouVersionDbHandler;
import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouVersion;
import com.lulu.tuyou.utils.RefreshListener;
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




        //从网络上获取图友圈
        BmobQuery<TuYouTrack> trackBmobQuery = new BmobQuery<>();
        trackBmobQuery.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                if (e == null) {
                    dbTrackHandler.addTracks(list, new TuYouTrackDbHandler.OnStateListener() {
                        @Override
                        public void onState(int state) {
                            dbTrackHandler.getTracksFromDb(new TuYouTrackDbHandler.OnDataReceiveListener() {
                                @Override
                                public void onReceive(int type, List<TuYouTrack> tracks) {
                                    Collections.sort(tracks);
                                    mAdapter.setDataList(tracks);
                                }
                            });
                        }
                    });

                } else {
                    Log.d(TAG, "done: 错误信息: " + e.getMessage());
                }
            }
        });

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
                                getTrackDataFromNet(listener);
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
            public void onReceive(int type, List<TuYouTrack> tracks) {
                Collections.sort(tracks);
                mAdapter.setDataList(tracks);
            }
        });
    }

    private void getTrackDataFromNet(final RefreshListener listener) {
        //从网络上获取图友圈
        BmobQuery<TuYouTrack> query = new BmobQuery<>();
        query.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                if (e == null) {

                } else {
                    Log.d(TAG, "done: 错误信息: " + e.getMessage());
                }
            }
        });
    }

}
