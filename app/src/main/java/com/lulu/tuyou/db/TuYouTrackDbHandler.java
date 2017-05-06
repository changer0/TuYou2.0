package com.lulu.tuyou.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import com.lulu.tuyou.db.DbConstant;

/**
 * Created by lulu on 17-4-20.
 * 图友圈 本地数据库的处理类
 */

public class TuYouTrackDbHandler {
    private Context mContext;
    private SQLiteOpenHelper dbHelper;
    private List<TuYouTrack> mTracks;
    //    public static final int DB_DATA_RECEIVE_TYPE_GET = 0x4;




    public TuYouTrackDbHandler(Context context) {
        mContext = context;
        dbHelper = DbHelper.getInstance(mContext);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 对外开放的方法
    ///////////////////////////////////////////////////////////////////////////


    public synchronized int clearTracks() {
        SQLiteDatabase db = null;
        int state;
        try {
            db = dbHelper.getReadableDatabase();
            db.delete(TuYouDbContract.TuYouTrack.TABLE_NAME, null, null);
            state =  DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS;
        } catch (Exception e) {
            state =  DbConstant.DB_DATA_RECEIVE_STATE_ERROR;
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return state;
    }


    public synchronized int addTracks(final List<TuYouTrack> tracks) {
        int state;
        SQLiteDatabase db = null;
        Message msg = Message.obtain();
        try {
            db = dbHelper.getReadableDatabase();
            for (TuYouTrack track : tracks) {
                addTracks(db, track);
            }
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

    public synchronized void updateTrack(final TuYouTrack track) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = null;
                try {
                    db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TuYouDbContract.TuYouTrack._ID, track.getObjectId());
                    values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID, track.getUser().getObjectId());
                    values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT, track.getText());
                    values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_UPDATE_TIME, track.getUpdatedAt());
                    values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS, Utils.getStringsJson(track.getImages()));
                    values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_TYPE, track.getType());
                    db.update(TuYouDbContract.TuYouTrack.TABLE_NAME, values,
                            "_ID=?", new String[]{track.getObjectId().toString()}
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                }
            }
        }).start();

    }

    public synchronized List<TuYouTrack> getTracks(Message msg) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<TuYouTrack> tracks = null;
        try {
            tracks = new ArrayList<>();
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
                String updateTime = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouTrack.COLUMN_NAME_UPDATE_TIME));
                track.setUpdatedAt(updateTime);
                String type = cursor.getString(cursor.getColumnIndex(TuYouDbContract.TuYouTrack.COLUMN_NAME_TYPE));
                track.setType(Integer.parseInt(type));
                tracks.add(track);
            }
            msg.arg1 = DbConstant.DB_DATA_RECEIVE_STATE_SUCCESS;
        } catch (Exception e) {
            msg.arg1 = DbConstant.DB_DATA_RECEIVE_STATE_ERROR;
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
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


    private synchronized void addTracks(SQLiteDatabase db, TuYouTrack track) {
        ContentValues values = new ContentValues();
        values.put(TuYouDbContract.TuYouTrack._ID, track.getObjectId());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_USER_ID, track.getUser().getObjectId());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_TEXT, track.getText());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_UPDATE_TIME, track.getUpdatedAt());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_TYPE, track.getType());
        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_IMAGE_CONTAINS, Utils.getStringsJson(track.getImages()));
//        values.put(TuYouDbContract.TuYouTrack.COLUMN_NAME_VERSION, track.getVersionId());
        db.insert(TuYouDbContract.TuYouTrack.TABLE_NAME, null, values);
    }



//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case DB_DATA_RECEIVE_TYPE_ADD://添加
//                    mDataListener.onReceive(DB_DATA_RECEIVE_TYPE_ADD, null, msg.arg1);
//                    break;
//                case DB_DATA_RECEIVE_TYPE_GET_TRACKS:
//                    mDataListener.onReceive(DB_DATA_RECEIVE_TYPE_GET_TRACKS, mTracks, msg.arg1);
//                    break;
//                case DB_DATA_RECEIVER_TYPE_CLEAR:
//                    mDataListener.onReceive(DB_DATA_RECEIVER_TYPE_CLEAR, null, msg.arg1);
//                    break;
//
//            }
//
//        }
//    };
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    //数据回调接口
//    ///////////////////////////////////////////////////////////////////////////
//    public interface OnDataReceiveListener {
//        void onReceive(int type, List<TuYouTrack> tracks, int state);
//    }
//    private OnDataReceiveListener mDataListener;
//
//    public void setDataListener(OnDataReceiveListener dataListener) {
//        mDataListener = dataListener;
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
