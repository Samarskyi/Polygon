package com.global.training.polygon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.global.training.polygon.App;
import com.global.training.polygon.model.User;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class PreferencesUtils {

    private static final String PREF_NAME = PreferencesUtils.class.getName();
    private static final String TAG = SharedPreferences.class.getSimpleName();

    private static SharedPreferences mSharedPreferences = App.self().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    public static void saveLastUser(User user, String pass){
        Log.d(TAG, "Save user to pref:" + user.getFirst_name() + ",  " + user.getLast_name());
        mEditor.putString("lastUser", user.getFirst_name()+ " "+ user.getLast_name()+ " "+ pass);
        mEditor.commit();
    }

    public static String getLastUser(){
       String lastUser = mSharedPreferences.getString("lastUser", null);
       return lastUser;
    }

}
