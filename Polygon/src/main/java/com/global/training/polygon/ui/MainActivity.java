package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.global.training.polygon.R;
import com.global.training.polygon.utils.Api;
import com.global.training.polygon.utils.PreferencesUtils;
import com.mobprofs.retrofit.converters.SimpleXmlConverter;

public class MainActivity extends Activity implements View.OnClickListener, Api.AuthCallback{

	private EditText mLoginField;
	private EditText mPasswordField;

	private String mLogin;
	private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

		autoLoginLastUser();

		Button loginButton = (Button) findViewById(R.id.login_btn);
		mLoginField = (EditText) findViewById(R.id.login);
		mPasswordField = (EditText) findViewById(R.id.password);

		mLoginField.setNextFocusDownId(R.id.password);
		mPasswordField.setOnEditorActionListener(new ImeActionListener());
		loginButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		initiateAuth();
	}

	@Override
	public void isAuthentication(boolean what) {
		if (what) {
			auth(mLogin, mPassword);
		} else {
			Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show();
			mPasswordField.setText("");
		}
	}

	private void initiateAuth() {
		mLogin = mLoginField.getText().toString();
		mPassword =  mPasswordField.getText().toString();
		Api.auth(mLogin, mPassword, this);
	}
	private void auth (String login, String password) {
		startActivity(new Intent(this, UserChooseActivity.class));
		PreferencesUtils.saveLastUser(login, password);
	}

	private void autoLoginLastUser() {
		if (PreferencesUtils.getLastUser() != null) {
			String[] logPas = PreferencesUtils.getLastUser().split(" ");
			auth(logPas[0], logPas[1]);
		}
	}

	private class ImeActionListener implements TextView.OnEditorActionListener {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				initiateAuth();
				return true;
			}
			return false;
		}
	}
}
