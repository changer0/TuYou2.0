package com.lulu.tuyou.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.MapItemBinding;
import com.lulu.tuyou.model.TuYouRelation;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.view.GlideCircleTransform;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lulu on 2017/1/24.
 * 地图上显示附近的人的Recycler
 */

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyHolder> implements View.OnClickListener {
    private Context mContext;
    private List<TuYouUser> mList;
    private RecyclerView mRecyclerView;

    public MapAdapter(Context context, List<TuYouUser> list) {
        mContext = context;
        mList = list;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 连接上RecyclerView
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 断开RecyclerView
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.map_item, parent, false);
        MapItemBinding binding = DataBindingUtil.bind(itemView);
        MyHolder holder = new MyHolder(itemView);
        holder.initView(binding, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bindData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    ///////////////////////////////////////////////////////////////////////////
    // Adapter的工具方法
    ///////////////////////////////////////////////////////////////////////////
    // 指定位置删除
    public void remove(int position) {
        mList.remove(position);
        //notifyDataSetChanged();//不要使用这种方式
        notifyItemRemoved(position);
    }

    //添加数据
    public void add(int position, TuYouUser user) {
        mList.add(position, user);
        notifyItemChanged(position);
    }

    //添加数据
    public void add(TuYouUser user, boolean isScrolling) {
        Log.d("lulu", "MapAdapter-add  是否正在滚动：" + isScrolling );
        if (!isScrolling) {
            mList.add(user);
            Collections.sort(mList);
            notifyDataSetChanged();
        }
    }

    //只添加不刷新
    public void addNoRefresh(TuYouUser user) {
        mList.add(user);
        Collections.sort(mList);
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void clearAllNoRefresh() {
        mList.clear();
    }

    public void addDataList(List<TuYouUser> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setDataList(List<TuYouUser> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 事件监听
    ///////////////////////////////////////////////////////////////////////////
    private OnChildListener mListener;

    public void setListener(OnChildListener listener) {
        mListener = listener;
    }

    public interface OnChildListener {
        void onChildClick(View view);
    }

    @Override
    public void onClick(View view) {
        mListener.onChildClick(view);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 事件回调
    ///////////////////////////////////////////////////////////////////////////
    static class MyHolder extends RecyclerView.ViewHolder {
        TextView mNickname;
        TextView mAge;
        ImageView mAttention;
        TextView mDuration;
        ImageView mIcon;
        ImageView mSex;
        ImageView mHi;


        MyHolder(View itemView) {
            super(itemView);
        }

        private void initView(MapItemBinding binding, View.OnClickListener listener) {
            //binding.getRoot().setOnClickListener(this);
            mNickname = binding.mapItemNickname;
            mAge = binding.mapItemAge;
            mAttention = binding.mapItemAttention;
            mDuration = binding.mapItemDuration;
            mIcon = binding.mapItemIcon;
            mSex = binding.mapItemSex;
            mHi = binding.mapItemHi;
            //监听事件调用
            mAttention.setOnClickListener(listener);
            mHi.setOnClickListener(listener);
        }

        public void bindData(TuYouUser user) {
            if (user != null) {
                //设置tag方便以后获取
                mAttention.setTag(user);
                mHi.setTag(user);
                //UI
                mNickname.setText(user.getNickName());
                mAge.setText(String.valueOf(user.getAge()));
                mDuration.setText(String.valueOf(user.getDistance()));
                Context context = mIcon.getContext();
                Glide.with(context).load(user.getIcon())
                        .transform(new GlideCircleTransform(context))
                        .into(mIcon);
                if (user.getSex().equals(Constant.SEX_BOY)) {
                    mSex.setImageResource(R.mipmap.sex_boy);
                } else {
                    mSex.setImageResource(R.mipmap.sex_girl);
                }
                //设置是否加关注
                BmobQuery<TuYouRelation> query = new BmobQuery<>();
                query.addWhereEqualTo("fromUser", Constant.currentUser.getObjectId());
                query.addWhereEqualTo("toUser", user.getObjectId());
                query.findObjects(new FindListener<TuYouRelation>() {
                    @Override
                    public void done(List<TuYouRelation> list, BmobException e) {
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                mAttention.setImageResource(R.mipmap.ic_attention);
                            } else {
                                mAttention.setImageResource(R.mipmap.ic_no_attention);
                            }
                        } else {
                            Log.d("lulu", "done: MapFriendsAdapter有问题" + e.getMessage());
                        }
                    }
                });
            }
        }


    }
}
