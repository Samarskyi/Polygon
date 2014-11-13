package com.global.training.polygon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.global.training.polygon.model.User;
import com.global.training.polygon.utils.Api;
import com.global.training.polygon.utils.ApiRequestInterceptor;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

public class MainActivity extends Activity implements Api.InternetCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    //    private static final String URL_EMPLOYEES = "https://portal-ua.globallogic.com/officetime/json/employees.php";
    private static final String URL_EMPLOYEES = "https://portal-ua.globallogic.com";

    interface Employees {
        @GET("/officetime/json/employees.php")
        void employeeList(Callback<List<User>> cb);
    }

    Button button;
    Employees mEmployees;
    Callback<List<User>> callback;
    List<User> users1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.addInternetCallback(this);

        callback = new Callback<List<User>>() {

            @Override
            public void success( List<User> users, Response response) {
                users1 = users;
                Log.d(TAG, "User count : " + users1.size());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Fail get users: "+ error.getMessage());
                error.printStackTrace();
            }
        };

        button = (Button) findViewById(R.id.butt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Api.auth(null, null);
//                DataBaseManager dataBaseManager = DataBaseManager.getInstance();
//                dataBaseManager.open();

                ApiRequestInterceptor requestInterceptor = new ApiRequestInterceptor("eugenii.samarskyi","[pi989898pi]!");
                RestAdapter restAdapter = new RestAdapter.
                                            Builder().setEndpoint(URL_EMPLOYEES).
                                            setRequestInterceptor(requestInterceptor).build();

                mEmployees = restAdapter.create(Employees.class);
                mEmployees.employeeList(callback);

            }
        });

    }

    @Override
    protected void onDestroy() {
        Api.removeInternetCallback(this);
        super.onDestroy();
    }

    @Override
    public void isAuthentication(boolean what) {
        Log.d(MainActivity.class.getSimpleName(), "From Callback Response " + what);
    }
}
