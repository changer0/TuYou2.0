package com.lulu.tuyou.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lulu on 17-4-24.
 */

public class SpManager {
    public static final String SETTING = "setting";
    public static final String SP_IS_INIT_CIRCLE_DATA = "init_data";

    public static void setInitCircleData(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        sp.edit().putBoolean(SP_IS_INIT_CIRCLE_DATA, true);
    }

    public static boolean getInitCircleData(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getBoolean(SP_IS_INIT_CIRCLE_DATA, false);
    }

}
