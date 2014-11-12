package com.global.training.polygon.ui;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import com.global.training.polygon.R;

/**
 * @author yurii.ostrovskyi
 */
public class TimeSheetActivity extends Activity {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.change_user:
				return true;
			case R.id.logout:
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
