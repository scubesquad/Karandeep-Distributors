package com.scube.karandeepdistributors.activities;
/**
 * Sign In Activity class
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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.NetworkHelper;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;
import com.scube.karandeepdistributors.utils.VerifyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AppCompatActivity {
    TextView notRegisteredYet, forgotPassword;
    Context mContext;
    EditText etUsername, etPassword;
    FloatingActionButton login;
    NetworkHelper networkHelper;
    ProgressDialogManager dialogManager;
    LoginSessionManger loginSessionManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initializeView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Use Toolbar to replace default activity action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display home menu item.
//            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        notRegisteredYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgotPasswordActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (networkHelper.isConnected()) {
                    if (!username.isEmpty() && !(password.isEmpty())) {
                        invokeSignInWebService(username, password);
                    }
                }
            }
        });
    }

    /**
     * Component Binding
     */
    private void initializeView() {
        mContext = this;
        dialogManager = new ProgressDialogManager(getApplicationContext());
        notRegisteredYet = (TextView) findViewById(R.id.tv);
        forgotPassword = (TextView) findViewById(R.id.tv1);
        etUsername = (EditText) findViewById(R.id.input_username);
        etPassword = (EditText) findViewById(R.id.input_password);
        networkHelper = new NetworkHelper(mContext);
        loginSessionManger = new LoginSessionManger(mContext);
        login = (FloatingActionButton) findViewById(R.id.btn_login);
    }

    /**
     * Sign In WebService
     * @param userName Registered Contact number/emailId
     * @param password password
     */
    private void invokeSignInWebService(String userName, String password) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("username", userName);
            params.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.post(Constants.LOGIN_API, params, new JsonHttpResponseHandler() {
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
                    if (errorResponse.getString("status").equalsIgnoreCase("999")) {
                        showInfoDialog("Fail", errorResponse.getString("message"));
                    } else {
                        showInfoDialog1("Error", errorResponse.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogManager.hideDialog();
                try {
                    String status = String.valueOf(response.getString("status"));
                    JSONObject jsonObject = response.getJSONObject("data");
                    if (status.equalsIgnoreCase("200")) {
                        loginSessionManger.saveUserDetails(jsonObject.getString("user_id")
                                , jsonObject.getString("user_name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("accesstoken"),
                                jsonObject.getString("contact_no"));
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        showInfoDialog1("Fail", response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Information Dialog box
     * @param title   title of dialog box
     * @param message detailed description message
     */
    private void showInfoDialog(String title, String message) {
        VerifyDialog infoDialog = new VerifyDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        infoDialog.setArguments(bundle);
        infoDialog.show(getSupportFragmentManager(), "complaint");
    }

    private void showInfoDialog1(String title, String message) {
        ShowInfoDialog infoDialog = new ShowInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        infoDialog.setArguments(bundle);
        infoDialog.show(getSupportFragmentManager(), "complaint");
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignInActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
