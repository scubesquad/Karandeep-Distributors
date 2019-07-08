package com.scube.karandeepdistributors.activities;
/**
 * Verify OTP Activity
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.NetworkHelper;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class VerfiyOTPActivity extends AppCompatActivity {
    private FloatingActionButton verifyOTPBtn;
    private TextView signUp, signIn, resendOTP;
    private EditText etOTP, etMobileNo;
    Context mContext;
    private
    NetworkHelper networkHelper;
    ProgressDialogManager dialogManager;
    LoginSessionManger loginSessionManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Use Toolbar to replace default activity action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display home menu item.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initilizeView();
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etOTP.getText().toString();
                String mobileNo = etMobileNo.getText().toString();
                if (networkHelper.isConnected()) {
                    if (!otp.isEmpty()) {
                        invokeVerifyOTPWebService(mobileNo, otp);
                    }
                }
            }
        });
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeResendOTPWebService(loginSessionManger.getKeyContact());
            }
        });
    }

    /**
     * Information Dialog box
     *
     * @param title   title of dialog box
     * @param message detailed description message
     */
    private void showInfoDialog(String title, String message) {
        ShowInfoDialog infoDialog = new ShowInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        infoDialog.setArguments(bundle);
        infoDialog.show(getSupportFragmentManager(), "complaint");
    }

    /**
     * Component Binding
     */
    private void initilizeView() {
        mContext = this;
        loginSessionManger = new LoginSessionManger(mContext);
        dialogManager = new ProgressDialogManager(getApplicationContext());
        networkHelper = new NetworkHelper(mContext);
        etOTP = (EditText) findViewById(R.id.input_otp);
        etMobileNo = (EditText) findViewById(R.id.input_mobile_number);
        verifyOTPBtn = (FloatingActionButton) findViewById(R.id.verifyOtp);
//        signUp = (TextView) findViewById(R.id.signUp);
//        signIn = (TextView) findViewById(R.id.SignIn);
        resendOTP = (TextView) findViewById(R.id.tvResendOTP);
    }

    /**
     * Verify OTP Web Service
     *
     * @param mobileNo mobile Number
     * @param otp      One Time password
     */
    private void invokeVerifyOTPWebService(String mobileNo, String otp) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("mobileNo", mobileNo);
            params.put("otp", otp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.post(Constants.VERIFY_OTP, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogManager.hideDialog();
                showInfoDialog("Error", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                dialogManager.hideDialog();
                showInfoDialog("Error", "Some error occurred.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogManager.hideDialog();
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("200")) {
//                        showInfoDialog("Success", response.getString("message"));
                        loginSessionManger.saveOTP();
                        JSONObject jsonObject = response.getJSONObject("data");
                        loginSessionManger.saveUserDetails(jsonObject.getString("user_id"),
                                jsonObject.getString("user_name"), jsonObject.getString("email"),
                                jsonObject.getString("accesstoken"), jsonObject.getString("contact_no"));
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Resend OTP Web Service
     *
     * @param contact contact number
     */
    private void invokeResendOTPWebService(String contact) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("mobileNo", contact);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.RESEND_OTP, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogManager.hideDialog();
                showInfoDialog("Error", "Some error occurred.");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                dialogManager.hideDialog();
                try {
                    showInfoDialog("Error", errorResponse.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogManager.hideDialog();
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        Toast.makeText(VerfiyOTPActivity.this, response.getString("otp"), Toast.LENGTH_SHORT).show();
                        Log.e("OTP", response.getString("otp"));
                        Intent intent = new Intent(mContext, VerfiyOTPActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VerfiyOTPActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/
}


