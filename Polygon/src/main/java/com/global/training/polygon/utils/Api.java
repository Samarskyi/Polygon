package com.global.training.polygon.utils;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class Api {

    private static final String URL_EMPLOYEES = "https://portal-ua.globallogic.com/officetime/json/employees.php";

    public interface InternetCallback {
       public void isAuthentication(boolean what);
    }

//    public void getUsers(List<User> list);

    private static final List<InternetCallback> callbacks = new ArrayList<InternetCallback>();

    public static void addInternetCallback(InternetCallback callback) {
        callbacks.add(callback);
    }

    public static void removeInternetCallback(InternetCallback callback) {
        callbacks.remove(callback);
    }

    public static void auth(final String name, final String pass) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(URL_EMPLOYEES);
//                    httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials("eugenii.samarskyi", "[pi989898pi]!"), "UTF-8", false));
                    httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(name, pass), "UTF-8", false));
                    HttpResponse httpResponse = httpclient.execute(httpGet);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    boolean result = false;

                    if(statusCode == 200){
                        result = true;
                    }

                    for (InternetCallback callback : callbacks){
                        callback.isAuthentication(result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void saveUsersToDataBase() {

    }


}
