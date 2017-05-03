package com.lulu.tuyou.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yzs.imageshowpickerview.ImageLoader;

/**
 * Created by zhanglulu on 2017/5/3.
 */

public class CircleImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    @Override
    public void displayImage(Context context, @DrawableRes Integer resId, ImageView imageView) {
        imageView.setImageResource(resId);
    }
    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }
}
