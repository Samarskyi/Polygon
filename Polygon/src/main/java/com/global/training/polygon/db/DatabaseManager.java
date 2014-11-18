package com.global.training.polygon.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by eugenii.samarskyi on 18.11.2014.
 */
public class DatabaseManager {

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getHelper(){
        return databaseHelper;
    }

    public static void initHelper(Context context){
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}
