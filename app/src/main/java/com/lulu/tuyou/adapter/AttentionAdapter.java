package com.lulu.tuyou.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.ItemAttentionBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.view.GlideCircleTransform;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhanglulu on 2017/3/1.
 * “我关注的人”的Adapter
 */

public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.MyViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<TuYouUser> mList;
    private RecyclerView mRecyclerView;

    public AttentionAdapter(Context context, List<TuYouUser> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAttentionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_attention, parent, false);
        MyViewHolder holder = new MyViewHolder(binding.getRoot());
        holder.initView(binding, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
    public void add(TuYouUser user) {
        mList.add(user);
        notifyDataSetChanged();
    }

    //只添加不刷新
    public void addNoRefresh(TuYouUser user) {
        mList.add(user);
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
    private MapAdapter.OnChildListener mListener;

    public void setListener(MapAdapter.OnChildListener listener) {
        mListener = listener;
    }

    public interface OnChildListener {
        void onChildClick(View view);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 事件回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onChildClick(view);
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    // 内部的Holder
    ///////////////////////////////////////////////////////////////////////////
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mNickname;
        private TextView mAge;
        private ImageView mSex;
        private ImageView mIcon;
        private CardView mCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
        public void initView(ItemAttentionBinding binding, View.OnClickListener listener) {
            mNickname = binding.attentionItemNickname;
            mAge = binding.attentionItemAge;
            mSex = binding.attentionItemSex;
            mIcon = binding.attentionItemIcon;
            mCardView = binding.attentionItemCard;
            //监听
            mCardView.setOnClickListener(listener);
        }
        public void bindData(TuYouUser user) {
            if (user != null) {
                //点击时取出
                mCardView.setTag(user);

                mNickname.setText(user.getNickName());
                mAge.setText(String.valueOf(user.getAge()));
                Context context = mIcon.getContext();
                Glide.with(context).load(user.getIcon())
                        .transform(new GlideCircleTransform(context))
                        .into(mIcon);
                String sex = user.getSex();
                if (Constant.SEX_BOY.equals(sex)) {
                    mSex.setImageResource(R.mipmap.sex_boy);
                } else if (Constant.SEX_GIRL.equals(sex)) {
                    mSex.setImageResource(R.mipmap.sex_girl);
                }
            }

        }
    }
}
