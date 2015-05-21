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
        try {
            mEditor.putString(LAST_USER_CREDENTIAL, loginPassForDecrypt);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        try {
            mEditor.putString(LAST_USER_CREDENTIAL, null);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCredentials(){
        return mSharedPreferences.getString(LAST_USER_CREDENTIAL, null);
    }

    public static void saveLastSeenUserID(int id){
        try {
            mEditor.putInt(LAST_SEEN_USER_ID, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEditor.commit();
    }

    public static int getLastSeenUserId() {
        return mSharedPreferences.getInt(LAST_SEEN_USER_ID, -1);
    }
}
