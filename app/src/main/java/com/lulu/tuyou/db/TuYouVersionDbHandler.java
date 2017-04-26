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

    public TuYouVersionDbHandler(Context context) {
        mContext = context;
        dbHelper = DbHelper.getInstance(mContext);
    }

    public synchronized int setTrackVersion(final int version) {
        int state;
        String versionStr = String.valueOf(version);
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TuYouDbContract.TuYouVersion.COLUMN_NAME_TRACK_VERSION, versionStr);
            db.insert(TuYouDbContract.TuYouVersion.TABLE_NAME, null, values);
            state = DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS;
        } catch (Exception e) {
            state = DbConstant.DB_DATA_RECEIVE_STATE_ERROR;
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return state;
    }


    public synchronized int getTrackVersion (Message msg) {
        int ret = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
           db = dbHelper.getReadableDatabase();
            cursor = db.query(TuYouDbContract.TuYouVersion.TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String version = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouVersion.COLUMN_NAME_TRACK_VERSION));
                ret = Integer.valueOf(version);
            }
            msg.arg1 = DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS;
        } catch (NumberFormatException e) {
            msg.arg1 = DbConstant.DB_DATA_RECEIVE_STATE_ERROR;
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }

//    public interface onGetTrackVersionListener {
//        void onGetTrackVersion(int version);
//    }
//    private onGetTrackVersionListener mTrackVersionListener = null;





}
