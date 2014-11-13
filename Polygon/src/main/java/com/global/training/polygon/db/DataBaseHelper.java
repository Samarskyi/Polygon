package com.global.training.polygon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by eugenii.samarskyi on 13.11.2014.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "polygon.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = DataBaseHelper.class.getSimpleName();

    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if(context == null){
            Log.d(TAG, "OnCreate context == null");
        }
        Log.d(TAG, "OnCreate DataBaseHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table employees ("
                + "_id integer primary key,"
                + "zone text,"
                + "first_name text,"
                + "last_name text" + ");");

//        db.execSQL("create table mytable ("
//                + "id integer primary key autoincrement,"
//                + "name text,"
//                + "email text" + ");");

        Log.d(TAG, "DataBaseHelper is created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
