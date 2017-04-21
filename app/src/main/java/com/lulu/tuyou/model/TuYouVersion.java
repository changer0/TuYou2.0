package com.lulu.tuyou.model;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lulu on 17-4-20.
 */

public class TuYouVersion extends BmobObject {
    private String tuyouTrackVersion = "0";

    public String getTuyouTrackVersion() {
        return tuyouTrackVersion;
    }

    public void setTuyouTrackVersion(String tuyouTrackVersion) {
        this.tuyouTrackVersion = tuyouTrackVersion;
    }

    public static void versionInit() {
        BmobQuery<TuYouVersion> vQuery = new BmobQuery<>();
        vQuery.findObjects(new FindListener<TuYouVersion>() {
            @Override
            public void done(List<TuYouVersion> list, BmobException e) {

                if (list == null || list.size() == 0) {
                    createTuYouVersionToBmob();
                    return;
                }

            }
        });
    }

    private static void createTuYouVersionToBmob() {
        TuYouVersion version = new TuYouVersion();
        version.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
            }
        });
    }

    public static void versionAdd() {
        BmobQuery<TuYouVersion> vQuery = new BmobQuery<>();
        vQuery.findObjects(new FindListener<TuYouVersion>() {
            @Override
            public void done(List<TuYouVersion> list, BmobException e) {
                if (e == null) {
                    TuYouVersion tuYouVersion = list.get(0);
                    String oldVersion = tuYouVersion.tuyouTrackVersion;
                    int i = Integer.parseInt(oldVersion);
                    String newVersion = String.valueOf(i + 1);
                    tuYouVersion.setTuyouTrackVersion(newVersion);
                    tuYouVersion.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                Log.d("lulu", "done: 更新版本号出现问题");
                            }
                        }
                    });
                } else {
                    Log.d("lulu", "done: 上传版本号出现问题");
                }

            }
        });
    }
}
