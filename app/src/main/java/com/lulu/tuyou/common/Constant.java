package com.lulu.tuyou.common;

import com.lulu.tuyou.model.TuYouUser;

/**
 * Created by lulu on 2017/1/19.
 *
 */
public class Constant {

    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static float screenDensity = 0;
    public static boolean login_state = false;//退出登录时一定想着将其设置为false
    public static final String CURRENT_FRAGMENT_NAME = "current_fragment";
    public static TuYouUser currentUser = null;

    public static final String SEX_BOY = "boy";
    public static final String SEX_GIRL = "girl";

    public static final String ATTENTION_TYPE_FRIENDS = "isFriends";

    public static final String PUSH_HI_ID = "hi_id";
    public static final String PUSH_HI_STATE = "hi_state";


}
