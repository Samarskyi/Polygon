package com.global.training.polygon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.global.training.polygon.App;
import com.global.training.polygon.model.User;

/**
 * @author  eugenii.samarskyi on 12.11.2014.
 */
public class PreferencesUtils {

    private static final String PREF_NAME = PreferencesUtils.class.getName();
    private static final String TAG = SharedPreferences.class.getSimpleName();

    private static SharedPreferences mSharedPreferences = App.self().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    public static void saveLastUser(String login, String pass){

        String loginPassForDecrypt = login + " " + pass;
        try {
            mEditor.putString("lastUser", loginPassForDecrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEditor.commit();
    }

    public static String getLastUser(){

//        try {
//            lastUser = EncryptionUtils.decrypt("123456789", lastUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return mSharedPreferences.getString("lastUser", null);
    }

}
