package com.global.training.polygon.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class PreferencesUtils {

	private SharedPreferences mSharedPreferences;
	private final String PREF_NAME = PreferencesUtils.class.getName();
	private SharedPreferences.Editor editor;


	public PreferencesUtils() {
		mSharedPreferences = App.self().getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
		editor = mSharedPreferences.edit();
	}

	public void saveLastUserLogin(String login){
		editor.putString("login",login);
		editor.commit();
	}

	public void saveLastUserPassword(String pass){
		editor.putString("password", pass);
		editor.commit();
	}

	public void saveLastUserId(int id){
		editor.putInt("id",id);
		editor.commit();
	}


}
