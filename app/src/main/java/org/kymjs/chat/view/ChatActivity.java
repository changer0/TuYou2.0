/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.chat.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import com.lulu.tuyou.R;

import org.kymjs.chat.presenter.ChatPresenterImpl;
import org.kymjs.chat.presenter.IChatPresenter;
import org.kymjs.chat.widget.KJChatKeyboard;
import org.kymjs.kjframe.KJActivity;

/**
 * 聊天主界面 View层
 */
public class ChatActivity extends KJActivity implements IChatView {
    private IChatPresenter mPresenter;
    //输入键盘
    private KJChatKeyboard box;
    private ListView mRealListView;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        //不可放入onCreate方法中，会有空指针
        mPresenter = new ChatPresenterImpl(this, this);
        box = (KJChatKeyboard) findViewById(R.id.chat_msg_input_box);
        mRealListView = (ListView) findViewById(R.id.chat_listview);
        mRealListView.setSelector(android.R.color.transparent);
        //初始化Presenter的数据
        mPresenter.initPresenterData();
        mPresenter.initWidget();
    }

    @Override
    public KJChatKeyboard onGetBox() {
        return box;
    }

    @Override
    public ListView onGetRealListView() {
        return mRealListView;
    }

    @Override
    public Activity onGetAty() {
        return aty;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && box.isShow()) {
            box.hideLayout();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }


}
