package com.lulu.tuyou.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

/**
 * Created by lulu on 17-4-21.
 */

public class TuYouVersionDbHandler {

    private Context mContext;
    private DbHelper dbHelper;
    public static final int DB_TRACK_VERSION = 0x1;

    public TuYouVersionDbHandler(Context context) {
        mContext = context;
        dbHelper = DbHelper.getInstance(mContext);
    }

    public void setTrackVersion(final int version) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String versionStr = String.valueOf(version);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(TuYouDbContract.TuYouVersion.COLUMN_NAME_TRACK_VERSION, versionStr);
                db.insert(TuYouDbContract.TuYouVersion.TABLE_NAME, null, values);
            }
        }).start();
    }

    public void getTrackVersionFromDb(onGetTrackVersionListener listener) {
        mTrackVersionListener = listener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = DB_TRACK_VERSION;
                msg.obj = getTrackVersion();
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    public interface onGetTrackVersionListener {
        void onGetTrackVersion(int version);
    }
    private onGetTrackVersionListener mTrackVersionListener = null;

    private int getTrackVersion () {
        int ret = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TuYouDbContract.TuYouVersion.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String version = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouVersion.COLUMN_NAME_TRACK_VERSION));
            ret = Integer.valueOf(version);
        }
        return ret;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DB_TRACK_VERSION:
                    if (mTrackVersionListener != null) {
                        int version = (int) msg.obj;
                        mTrackVersionListener.onGetTrackVersion(version);
                    }
                    break;
            }
        }
    };




}
