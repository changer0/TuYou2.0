package com.lulu.tuyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lulu.tuyou.R;

import java.util.List;

/**
 * Created by lulu on 2017/5/6.
 * 适配图友圈图片的显示
 */

public class CircleImageAdapter extends BaseAdapter {
    private List<String> mImageUrls;
    private Context mContext;

    public CircleImageAdapter(List<String> imageUrls, Context context) {
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mImageUrls != null) {
            ret = mImageUrls.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mImageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
            convertView.setTag(R.id.circle_item_image_tag, new ViewHolder());
        }
        Object tag = convertView.getTag(R.id.circle_item_image_tag);
        ViewHolder holder = (ViewHolder) tag;
        holder.mImageView = (ImageView) convertView.findViewById(R.id.item_image);
        Glide.with(mContext).load(mImageUrls.get(position)).into( holder.mImageView);

        return convertView;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 对外开放的方法
    ///////////////////////////////////////////////////////////////////////////
    public void setData(List<String> list) {
        mImageUrls.clear();
        mImageUrls.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(String str) {
        mImageUrls.add(str);
        notifyDataSetChanged();
    }
    static class ViewHolder {
        public ImageView mImageView;

    }

}
