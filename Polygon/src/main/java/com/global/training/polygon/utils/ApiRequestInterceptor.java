package com.global.training.polygon.utils;

import android.util.Base64;

import retrofit.RequestInterceptor;

/**
 * @author  eugenii.samarskyi on 13.11.2014.
 */
public class ApiRequestInterceptor implements RequestInterceptor {

    private String login;
    private String pass;

    public ApiRequestInterceptor(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public void intercept(RequestFacade request) {
        if(login != null && pass != null){
            final String authorizationValue = encodeCredentialsForBasicAuthorization();
            request.addHeader("Authorization", authorizationValue);
        }
    }

    private String encodeCredentialsForBasicAuthorization() {
        final String userAndPassword  = login + ":" + pass;
        return "Basic " + Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
    }

}
