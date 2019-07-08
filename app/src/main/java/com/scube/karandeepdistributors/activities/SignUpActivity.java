package com.scube.karandeepdistributors.activities;
/**
 * Sign Up Activity class
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.scube.karandeepdistributors.utils.NetworkHelper;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;
import com.scube.karandeepdistributors.utils.VerifyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {
    private Button registerbtn;
    private NetworkHelper networkHelper;
    Context mContext;
    ProgressDialogManager dialogManager;
    private EditText etName, etEmail, etMobileNo, etCompanyName,
            etGstnumber, etAddress, etPassword, etConfirmpassword;
    LoginSessionManger loginSessionManger;
    TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Use Toolbar to replace default activity action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display home menu item.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initializeView();
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String mobile = etMobileNo.getText().toString();
                String companyname = etCompanyName.getText().toString();
                String gstnumber = etGstnumber.getText().toString();
                String address = etAddress.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmpassword.getText().toString();
                if (networkHelper.isConnected()) {
                   /* if (!name.isEmpty() && !(email.isEmpty()) &&
                            !(mobile.isEmpty()) && !(companyname.isEmpty()) && !(gstnumber.isEmpty())
                            && !(address.isEmpty()) && !(password.isEmpty()) && !(confirmPassword.isEmpty())) {*/
                    if (!name.equalsIgnoreCase("")) {
                        if (!email.equalsIgnoreCase("")) {
                            if (!mobile.equalsIgnoreCase("")) {
                                if (!companyname.equalsIgnoreCase("")) {
                                    if (!gstnumber.equalsIgnoreCase("")) {
                                        if (!address.equalsIgnoreCase("")) {
                                            if (!password.equalsIgnoreCase("")) {
                                                if (!confirmPassword.equalsIgnoreCase("")) {
                                                    if (password.matches(confirmPassword)) {
                                                        invokeRegistrationWebService(name, email, mobile, companyname, gstnumber, address, password, confirmPassword);
                                                    } else {
                                                        etConfirmpassword.setError("Password Doesn't Matches");
                                                    }
                                                } else {
                                                    etConfirmpassword.setError("Enter Confirm Password");
                                                }
                                            } else {
                                                etPassword.setError("Enter Password");
                                            }
                                        } else {
                                            etAddress.setError("Enter Address");
                                        }
                                    } else {
                                        etGstnumber.setError("Enter GST Number");
                                    }
                                } else {
                                    etCompanyName.setError("Enter Company name");
                                }
                            } else {
                                etMobileNo.setError("Enter Mobile Number");
                            }
                        } else {
                            etEmail.setError("Enter Email");
                        }
                    } else {
                        etName.setError("Enter Name");
                    }
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
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
    /**
     * Component Binding
     */
    private void initializeView() {
        mContext = this;
        dialogManager = new ProgressDialogManager(getApplicationContext());
        etName = (EditText) findViewById(R.id.etname);
        etEmail = (EditText) findViewById(R.id.etemail);
        etMobileNo = (EditText) findViewById(R.id.etmobile);
        etCompanyName = (EditText) findViewById(R.id.etcompany);
        etGstnumber = (EditText) findViewById(R.id.etgstnumber);
        etAddress = (EditText) findViewById(R.id.etaddress);
        etPassword = (EditText) findViewById(R.id.etpassword);
        etConfirmpassword = (EditText) findViewById(R.id.etconfirmpass);
        networkHelper = new NetworkHelper(mContext);
        loginSessionManger = new LoginSessionManger(mContext);
        signIn = (TextView) findViewById(R.id.signIn);
        registerbtn = (Button)findViewById(R.id.btn_SignUp);
    }

    /**
     * Registration Web Service
     * @param username username
     * @param email email
     * @param mobile mobile
     * @param companyname companyname
     * @param gstnumber gstnumber
     * @param address address
     * @param password password
     * @param confirmPass confirmPass
     */
    private void invokeRegistrationWebService(String username, String email,
                                              String mobile, String companyname, String gstnumber,
                                              String address, String password, String confirmPass) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("name", username);
            params.put("email", email);
            params.put("mobileNo", mobile);
            params.put("companyName", companyname);
            params.put("companyGSTNo", gstnumber);
            params.put("address", address);
            params.put("password", password);
            params.put("confirmpassword", confirmPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.post(Constants.REGISTER_API, params, new JsonHttpResponseHandler() {
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
                    if (errorResponse.getString("status").equalsIgnoreCase("409")){
                        showInfoDialog1("Error",errorResponse.getString("message"));
                    }else if (errorResponse.getString("status").equalsIgnoreCase("422")){
                        showInfoDialog1("Error", errorResponse.getString("message"));
                    }else if (errorResponse.getString("status").equalsIgnoreCase("400")){
                        showInfoDialog1("Error", errorResponse.getString("message"));
                    }else if (errorResponse.getString("status").equalsIgnoreCase("403")){
                        showInfoDialog1("Error", errorResponse.getString("message"));
                    }else {
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
                    if (status.equalsIgnoreCase("200")) {
                        showInfoDialog("Success", response.getString("message"));
                        Toast.makeText(SignUpActivity.this, response.getString("otp"), Toast.LENGTH_SHORT).show();
                        Log.e("OTP", response.getString("otp"));
                        loginSessionManger.saveContactNo(etMobileNo.getText().toString());
//                        Intent intent = new Intent(mContext, VerfiyOTPActivity.class);
//                        startActivity(intent);
                    } else {
                        showInfoDialog("Error", "Please Enter valid Credentials");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

  /*  @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignUpActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/
}

