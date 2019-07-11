package com.scube.karandeepdistributors.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.scube.karandeepdistributors.model.Order;

import java.util.ArrayList;

/**
 * Maintaining All Active Sessions
 */
public class LoginSessionManger {
    private static final String PREF_NAME = "LoginPref";
    private static final String KEY_OTP = "OTP";
    private static final String KEY_USERID = "RETAILER_ID";
    private static final String KEY_NAME = "NAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_CONTACT = "CONTACT";
    private static final String KEY_ACCESSTOKEN = "accesstoken";
    private static final String KEY_LOGIN = "LOGIN";
    private static final String KEY_COMPANY_NAME = "COMPANY";
    private static final String KEY_GST_NUMBER = "GST_NUMBER";
    private static final String KEY_ADDRESS = "ADDRESS";
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<Order> orderArrayList;

    public LoginSessionManger(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
        orderArrayList = new ArrayList<>();
    }

    /**
     * saving contact number while signUp
     *
     * @param contact
     */
    public void saveContactNo(String contact) {
        editor.putString(KEY_CONTACT, contact);
        editor.apply();
    }

    /**
     * Save OTP at while verifying OTP
     */
    public void saveOTP() {
        editor.putBoolean(KEY_OTP, true);
        editor.apply();
    }

    /**
     * get retailer id
     *
     * @return
     */
    public String getRetailerId() {
        return preferences.getString(KEY_USERID, null);
    }

    /**
     * get contact number
     *
     * @return
     */
    public String getKeyContact() {
        return preferences.getString(KEY_CONTACT, null);
    }

    /**
     * return true or false statement if token is available
     *
     * @return
     */
    public boolean isAccessTokenAvailable() {
        return preferences.getBoolean(KEY_LOGIN, false);
    }

    /**
     * return key access token
     *
     * @return
     */
    public String getKeyAccesstoken() {
        return preferences.getString(KEY_ACCESSTOKEN, null);
    }

    /**
     * clear all session data
     */
    public void deleteAll() {
        editor.clear();
        editor.apply();
    }

    /**
     * get key name
     *
     * @return
     */
    public String getKeyName() {
        return preferences.getString(KEY_NAME, null);
    }

    /**
     * get email
     *
     * @return
     */
    public String getKeyEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }

    /**
     * save user details while sign in
     *
     * @param userId   logged in retailer id
     * @param userName logged in retailer name
     * @param email    logged in retailer email id
     * @param token    access token which is received in response after logged in
     * @param contact  logged in retailer contact number
     */
    public void saveUserDetails(String userId, String userName, String email, String token, String contact) {
        editor.putString(KEY_ACCESSTOKEN, token);
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_NAME, userName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CONTACT, contact);
        editor.putBoolean(KEY_LOGIN, true);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return preferences.getBoolean(KEY_LOGIN, false);
    }

    /**
     * save user's profile
     *
     * @param name      retailer name
     * @param email     retailer emailId
     * @param contact   retailer contact number
     * @param comp_name retailer company name
     * @param address   retailer address
     * @param gstnumber retailer gst number
     */
    public void saveUserProfile(String name, String email, String contact, String comp_name, String address, String gstnumber) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CONTACT, contact);
        editor.putString(KEY_COMPANY_NAME, comp_name);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_GST_NUMBER, gstnumber);
        editor.apply();
    }
}
