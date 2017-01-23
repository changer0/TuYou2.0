package com.lulu.tuyou.presenter;

import android.content.Context;

import com.lulu.tuyou.view.IMessageView;

/**
 * Created by lulu on 2017/1/22.
 * 消息列表 处理逻辑
 */
public class MessagePresenterImpl implements IMessagePresenter {
    private Context mContext;
    private IMessageView mMessageView;

    public MessagePresenterImpl(Context context, IMessageView messageView) {
        mContext = context;
        mMessageView = messageView;
    }
}
