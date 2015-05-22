package com.global.training.polygon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.global.training.polygon.App;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class PreferencesUtils {
    private static final String PREF_NAME = PreferencesUtils.class.getName();

    private static final String LAST_USER_CREDENTIAL = "lastUser";
    private static final String LAST_SEEN_USER_ID = "";


    private static SharedPreferences mSharedPreferences = App.self().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    public static void saveCredentials(String login, String pass){
        String loginPassForDecrypt = login + " " + pass;
        mEditor.putString(LAST_USER_CREDENTIAL, loginPassForDecrypt).commit();
    }

    public static void logout() {
        mEditor.putString(LAST_USER_CREDENTIAL, null).commit();
    }

    public static String getCredentials(){
        return mSharedPreferences.getString(LAST_USER_CREDENTIAL, null);
    }

    public static void saveLastSeenUserID(int id){
        mEditor.putInt(LAST_SEEN_USER_ID, id).commit();
    }

    public static int getLastSeenUserId() {
        return mSharedPreferences.getInt(LAST_SEEN_USER_ID, -1);
    }
}
