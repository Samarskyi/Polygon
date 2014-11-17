package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.TextView;
import com.global.training.polygon.R;

/**
 * @author yurii.ostrovskyi
 */
public class TimeSheetActivity extends Activity implements AdapterView.OnClickListener {

	private TextView mTimeOracle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_timesheet);

		mTimeOracle = (TextView) findViewById(R.id.time_or_oracle);

		mTimeOracle.setEnabled(true);
		mTimeOracle.setText(R.string.time_oracle_selected_time);
		mTimeOracle.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
		mTimeOracle.setOnClickListener(this);

		XmlResourceParser xrp = getResources().getXml(R.drawable.time_oracle_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
			mTimeOracle.setTextColor(csl);
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	@Override
	public void onClick(View v) {
	Log.d("MyLog", "is selected " +mTimeOracle.isSelected());
		if(mTimeOracle.isSelected()) {
			mTimeOracle.setText(R.string.time_oracle_selected_time);
			mTimeOracle.setSelected(false);
		} else {
			mTimeOracle.setText(R.string.time_oracle_selected_oracle);
			mTimeOracle.setSelected(true);
		}
	}
}
