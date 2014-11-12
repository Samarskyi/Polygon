package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.global.training.polygon.R;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

		Button loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, UserChooseActivity.class));
	}
}
