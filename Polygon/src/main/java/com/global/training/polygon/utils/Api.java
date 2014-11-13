package com.global.training.polygon.utils;

import android.util.Log;

import com.global.training.polygon.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * @author  eugenii.samarskyi on 12.11.2014.
 */
public class Api {

    private static final String URL_EMPLOYEES = "https://portal-ua.globallogic.com/officetime/json/employees.php";
    private static final String TAG = Api.class.getSimpleName();

    private static Employees mEmployees;
    private static Callback<List<User>> callback;


    public static void getUsers(final EmployeesCallback employeesCallback) {

        callback = new Callback<List<User>>() {

            @Override
            public void success(List<User> users, Response response) {
                employeesCallback.getUserList(users);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Fail get users: " + error.getMessage());
                error.printStackTrace();
            }
        };

        String userInfo = PreferencesUtils.getLastUser();
        String[] infoSplit = userInfo.split(" "); // 0 - first_name, 1 - last_name, 2 - password

        ApiRequestInterceptor requestInterceptor = new ApiRequestInterceptor(infoSplit[0] + "." + infoSplit[1], infoSplit[2]);
        RestAdapter restAdapter = new RestAdapter.
                Builder().setEndpoint(URL_EMPLOYEES).
                setRequestInterceptor(requestInterceptor).build();

        mEmployees = restAdapter.create(Employees.class);
        mEmployees.employeeList(callback);
    }


    public static void auth(final String login, final String pass, final AuthCallback authCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(URL_EMPLOYEES);
                    httpGet.addHeader(BasicScheme.authenticate(
                            new UsernamePasswordCredentials(login, pass), "UTF-8", false));

                    HttpResponse httpResponse = httpclient.execute(httpGet);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    boolean result = false;
                    if (statusCode == 200) {
                        result = true;
                    }

                    authCallback.isAuthentication(result);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface AuthCallback {
        public void isAuthentication(boolean what);
    }

    public interface EmployeesCallback {
        public void getUserList(List<User> list);
    }

    interface Employees {
        @GET("/officetime/json/employees.php")
        void employeeList(Callback<List<User>> retrofitCallback);
    }

}
