package com.global.training.polygon.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import com.global.training.polygon.R;

/**
 * @author yurii.ostrovskyi
 */
public class TimeSheetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_timesheet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.change_user:
				return true;
			case R.id.logout:
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
