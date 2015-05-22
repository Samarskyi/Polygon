package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.global.training.polygon.App;
import com.global.training.polygon.R;
import com.global.training.polygon.db.DatabaseManager;
import com.global.training.polygon.model.User;
import com.global.training.polygon.utils.Api;
import com.global.training.polygon.utils.PreferencesUtils;

import java.sql.SQLException;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener, Api.AuthCallback, Api.EmployeesCallback {

	private EditText mLoginField;
	private EditText mPasswordField;

	private String mLogin;
	private String mPassword;

	private StubLoginCredentials stubLoginCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

		autoLoginLastUser();

		Button loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(this);

		mLoginField = (EditText) findViewById(R.id.login);
		mLoginField.setNextFocusDownId(R.id.password);

		mPasswordField = (EditText) findViewById(R.id.password);
		mPasswordField.setOnEditorActionListener(new ImeActionListener());

		stubLoginCredentials = new StubLoginCredentials();
		mLoginField.setOnLongClickListener(stubLoginCredentials);
		mPasswordField.setOnLongClickListener(stubLoginCredentials);
	}

	@Override
	public void onClick(View v) {
		initiateAuth();
	}

	@Override
	public void isAuthentication(boolean success) {
		if (success) {
			PreferencesUtils.saveCredentials(mLogin, mPassword);
			Api.getUsers(this);
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(App.self(), "Authentication failed", Toast.LENGTH_LONG).show();
					mPasswordField.setText("");
				}
			});
		}
	}

	private void initiateAuth() {
		mLogin = mLoginField.getText().toString();
		mPassword =  mPasswordField.getText().toString();
		Api.auth(mLogin, mPassword, this);
	}

    private void autoLoginLastUser() {
        if (PreferencesUtils.getCredentials() != null) {
			Intent userChooserActivity = new Intent(this, TimeSheetActivity.class);
			startActivity(userChooserActivity);
			finish();
		}
	}

	@Override
	public void getUserList(List<User> list) {
		String[] infoSplit = PreferencesUtils.getCredentials().split(" ");
		Log.d("XXX", "Credentials: " + infoSplit[0]);

		if (list == null || list.size() == 0) {
			Log.e("XXX", "LIST IS EMPTY");
			return;
		}

		for (User currentUser : list) {
			if ((currentUser.getFirst_name() + "." + currentUser.getLast_name()).toLowerCase().contains(infoSplit[0])) {
					PreferencesUtils.saveLastSeenUserID(currentUser.getUid());
			}
		}

		new Thread(new UsersSaverThread(list)).start();

		Intent timeSheetActivity = new Intent(this, TimeSheetActivity.class);
		startActivity(timeSheetActivity);
		finish();
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

	private class StubLoginCredentials implements View.OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			if (v.getId() == R.id.login) {
				mLoginField.setText("eugenii.samarskyi");
				return true;
			} else if (v.getId() == R.id.password) {
				mPasswordField.setText("[PA989898pa]!");
				return true;
			}
			return false;
		}
	}

	private class UsersSaverThread implements Runnable {

		private List<User> users;

		public UsersSaverThread(List<User> userList) {
			users = userList;
		}

		@Override
		public void run() {
			Log.d("XXX", "Start saving users, size of list: " + users.size());
			for (User user : users) {
				try {
					DatabaseManager.createOrUpdate(user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
