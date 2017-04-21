package com.lulu.tuyou.db;

import android.provider.BaseColumns;

/**
 * Created by lulu on 17-4-20.
 */

public final class TuYouDbContract {

    public  static abstract class TuYouTrack implements BaseColumns {
        public static final String TABLE_NAME = "track";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_USER_ID= "userId";
        public static final String COLUMN_NAME_IMAGE_CONTAINS = "images";
//        public static final String COLUMN_NAME_VERSION = "version";
    }

    public  static abstract class TuYouVersion implements BaseColumns {
        public static final String TABLE_NAME = "tuyou_version";
        public static final String COLUMN_NAME_TRACK_VERSION = "track_version";
//        public static final String COLUMN_NAME_VERSION = "version";
    }
}
