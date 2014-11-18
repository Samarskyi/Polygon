package com.global.training.polygon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by eugenii.samarskyi on 18.11.2014.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "polygon.db";
    private static final int DATABASE_VERSION = 1;

    private Dao userDao = null;
    private Dao timeSheetDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, RealWorksTime.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getSimpleName(), "error creating DB " + DATABASE_NAME);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao getUserDao() throws SQLException{
        if(userDao == null){
            userDao =  getDao(User.class);
        }
        return userDao;
    }

    public Dao getTimeSheetDao() throws SQLException{
        if(timeSheetDao == null){
            timeSheetDao = getDao(RealWorksTime.class);
        }
        return timeSheetDao;
    }

    @Override
    public void close(){
        super.close();
        userDao = null;
        timeSheetDao = null;
    }
}
