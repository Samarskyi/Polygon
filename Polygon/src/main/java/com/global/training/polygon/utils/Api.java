package com.global.training.polygon.utils;

import android.text.TextUtils;
import android.util.Log;

import com.global.training.polygon.db.DatabaseManager;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.TimeCounter;
import com.global.training.polygon.model.User;
import com.global.training.polygon.model.WorksTime;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * @author eugenii.samarskyi on 12.11.2014.
 */
public class Api {

    private static final String URL_GLOBAL_LOGIC = "https://portal-ua.globallogic.com";
    private static final String URL_EMPLOYEES = "https://portal-ua.globallogic.com/officetime/json/employees.php";
    private static final String TAG = Api.class.getSimpleName();

    private static Employees mEmployees;
    private static OfficeTime officeTime;

    private static Callback<List<User>> usersCallback;
    private static Callback<List<WorksTime>> timeCallback;

    public static void auth(final String login, final String pass, final AuthCallback authCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(URL_EMPLOYEES);

                    //TODO login stub
                    if (TextUtils.isEmpty(login)) {
                        Log.d("XXX", "Login is empty! Use stub");
                        httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials("eugenii.samarskyi", "[PA989898pa]!"), "UTF-8", false));
                    } else {
                        httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(login, pass), "UTF-8", false));
                    }
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

    public static void getUsers(final EmployeesCallback employeesCallback) {

        List<User> userList = DatabaseManager.getUsersFromDB();
        if (userList.size() != 0) {
            employeesCallback.getUserList(userList);
            return;
        }

        String userInfo = PreferencesUtils.getCredentials();
        String[] infoSplit = userInfo.split(" "); // 0 - login, 2 - password
        ApiRequestInterceptor requestInterceptor = null;
        if (infoSplit.length == 0) {
            Log.d("XXX", "Login is empty! Use stub");
            requestInterceptor = new ApiRequestInterceptor("eugenii.samarskyi", "[PA989898pa]!");
        } else {
            requestInterceptor = new ApiRequestInterceptor(infoSplit[0], infoSplit[1]);
        }

        RestAdapter restAdapter = new RestAdapter.
                Builder().setEndpoint(URL_EMPLOYEES).
                setRequestInterceptor(requestInterceptor).build();

        usersCallback = new Callback<List<User>>() {

            @Override
            public void success(List<User> users, Response response) {
                employeesCallback.getUserList(users);
                DatabaseManager.saveUsersToDB(users);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Fail get users: " + error.getMessage());
                error.printStackTrace();
            }
        };

        mEmployees = restAdapter.create(Employees.class);
        mEmployees.employeeList(usersCallback);
    }

    public static void timeWork(long from, long till, final int userId, final OfficeTimeCallback officeTimeCallback) {
        Log.d(TAG, "New callback, userId : " + userId + ". From : " + new Date(from) + ", till :" + new Date(till));
        /*try {
            List<RealWorksTime> list = DatabaseManager.getTimeSheet(from, till, userId);
            Log.d(TAG, list.size() + ": size of list ");
            if (list != null) {
                for (RealWorksTime realWorksTime : list) {
                    Log.d(TAG, realWorksTime.toString());
                }
                officeTimeCallback.getTimeList(list);
//                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        String userInfo = PreferencesUtils.getCredentials();
        String[] infoSplit = userInfo.split(" "); // 0 - login, 1 - password

        ApiRequestInterceptor requestInterceptor = new ApiRequestInterceptor(infoSplit[0], infoSplit[1]);
        RestAdapter restAdapter = new RestAdapter.Builder().
                setEndpoint(URL_GLOBAL_LOGIC).
                setRequestInterceptor(requestInterceptor).
                build();

        timeCallback = new Callback<List<WorksTime>>() {
            @Override
            public void success(List<WorksTime> worksTimeList, Response response) {
                Log.d(TAG, "parse xml success" + worksTimeList);
                List<RealWorksTime> timeSheetList = TimeCounter.getRealTime(worksTimeList, userId);

                Log.d(TAG, "realTimeSheet size: " + timeSheetList.size());
                officeTimeCallback.getTimeList(timeSheetList);
                DatabaseManager.saveTimeSheetToDB(timeSheetList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "XML Parsing Fail :" + error.getMessage());
                error.printStackTrace();
            }
        };

        officeTime = restAdapter.create(OfficeTime.class);
        officeTime.timeList(from, till, userId, "LWO", timeCallback);
    }

    public interface AuthCallback {
        public void isAuthentication(boolean what);
    }

    public interface EmployeesCallback {
        public void getUserList(List<User> list);
    }

    public interface OfficeTimeCallback {
        public void getTimeList(List<RealWorksTime> list);
    }

    interface OfficeTime {
        @GET("/officetime/json/events.php")
        void timeList(@Query("from") long from,
                      @Query("till") long till,
                      @Query("employeeId") int limit,
                      @Query("zone") String zone,
                      Callback<List<WorksTime>> time);
    }

    interface Employees {
        @GET("/officetime/json/employees.php")
        void employeeList(Callback<List<User>> retrofitCallback);
    }

}
