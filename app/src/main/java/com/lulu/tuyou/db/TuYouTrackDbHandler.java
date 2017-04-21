package com.lulu.tuyou.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;

import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lulu on 17-4-20.
 * 图友圈 本地数据库的处理类
 */

public class TuYouTrackDbHandler {
    private Context mContext;
    private SQLiteOpenHelper dbHelper;
    public static final int DB_DATA_RECEIVE_STATE_SUCCESS = 0x1;
    public static final int DB_DATA_RECEIVE_STATE_ERROR = 0x2;
    public static final int DB_DATA_RECEIVE_TYPE_GET_TRACKS = 0x3;
    public static final int DB_DATA_RECEIVE_TYPE_ADD = 0x4;
//    public static final int DB_DATA_RECEIVE_TYPE_GET = 0x4;




    public TuYouTrackDbHandler(Context context) {
        mContext = context;
        dbHelper = DbHelper.getInstance(mContext);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 对外开放的方法
    ///////////////////////////////////////////////////////////////////////////
    public synchronized void getTracksFromDb(OnDataReceiveListener listener) {
        mDataListener = listener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = DB_DATA_RECEIVE_TYPE_GET_TRACKS;
                msg.obj = getTracks();
                mHandler.sendMessage(msg);
            }
        }).start();
    }






    public synchronized void updateTrack(final TuYouTrack track) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(TuYouDbContract.TuYouTrack._ID, track.getObjectId());
                values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID, track.getUser().getObjectId());
                values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT, track.getText());
                values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS, Utils.getStringsJson(track.getImages()));
                db.update(TuYouDbContract.TuYouTrack.TABLE_NAME, values,
                        "_ID=?", new String[]{track.getObjectId().toString()}
                );
            }
        }).start();

    }

    private synchronized List<TuYouTrack> getTracks() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<TuYouTrack> tracks = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        cursor = db.query(TuYouDbContract.TuYouTrack.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            TuYouTrack track = new TuYouTrack();
            int newId = cursor.getInt(cursor.getColumnIndex(TuYouDbContract.TuYouTrack._ID));
            track.setObjectId(String.valueOf(newId));
            String newUserId = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID));
            TuYouUser user = new TuYouUser();
            user.setObjectId(newUserId);
            track.setUser(user);
            String newText = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT));
            track.setText(newText);
            String newImages = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS));
            track.setImages(Utils.genStringListFromJson(newImages));

            tracks.add(track);
        }
        return tracks;
    }

//    public synchronized void addTrack(final TuYouTrack track) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase db = null;
//                db = dbHelper.getReadableDatabase();
//                addTracks(db, track);
//            }
//        });
//
//    }

    public synchronized void addTracks(final List<TuYouTrack> tracks, OnStateListener listener) {
        mAddListener = listener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;
                db = dbHelper.getReadableDatabase();
                for (TuYouTrack track : tracks) {
                    addTracks(db, track);
                }
                mHandler.sendEmptyMessage(DB_DATA_RECEIVE_TYPE_ADD);
            }
        }).start();

    }

    private synchronized void addTracks(SQLiteDatabase db, TuYouTrack track) {
        ContentValues values = new ContentValues();
        values.put(TuYouDbContract.TuYouTrack._ID, track.getObjectId());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID, track.getUser().getObjectId());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT, track.getText());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS, Utils.getStringsJson(track.getImages()));
//        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_VERSION, track.getVersionId());

        db.insert(TuYouDbContract.TuYouTrack.TABLE_NAME, null, values);
    }



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case DB_DATA_RECEIVE_TYPE_GET_TRACKS:
                    if (mDataListener != null) {
                        mDataListener.onReceive(DB_DATA_RECEIVE_TYPE_GET_TRACKS, ((List<TuYouTrack>) msg.obj));
                    }
                    break;
                case DB_DATA_RECEIVE_TYPE_ADD:
                    if (mAddListener != null) {
                        mAddListener.onState(DB_DATA_RECEIVE_STATE_SUCCESS);
                    }
                    break;
            }

        }
    };


    ///////////////////////////////////////////////////////////////////////////
    //数据回调接口
    ///////////////////////////////////////////////////////////////////////////
    public interface OnDataReceiveListener {
        void onReceive(int type, List<TuYouTrack> tracks);
    }
    private OnDataReceiveListener mDataListener;

    public interface OnStateListener {
        void onState(int state);
    }
    private OnStateListener mAddListener;

//    public void setmListener(OnDataReceiveListener mDataListener) {
//        this.mDataListener = mDataListener;
//    }

    ///////////////////////////////////////////////////////////////////////////
    // 数据库帮助类
    ///////////////////////////////////////////////////////////////////////////
//    private class TuYouTrackDbHelper extends SQLiteOpenHelper {
//        public static final String DATABASE_NAME = "TuYouTrack.db";
//        public static final String DATABASE_TYPE = " TEXT";
//        public static final int DATABASE_VERSION = 1;
//
//        private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TuYouDbContract.TuYouTrack.TABLE_NAME +
//                "(" + TuYouDbContract.TuYouTrack._ID + " INT PRIMARY KEY," +
//                TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID + DATABASE_TYPE + "," +
//                TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT + DATABASE_TYPE + "," +
//                TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS + DATABASE_TYPE + ")";
//
//        public TuYouTrackDbHelper(Context context) {
//            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            Log.d("lulu", "onCreate:创建表:" + SQL_CREATE_ENTRIES);
//            db.execSQL(SQL_CREATE_ENTRIES);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        }
//    }

}
