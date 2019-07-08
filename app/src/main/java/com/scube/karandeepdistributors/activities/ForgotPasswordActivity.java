package com.scube.karandeepdistributors.activities;
/**
 * Forgot Password Activity
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView signUp, signIn;
    Toolbar toolbar;
    LoginSessionManger loginSessionManger;
    private Context mContext;
    ProgressDialogManager dialogManager;
    FloatingActionButton submit;
    EditText etmobileNo, etemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initializeView();
        // Use Toolbar to replace default activity action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display home menu item.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordWebService(etmobileNo.getText().toString(), "");
            }
        });
    }

    /**
     * Binding of components
     */
    private void initializeView() {
        mContext = this;
        dialogManager = new ProgressDialogManager(mContext);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        signUp = (TextView) findViewById(R.id.signUp);
        signIn = (TextView) findViewById(R.id.SignIn);
        submit = (FloatingActionButton) findViewById(R.id.btn_login);
        etmobileNo = (EditText) findViewById(R.id.input_mobile_number);
        etemail = (EditText) findViewById(R.id.input_email);
        loginSessionManger = new LoginSessionManger(mContext);
    }

    /**
     * forgot password WebService
     *
     * @param mobileNo contact no of logged in user
     * @param otp      one time password for verification
     */
    private void forgotPasswordWebService(String mobileNo, String otp) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("mobileNo", mobileNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.FORGOT_PASSWORD, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogManager.hideDialog();
                showInfoDialog("Error", "Some error occurred.");
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
//                        loginSessionManger.saveOTP();
                        Toast.makeText(mContext, response.getString("otp"), Toast.LENGTH_SHORT).show();
                        Log.e("OTP", response.getString("otp"));
                        Intent intent = new Intent(mContext, ChangePasswordActivity.class);
                        intent.putExtra("otp", response.getString("otp"));
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    /*@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ForgotPasswordActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/
}
