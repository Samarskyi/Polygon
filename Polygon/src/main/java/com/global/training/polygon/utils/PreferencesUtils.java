package com.global.training.polygon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.global.training.polygon.App;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class PreferencesUtils {
    private static final String PREF_NAME = PreferencesUtils.class.getName();

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
        return mSharedPreferences.getString("lastUser", null);
    }

}
