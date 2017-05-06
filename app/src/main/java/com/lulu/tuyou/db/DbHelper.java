package com.lulu.tuyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lulu on 17-4-21.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TuYouTrack.db";
    public static final String DATABASE_TYPE = " TEXT";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TRACK = "CREATE TABLE " + TuYouDbContract.TuYouTrack.TABLE_NAME +
            "(" + TuYouDbContract.TuYouTrack._ID + " INT PRIMARY KEY," +
            TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID + DATABASE_TYPE + "," +
            TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT + DATABASE_TYPE + "," +
            TuYouDbContract.TuYouTrack.COLUMN_NAME_UPDATE_TIME + DATABASE_TYPE + "," +
            TuYouDbContract.TuYouTrack.COLUMN_NAME_TYPE + DATABASE_TYPE + "," +
            TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS + DATABASE_TYPE + ")";

    private static final String SQL_CREATE_VERSION = "CREATE TABLE " + TuYouDbContract.TuYouVersion.TABLE_NAME +
            "(" + TuYouDbContract.TuYouVersion._ID + " INT PRIMARY KEY," +
            TuYouDbContract.TuYouVersion.COLUMN_NAME_TRACK_VERSION + DATABASE_TYPE + ")";

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DbHelper instance = null;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    instance = new DbHelper(context);
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("lulu", "onCreate:创建表:" + SQL_CREATE_TRACK);
        db.execSQL(SQL_CREATE_TRACK);
        db.execSQL(SQL_CREATE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
