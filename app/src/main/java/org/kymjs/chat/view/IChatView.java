package org.kymjs.chat.view;

import android.app.Activity;
import android.widget.ListView;

import org.kymjs.chat.widget.KJChatKeyboard;

/**
 * Created by lulu on 2017/1/22.
 */

public interface IChatView {
    KJChatKeyboard onGetBox();
    ListView onGetRealListView();
    Activity onGetAty();
}
