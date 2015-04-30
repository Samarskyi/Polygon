package com.global.training.polygon.db;

import android.content.Context;
import android.util.Log;

import com.global.training.polygon.App;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by eugenii.samarskyi on 18.11.2014.
 */
public class DatabaseManager {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper mDatabaseHelper;

    public static DatabaseHelper getHelper() {
        return mDatabaseHelper;
    }

    public static synchronized void initHelper(Context context) {
        mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static synchronized void releaseHelper() {
        OpenHelperManager.releaseHelper();
        mDatabaseHelper = null;
    }

    public static boolean checkForCompleteDays(RealWorksTime time){
        boolean result = true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getDate().getTime());

        if(calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
            result = false;
        }

        return result;
    }

    public static boolean checkForCompleteDays(List<RealWorksTime> time){
        boolean result = true;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time.getDate().getTime());
//
//        if(calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
//            result = false;
//        }

        return result;
    }

    public static void saveTimeSheetToDB(final List<RealWorksTime> list){
        if (mDatabaseHelper == null) {
            initHelper(App.self());
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Dao timeSheetDao = null;
                try {
                    timeSheetDao = mDatabaseHelper.getTimeSheetDao();
                    for(RealWorksTime time : list){
                        if(checkForCompleteDays(time)){
                            timeSheetDao.createIfNotExists(time);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "TimeSheet is saved");
            }
        });

        thread.start();
    }

    public static void saveUsersToDB(final List<User> list) {
        if (mDatabaseHelper == null) {
            initHelper(App.self());
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Dao userDao = null;
                try {
                    userDao = mDatabaseHelper.getUserDao();
                    if (userDao.queryForAll().size() == 0) {
                        Log.d(TAG, "Before adding size of user table : " + userDao.queryForAll().size());
                        for (User user : list) {
                            userDao.create(user);
                        }
                        Log.d(TAG, "After adding size of user table : " + userDao.queryForAll().size());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
    public static List<User> getUsersFromDB(){
        List<User> list = null;
        try {
        if (mDatabaseHelper == null) {
            initHelper(App.self());
        }

             list = mDatabaseHelper.getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        releaseHelper();
//        if(list!= null && list.size() != 0){
////            for(User user : list){
////                Log.d(TAG, "User id: " + user.getUid() + ", user name: " + user.getFirst_name());
////            }
//        }
        return list;
    }


    public static List<RealWorksTime> getTimeSheet(long from, long till, int userId) throws SQLException {

        if (mDatabaseHelper == null) {
            initHelper(App.self());
        }

//        Calendar fromCalendar = Calendar.getInstance();
//        fromCalendar.setTimeInMillis(from);
//        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        fromCalendar.set(Calendar.SECOND, 0);
//
//        Calendar tillCalendar = Calendar.getInstance();
//        tillCalendar.setTimeInMillis(till);
//        tillCalendar.set(Calendar.HOUR_OF_DAY, 23);
//        tillCalendar.set(Calendar.SECOND, 0);
//
//        Date fromDate = fromCalendar.getTime();
//        Date toDate = tillCalendar.getTime();
//        Log.d(TAG, "From : " + fromDate.toString() + ", to :" + toDate + " , id :" + userId );

        Dao timeSheetDao = mDatabaseHelper.getTimeSheetDao();

        List<RealWorksTime> list = (List<RealWorksTime>) timeSheetDao.queryBuilder()
                .where()
                .between(RealWorksTime.DATE_FIELD, new Date(from), new Date(till))
                .and()
                .eq(RealWorksTime.ID_FIELD, userId).query();

        return list;
    }
}
