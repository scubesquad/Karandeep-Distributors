package com.scube.karandeepdistributors.activities;
/**
 * Change Password Activity
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.ChangePasswordDialog;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangePasswordActivity extends AppCompatActivity {
  private EditText etMobileNo, etotp, etnewpass, etconfirmpass;
  private FloatingActionButton submitbtn;
  LoginSessionManger loginSessionManger;
  private Context mContext;
  ProgressDialogManager dialogManager;
  Intent intent;
  String otp;
//    TextView signUp, signIn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_changepassword);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    initializeview();
    // Use Toolbar to replace default activity action bar.
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      // Display home menu item.
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    intent = getIntent();
    otp = intent.getStringExtra("otp");
    etotp.setText(otp);
    submitbtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String mobileNo = etMobileNo.getText().toString();
        String password = etnewpass.getText().toString();
        String confirmpass = etconfirmpass.getText().toString();
        invokeChangePasswordWebService(mobileNo, password, confirmpass, otp);
      }
    });
      /*  signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SignUpActivity.class));
                finish();
            }
        });*/
  }

  /**
   * Binding components
   */
  private void initializeview() {
    mContext = this;
    dialogManager = new ProgressDialogManager(mContext);
    etMobileNo = (EditText) findViewById(R.id.input_mobile_number);
    etotp = (EditText) findViewById(R.id.input_otp);
    etnewpass = (EditText) findViewById(R.id.input_new_pass);
    etconfirmpass = (EditText) findViewById(R.id.input_confirm_pass);
    submitbtn = (FloatingActionButton) findViewById(R.id.btn_login);
    loginSessionManger = new LoginSessionManger(mContext);
//        signUp = (TextView) findViewById(R.id.user_signup);
//        signIn = (TextView) findViewById(R.id.user_signIn);
  }

  /**
   * Web Service for password change
   *
   * @param mobileNo        Contact no
   * @param password        password
   * @param confirmPassword confirm password
   * @param otp             one tme password received for verification
   */
  private void invokeChangePasswordWebService(String mobileNo, String password, String confirmPassword, String otp) {
    dialogManager.showDialog();
    RequestParams params = new RequestParams();
    try {
      params.put("mobileNo", mobileNo);
      params.put("password", password);
      params.put("confirmpassword", confirmPassword);
      params.put("otp", otp);
    } catch (Exception e) {
      e.printStackTrace();
    }
    AsyncHttpClient client = new AsyncHttpClient();
    client.setTimeout(30000);
    client.addHeader("apikey", Constants.API_KEY);
    client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
    client.post(Constants.CHANGE_PASSWORD, params, new JsonHttpResponseHandler() {
      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        dialogManager.hideDialog();
        showInfoDialog("Error", responseString);
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
            showInfoDialog("Success", response.getString("message"));
            Toast.makeText(mContext, response.getString("otp"), Toast.LENGTH_SHORT).show();
            Log.e("OTP", response.getString("otp"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(mContext, MainActivity.class));
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
    ChangePasswordDialog infoDialog = new ChangePasswordDialog();
    Bundle bundle = new Bundle();
    bundle.putString("title", title);
    bundle.putString("message", message);
    infoDialog.setArguments(bundle);
    infoDialog.show(getSupportFragmentManager(), "complaint");
  }

   /* @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangePasswordActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/
}

