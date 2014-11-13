package com.global.training.polygon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.global.training.polygon.model.User;
import com.global.training.polygon.utils.Api;

import java.util.List;

public class MainTestActivity extends Activity implements Api.AuthCallback, Api.EmployeesCallback {

    private static final String TAG = MainTestActivity.class.getSimpleName();

    Button button;
    String lName;
    String fName;
    String pass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         fName = "eugenii";
         lName = "samarskyi";
         pass = "[pi989898pi]!";

        button = (Button) findViewById(R.id.butt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Api.auth(fName+ "." + lName, pass, MainTestActivity.this);
//                  Api.getUsers(MainTestActivity.this);

                Log.d(TAG, "Before " +  fName);
//                fName =

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void isAuthentication(boolean what) {
//        Log.d(MainTestActivity.class.getSimpleName(), "From Callback Auth " + what);
//        if(what){
//            PreferencesUtils.saveLastUser(new User(fName,lName),pass);
//        }
    }

    @Override
    public void getUserList(List<User> list) {
        Log.d(TAG, "From Callback GetUserList " + list.size());
    }

}
