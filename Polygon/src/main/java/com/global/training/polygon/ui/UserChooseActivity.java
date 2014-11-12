package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.global.training.polygon.R;

/**
 * @author yurii.ostrovskyi
 */
public class UserChooseActivity extends Activity {

	private String[] mEmployee = new String[] {
			"YuriiO", "Sergiy", "Evgenii", "Rostyk", "Sasha", "John", "Bobby", "Vasia", "Tom"
			, "Jackson", "Dmitrii", "YuriiBeear", "YuriiWolf", "YuriiFox"
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_user_choose);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mEmployee);
		AutoCompleteTextView textView = (AutoCompleteTextView)
				findViewById(R.id.user_search_field);
		textView.setAdapter(adapter);
		textView.setOnEditorActionListener(new ImeActionListener());
	}

	private class ImeActionListener implements TextView.OnEditorActionListener {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startActivity(new Intent(UserChooseActivity.this, TimeSheetActivity.class));
				return true;
			}
			return false;
		}
	}
}
