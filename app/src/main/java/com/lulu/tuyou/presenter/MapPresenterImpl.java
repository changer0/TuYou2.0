package com.lulu.tuyou.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SendCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import com.lulu.tuyou.R;
import com.lulu.tuyou.adapter.MapAdapter;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.model.CustomUserProvider;
import com.lulu.tuyou.model.TuYouRelation;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;
import com.lulu.tuyou.view.IMapView;
import com.lulu.tuyou.view.TuYouActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;

/**
 * Created by lulu on 2017/1/18.
 * 地图Presenter核心业务类
 */
public class MapPresenterImpl implements IMapPresenter, AMapLocationListener, LocationSource, AMap.OnMapTouchListener,
        AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, NearbySearch.NearbyListener, MapAdapter.OnChildListener {
    private IMapView mMapFragmentView;
    private List<TuYouUser> mList;
    private Context mContext;
    private AMap mAMap;
    private Marker mMyMarker; //地图上“我”的点
    private List<Marker> mOtherMakers;//地图上“其他人”的点
    private boolean isDrag = false; //是否是拖拽状态
    private float currentZoom = 15; //当前的放大级别
    private NearbySearch mNearbySearch; //附近
    private TuYouUser mCurrentUser;
    private MapAdapter mAdapter;

    public MapPresenterImpl(IMapView mapFragmentView, Context context) {
        mMapFragmentView = mapFragmentView;
        mContext = context;
        mCurrentUser = Constant.currentUser;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 在Presenter中初始化数据
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void initData() {
        mAMap = mMapFragmentView.onGetAMap();
        //触摸监听事件
        mAMap.setOnMapTouchListener(this);
        //可视范围改变监听
        mAMap.setOnCameraChangeListener(this);
        //Marker监听
        mAMap.setOnMarkerClickListener(this);

        mNearbySearch = NearbySearch.getInstance(mContext);
        mNearbySearch.addNearbyListener(this);
        mOtherMakers = new ArrayList<>();
    }


    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;

    ///////////////////////////////////////////////////////////////////////////
    // 定位开启
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void startLocation() {
        //设置定位监听
        mAMap.setLocationSource(this);
        //设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMyLocationEnabled(true);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 定位激活
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void activate(OnLocationChangedListener listener) {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(1000 * 10);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.setLocationListener(this);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 停止定位
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 定位回调监听
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {
                //只有不是在拖拽状态下始终保持当前位置是屏幕的中心
                if (!isDrag) {
                    //定位成功
                    // 获取网络定位结果
                    int type = location.getLocationType();
                    double latitude = location.getLatitude();//纬度
                    double longitude = location.getLongitude();//经度
                    String city = location.getCity();
                    LatLng latLng = new LatLng(latitude, longitude);
                    LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                    //上传用户位置信息
                    uploadCurrentUserInfo(latLonPoint);
                    //添加自己到地图上
                    addMineToMap(latLng);
                    //添加附近的人到地图上
                    addOtherToMap(latLonPoint);
                    //将地图放大到一定级别
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(currentZoom);
                    mAMap.animateCamera(zoom);
                    CameraUpdate changeLatLng = CameraUpdateFactory.changeLatLng(latLng);
                    mAMap.animateCamera(changeLatLng);
                }
            } else {
                Log.e("lulu", "onLocationChanged: 定位异常, 异常码：" + location.getErrorCode() + "\n异常信息：" +
                        location.getErrorInfo());
            }
        } else {
            Toast.makeText(mContext, "定位信息返回为空！！", Toast.LENGTH_SHORT).show();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 添加其他人到Map上
    ///////////////////////////////////////////////////////////////////////////
    private void addOtherToMap(LatLonPoint point) {
        //设定搜索条件
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(point);
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(100000);
        //设置查询的时间
        query.setTimeRange(5000);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH);
        //调用异步查询接口
        mNearbySearch
                .searchNearbyInfoAsyn(query);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 获取附近人的回调接口
    ///////////////////////////////////////////////////////////////////////////
    private boolean isToast = false;
    public static boolean isHaveFriends = false;//附近是否有图友
    private Map<String, TuYouUser> mOldUserHashMap = new HashMap<>();
    private Map<String, LatLonPoint> mOldPoint = new HashMap<>();
    private Map<String, LCChatKitUser> mKitUserMap = new HashMap<>();

    @Override
    public void onUserInfoCleared(int i) {

    }

    @Override
    public void onNearbyInfoSearched(NearbySearchResult result, int resultCode) {
        //搜索周边附近用户
        if (resultCode == 1000) {
            if (result != null
                    && result.getNearbyInfoList() != null
                    && result.getNearbyInfoList().size() > 0) {
                List<NearbyInfo> list = result.getNearbyInfoList();
                //清空Marker
                for (int i = 0; i < mOtherMakers.size(); i++) {
                    mOtherMakers.get(i).remove();
                    mOtherMakers.remove(i);
                }
                //先将数据清空
                mAdapter.clearAllNoRefresh();
                //需要将现有List集合转为线程安全的
                //Log.d("lulu", "MapPresenterImpl-onNearbyInfoSearched  list:" + list.size());
                List<NearbyInfo> listSafe = Collections.synchronizedList(list);
                for (int i = 0; i < listSafe.size(); i++) {
                    final NearbyInfo info = listSafe.get(i);
                    final int distance = info.getDistance();
                    final String userID = info.getUserID();
                    //如果是用户自己则需要continue
                    if (userID.equals(mCurrentUser.getObjectId())) {
                        //有时会将自己的位置显示出来
                        if (list.size() == 1 && !isToast) {
                            Toast.makeText(mContext, R.string.map_no_friends, Toast.LENGTH_SHORT).show();
                            MapPresenterImpl.isHaveFriends = false;
                            isToast = true;
                        }
                        continue;
                    }


                    MapPresenterImpl.isHaveFriends = true;
                    final LatLonPoint point = info.getPoint();
                    LatLonPoint oldPoint = mOldPoint.get(userID);
                    if (oldPoint != null) {
                        double oldLat = oldPoint.getLatitude();
                        double oldLon = oldPoint.getLongitude();
                        //这是一步优化处理，当经纬度都大于1'之后才进行更新
                        if (Math.abs(oldLat - point.getLatitude()) < (1 / 60.0) && Math.abs(oldLon - point.getLongitude()) < (1 / 60.0)) {
                            //Log.d("lulu", "MapPresenterImpl-onNearbyInfoSearched  执行了优化逻辑");
                            //此时还是需要把这个用户标记在上面
                            addOlderPointToMap(distance, userID, oldPoint);
                            continue;
                        }
                    }
                    //更新旧坐标
                    mOldPoint.put(userID, point);
                    //根据用户ID查找
                    BmobQuery<TuYouUser> query = new BmobQuery<>();
                    query.getObject(userID, new QueryListener<TuYouUser>() {
                        @Override
                        public void done(TuYouUser user, BmobException e) {
                            if (e == null) {
                                addNewPointToMap(user, point, userID, distance);
                            } else {
                                Log.d("lulu", "MapPresenterImpl " + e.getMessage());
                            }
                        }
                    });

                }

            } else {
                Toast.makeText(mContext, R.string.map_no_friends, Toast.LENGTH_SHORT).show();
                MapPresenterImpl.isHaveFriends = false;
                isToast = true;
            }
        } else {
            Log.d("lulu", "MapPresenterImpl-onNearbyInfoSearched  出现异常异常码： " + resultCode);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //添加KitUser
    ///////////////////////////////////////////////////////////////////////////
    public void addKitUsers(String userID, String username, String img) {
        LCChatKitUser kitUser = mKitUserMap.get(userID);
        if (kitUser == null) {
            //Log.d("lulu", "MapPresenterImpl-addKitUsers  img：" + img);
            LCChatKitUser e = new LCChatKitUser(userID, username, img);
            CustomUserProvider.getPartUsers().add(e);
            mKitUserMap.put(userID, e);
        } else {

            Log.d("lulu", "MapPresenterImpl-addKitUsers  kitUser" + kitUser.getUserName());
            //只有在两者都不相等才会调用
            if (!img.equals(kitUser.getAvatarUrl()) || !username.equals(kitUser.getUserName())) {
                LCChatKitUser e = new LCChatKitUser(userID, username, img);
                CustomUserProvider.getPartUsers().add(e);
                mKitUserMap.put(userID, e);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 添加新的point到地图上
    ///////////////////////////////////////////////////////////////////////////
    private void addNewPointToMap(TuYouUser user, LatLonPoint point, String userID, int distance) {
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_other_mark))
                .draggable(false);
        Marker marker = mAMap.addMarker(options);
        mOtherMakers.add(marker);
        synchronized (MapPresenterImpl.class) {
            mOldUserHashMap.put(userID, user);
            //添加到kitUser中
            addKitUsers(user.getObjectId(), user.getNickName(), user.getIcon());
            Log.d("lulu", "MapPresenterImpl-done  添加数据");
            user.setDistance(distance);
            mAdapter.add(user, mMapFragmentView.onGetIsUserScolling());
            mMapFragmentView.scrollToCurrentY();
            //Log.d("lulu", "MapPresenterImpl-done  userName" + user.getNickName());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 将旧的点添加到地图上
    ///////////////////////////////////////////////////////////////////////////
    private void addOlderPointToMap(int distance, String userID, LatLonPoint point) {
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_other_mark))
                .draggable(false);
        Marker marker = mAMap.addMarker(options);
        mOtherMakers.add(marker);

        TuYouUser user = mOldUserHashMap.get(userID);
        if (user != null) {
            synchronized (MapPresenterImpl.class) {
                addKitUsers(user.getObjectId(), user.getNickName(), user.getIcon());
                user.setDistance(distance);
                mAdapter.add(user, mMapFragmentView.onGetIsUserScolling());
                mMapFragmentView.scrollToCurrentY();
            }
        }
    }

    @Override
    public void onNearbyInfoUploaded(int i) {
    }

    ///////////////////////////////////////////////////////////////////////////
    // 添加自身到Map
    ///////////////////////////////////////////////////////////////////////////
    private void addMineToMap(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng)
                .draggable(false)//是否拖拽
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_mark));
        //先清除一下
        if (mMyMarker != null) {
            mMyMarker.remove();
        }
        mMyMarker = mAMap.addMarker(markerOptions);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 上传当前用户位置
    ///////////////////////////////////////////////////////////////////////////
    private void uploadCurrentUserInfo(LatLonPoint latLonPoint) {
        UploadInfo loadInfo = new UploadInfo();
        loadInfo.setCoordType(NearbySearch.AMAP);
        loadInfo.setPoint(latLonPoint);
        loadInfo.setUserID(mCurrentUser.getObjectId());
        mNearbySearch.uploadNearbyInfoAsyn(loadInfo);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点击Marker的回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onMarkerClick(Marker marker) {
        mMapFragmentView.hideUpArrows();
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Map的Touch回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onTouch(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isDrag = true;
                break;
            case MotionEvent.ACTION_UP:
                isDrag = false;
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 缩放等的回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onCameraChange(CameraPosition position) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition position) {
        // 记录当前的放大级别
        currentZoom = position.zoom;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 从View回调过来，用于释放资源
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        if (mNearbySearch != null) {
            mNearbySearch.destroy();
        }
    }


    @Override
    public void initRecycler(RecyclerView recyclerView) {
        mList = new Vector<>();
        mAdapter = new MapAdapter(mContext, mList);
        mAdapter.setListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onChildClick(View view) {
        TuYouUser user = null;
        Object tag = view.getTag();
        if (tag != null && tag instanceof TuYouUser) {
            user = (TuYouUser) tag;
        }
        switch (view.getId()) {
            case R.id.map_item_attention:
                //先让其不可点击
                view.setClickable(false);
                if (user != null && view instanceof ImageView) {
                    final ImageView imageView = (ImageView) view;
                    BmobQuery<TuYouRelation> query = new BmobQuery<>();
                    query.addWhereEqualTo("fromUser", Constant.currentUser.getObjectId());
                    query.addWhereEqualTo("toUser", user.getObjectId());
                    final TuYouUser finalUser = user;

                    query.findObjects(new FindListener<TuYouRelation>() {
                        @Override
                        public void done(List<TuYouRelation> list, BmobException e) {
                            if (e == null) {
                                if (list != null && list.size() > 0) {
                                    //关注了，取消关注
                                    TuYouRelation relation = list.get(0);
                                    relation.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                imageView.setImageResource(R.mipmap.ic_no_attention);
                                                Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "出了点问题" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    relationToUser(finalUser, imageView);
                                }
                            } else {

                                relationToUser(finalUser, imageView);
                                Log.d("lulu", "done: MapFriendsAdapter有问题" + e.getMessage());
                            }
                            imageView.setClickable(true);
                        }
                    });
                }
                break;
            case R.id.map_item_hi:
                if (user != null) {

                    Utils.pushHiData(mContext, user.getInstallationId(), Constant.PUSH_HI_ACTION_REQUEST, null, mCurrentUser.getObjectId(), new SendCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "正在请求", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                break;
        }
    }

    private void relationToUser(TuYouUser finalUser, final ImageView imageView) {
        //没有关注，关注
        TuYouRelation relation = new TuYouRelation();
        relation.setFromUser(mCurrentUser);
        relation.setToUser(finalUser);
        relation.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(R.mipmap.ic_attention);
                } else {
                    Toast.makeText(mContext, "出了点问题" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
