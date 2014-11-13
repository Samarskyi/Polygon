package com.global.training.polygon.db;

import android.util.Log;

import com.global.training.polygon.App;

/**
 * Created by eugenii.samarskyi on 13.11.2014.
 */
public class DataBaseManager {

    private static final String TAG = DataBaseManager.class.getSimpleName();
    private static DataBaseManager mDataBaseManager;
    private DataBaseHelper mDataBaseHelper;

    private DataBaseManager() {
        Log.d(TAG, "DataBaseManager constructor");
        mDataBaseHelper = new DataBaseHelper(App.self());

    }

    public static DataBaseManager getInstance(){
        if (mDataBaseManager == null){
            mDataBaseManager = new DataBaseManager();
        }
        Log.d(TAG, "DataBaseManager getInstance");
        return mDataBaseManager;
    }

    public void open(){
        Log.d(TAG, "DataBaseManager open");
        mDataBaseHelper.getReadableDatabase();
    }


}
