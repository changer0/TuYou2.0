package com.lulu.tuyou.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.CircleItemHeaderBinding;
import com.lulu.tuyou.databinding.ItemCircleBinding;
import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouUser;

import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by lulu on 17-4-19.
 *
 * 图友圈的Adapter
 */

public class CircleAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String TAG = "CircleAdapter";
    private Context mContext;
        private List<TuYouTrack> mList;
    public static final int TYPE_HEADER = 0x1;
    public static final int TYPE_CONTENT_TEXT = 0x2;
    public static final int TYPE_CONTENT_IMAGE = 0x3;

    public CircleAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new Vector();
        //首位空出来放Header
        mList.add(0, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        switch (viewType) {
            case TYPE_CONTENT_TEXT:
                binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_circle, parent, false);
                return new ContentViewHolder(binding, this, viewType);
            case TYPE_HEADER:
                binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.circle_item_header, parent, false);
                return new HeaderViewHolder(binding, this);
            case TYPE_CONTENT_IMAGE:
                binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_circle, parent, false);
                return new ContentViewHolder(binding, this, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).bindData(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            if (mList.get(position).getType() == TuYouTrack.TYPE_IMAGE) {
                return TYPE_CONTENT_IMAGE;
            } else {
                return TYPE_CONTENT_TEXT;
            }
        }
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
    // Adapter的工具方法
    ///////////////////////////////////////////////////////////////////////////
    // 指定位置删除
    public void remove(int position) {
        mList.remove(position + 1);
        //notifyDataSetChanged();//不要使用这种方式
        notifyItemRemoved(position + 1);
    }

    //添加数据
    public void add(int position, TuYouTrack list) {
        mList.add(position+1, list);
        notifyItemChanged(position + 1);
    }
    //添加数据
    public void add(TuYouTrack list) {
        mList.add(list);
        notifyDataSetChanged();
    }

    //只添加不刷新
    public void addNoRefresh(TuYouTrack list) {
        mList.add(list);
    }

    public void clearAll() {
        mList.clear();
        mList.add(0, null);
        notifyDataSetChanged();
    }

    public void clearAllNoRefresh() {
        mList.clear();
    }

    public void addDataList(List<TuYouTrack> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setDataList(List<TuYouTrack> list) {
        mList.clear();
        mList.add(0, null);
        mList.addAll(list);
        notifyDataSetChanged();
    }


    ///////////////////////////////////////////////////////////////////////////
    // 自定义Holder
    ///////////////////////////////////////////////////////////////////////////

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        CircleItemHeaderBinding mBinding;

        public HeaderViewHolder(ViewDataBinding binding, View.OnClickListener listener) {
            super(binding.getRoot());
            mBinding = ((CircleItemHeaderBinding) binding);
            mBinding.circleUsername.setText(Constant.currentUser.getNickName());
            Glide.with(itemView.getContext()).load(Constant.currentUser.getIcon()).into(mBinding.circleIcon);
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemCircleBinding mBinding;//一次大胆尝试
        int mViewType;

        public ContentViewHolder(ViewDataBinding binding, View.OnClickListener listener, int viewType) {
            super(binding.getRoot());
            mBinding = (ItemCircleBinding) binding;
            mBinding.itemCircleUsername.setOnClickListener(listener);
            mBinding.itemCircleIcon.setOnClickListener(listener);
            mViewType = viewType;
        }

        public void bindData(TuYouTrack tuYouTrack) {
            if (tuYouTrack != null) {
                Context context = mBinding.getRoot().getContext();
                String text = tuYouTrack.getText();
                if (!TextUtils.isEmpty(text)) {
                    mBinding.itemCircleText.setText(text);
                } else {
                    mBinding.itemCircleText.setVisibility(View.GONE);
                }
                mBinding.itemCircleTime.setText(tuYouTrack.getUpdatedAt());
                TuYouUser tuYouTrackUser = tuYouTrack.getUser();
                BmobQuery<TuYouUser> query = new BmobQuery<>();
                query.addQueryKeys("nickName,icon");
                query.getObject(tuYouTrack.getUser().getObjectId(), new QueryListener<TuYouUser>() {
                    @Override
                    public void done(TuYouUser tuYouUser, BmobException e) {
                        if (e == null) {
                            mBinding.itemCircleUsername.setText(tuYouUser.getNickName());
                            //头像
                            Glide.with(itemView.getContext()).load(tuYouUser.getIcon()).into(mBinding.itemCircleIcon);
                        } else {
                            Log.d(TAG, "done: e:" + e.getMessage());
                        }
                    }
                });

                if (mViewType == TYPE_CONTENT_IMAGE) {
                    //有图片显示的时候
                    mBinding.itemCircleGridView.setVisibility(View.VISIBLE);
                    CircleImageAdapter adapter = new CircleImageAdapter(tuYouTrack.getImages(), context);
                    mBinding.itemCircleGridView.setAdapter(adapter);
                } else {
                    mBinding.itemCircleGridView.setVisibility(View.GONE);
                }
            }
        }

    }



}
