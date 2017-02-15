package com.lulu.tuyou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;


public class HiReceiver extends BroadcastReceiver {
    private static final String TAG = "lulu";

    public HiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("lulu", "HiReceiver-onReceive  这个Receiver执行了");
        try {
            if (intent != null) {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                if ("com.tuyou.push".equals(action)) {
                    JSONObject json = new JSONObject(bundle.getString("com.avos.avoscloud.Data"));
                    String msg = json.optString("msg");
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}
