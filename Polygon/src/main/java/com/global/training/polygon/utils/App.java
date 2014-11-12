package com.global.training.polygon.utils;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class App extends Application {

	private static App sSelf;
	private Handler mHandler;

	@Override
	public void onCreate() {
		super.onCreate();

		sSelf = this;
		mHandler = new Handler();
	}

	public static App self() {
		return sSelf;
	}

	public void showToast(final CharSequence message){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
//				Toast.makeText(App.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
