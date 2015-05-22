package com.global.training.polygon.db;

import android.content.Context;

import com.global.training.polygon.App;
import com.global.training.polygon.model.RealWorksTime;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
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

    public static void checkIfInited() {
        if (mDatabaseHelper == null) {
            initHelper(App.self());
        }
    }

    public static synchronized void releaseHelper() {
        OpenHelperManager.releaseHelper();
        mDatabaseHelper = null;
    }

    public static <T> List<T> getAll(Class<T> clazz) throws SQLException {
        checkIfInited();

        Dao<T, ?> dao = mDatabaseHelper.getDao(clazz);
        return dao.queryForAll();
    }

    public static <T> T createOrUpdate(T ob) throws SQLException {
        checkIfInited();

        Dao<Object, ?> dao = (Dao<Object, ?>) mDatabaseHelper.getDao(ob.getClass());
        return (T) dao.createOrUpdate(ob);
    }

    public static <T> int update(T ob) throws SQLException {
        checkIfInited();

        Dao<Object, ?> dao = (Dao<Object, ?>) mDatabaseHelper.getDao(ob.getClass());
        return dao.update(ob);
    }

    public static <T> int delete(T ob) throws SQLException {
        checkIfInited();

        Dao<Object, ?> dao = (Dao<Object, ?>) mDatabaseHelper.getDao(ob.getClass());
        return dao.delete(ob);
    }

    public static <T> T getById(Object aId, Class<T> clazz) throws SQLException {
        checkIfInited();

        Dao<T, Object> dao = mDatabaseHelper.getDao(clazz);
        return dao.queryForId(aId);
    }

    public static <T> int deleteAll(Class<T> clazz) throws SQLException {
        checkIfInited();

        Dao<Object, ?> dao = (Dao<Object, ?>) mDatabaseHelper.getDao(clazz);
        return dao.deleteBuilder().delete();
    }


    public static List<RealWorksTime> getTimeSheet(long from, long till, int userId) throws SQLException {
        checkIfInited();
        List<RealWorksTime> list = null;
        return list;
    }

    public static <T> List<T> getByFieldValueLike(boolean not, String column, Object value, String secondColumn, Object secondValue,
                                                  Class<T> clazz) throws SQLException {
        checkIfInited();

        Dao<T, Object> dao = mDatabaseHelper.getDao(clazz);

        QueryBuilder<T, Object> queryBuilder = dao.queryBuilder();
        if (not) {
            queryBuilder.where().not().like(column, "%" + value + "%");
        } else {
            queryBuilder.where().like(column, "%" + value + "%").or().like(secondColumn, "%" + secondValue + "%");
        }
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        List<T> result = dao.query(preparedQuery);

        return result;
    }

    public static <T> List<T> getAllByFieldValue(String column, Object value, Class<T> clazz)
            throws SQLException {
        checkIfInited();

        Dao<T, Object> dao = mDatabaseHelper.getDao(clazz);

        QueryBuilder<T, Object> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq(column, value);
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        List<T> result = dao.query(preparedQuery);

        if (result == null || result.size() == 0)
            return null;
        else
            return result;
    }

    public static <T> List<T> getAllByFieldValueAnd(String column, Object value, String andColumn,
                                                    Object andValue, Class<T> clazz) throws SQLException {
        checkIfInited();

        Dao<T, Object> dao = mDatabaseHelper.getDao(clazz);

        QueryBuilder<T, Object> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq(column, value).and().eq(andColumn, andValue);
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        List<T> result = dao.query(preparedQuery);

        if (result == null || result.size() == 0)
            return null;
        else
            return result;
    }

}
