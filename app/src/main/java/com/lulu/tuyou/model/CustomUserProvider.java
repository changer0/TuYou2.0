package com.lulu.tuyou.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by lulu on 2017/1/25.
 *
 */

public class CustomUserProvider implements LCChatProfileProvider {
    private static CustomUserProvider customUserProvider;
    private static List<LCChatKitUser> partUsers = new ArrayList<>();
    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    @Override
    public  void fetchProfiles(List<String> list, final LCChatProfilesCallBack callBack) {
        Log.d("lulu", "CustomUserProvider-fetchProfiles  执行了一次");
        final List<LCChatKitUser> userList = new ArrayList<>();
        for ( String userId : list) {
            for (LCChatKitUser user : partUsers) {
                if (user.getUserId().equals(userId)) {
                    userList.add(user);
                    break;
                }
            }
        }
        callBack.done(userList, null);
    }

    public static void setPartUsers(List<LCChatKitUser> partUsers) {
        CustomUserProvider.partUsers = partUsers;
    }

    public static List<LCChatKitUser> getPartUsers() {
        return partUsers;
    }
}
