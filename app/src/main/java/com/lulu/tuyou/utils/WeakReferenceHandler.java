package com.lulu.tuyou.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by zhanglulu on 2017/4/26.
 * 弱引用的Handler，简单处理一下
 */
public class WeakReferenceHandler extends Handler {
    private WeakReference<Callback> mCallbackWeakReference;

    public WeakReferenceHandler(Callback callback) {
        super();
        mCallbackWeakReference = new WeakReference<Callback>(callback);
    }
    public WeakReferenceHandler(Looper looper, Callback cb) {
        super(looper);
        mCallbackWeakReference = new WeakReference<Handler.Callback>(cb);
    }

    @Override
    public void handleMessage(Message msg) {
        Callback callback = mCallbackWeakReference.get();
        if (callback != null) {
            callback.handleMessage(msg);
        }
    }
}
