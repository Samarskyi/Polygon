package com.global.training.polygon;

import android.app.Application;
import android.util.Log;

/**
 * @author  eugenii.samarskyi on 12.11.2014.
 */
public class App extends Application {

	private static App sSelf;

	@Override
	public void onCreate() {
		super.onCreate();
        Log.d(App.class.getSimpleName(), "App onCreate");
        sSelf = this;

	}

	public static App self() {
		return sSelf;
	}

}
