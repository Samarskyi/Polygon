package com.global.training.polygon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.global.training.polygon.App;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class PreferencesUtils {

    private final String PREF_NAME = PreferencesUtils.class.getName();

    private SharedPreferences mSharedPreferences = App.self().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    private SharedPreferences.Editor mEditor = mSharedPreferences.edit();





    public void saveLastUser(String login, String pass) {
        mEditor.putString("login", login);
        mEditor.putString("password", pass);
        mEditor.commit();
    }


    public void saveLastUserLogin(String login) {
        mEditor.putString("login", login);
        mEditor.commit();
    }

    public void saveLastUserPassword(String pass) {
        mEditor.putString("password", pass);
        mEditor.commit();
    }

    public void saveLastUserId(int id) {
        mEditor.putInt("id", id);
        mEditor.commit();
    }


}
