package com.global.training.polygon;

import android.app.Application;

/**
 * @author  eugenii.samarskyi on 12.11.2014.
 */
public class App extends Application {

	private static App sSelf;

	@Override
	public void onCreate() {
		super.onCreate();
        sSelf = this;
	}

	public static App self() {
		return sSelf;
	}

}
