package com.scube.karandeepdistributors.activities;
/**
 * Profile Activity class
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {
    EditText etName, etMobileNo, etemaild, etcomapnyname, etaddress, etGstnumber;
    Button submit;
    Intent intent;
    String username, mobilenumber, emilid, companyname, address, gstnumber;
    ProgressDialogManager dialogManager;
    LoginSessionManger loginSessionManger;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        intent = getIntent();
        username = intent.getStringExtra("user_name");
        mobilenumber = intent.getStringExtra("contact_no");
        emilid = intent.getStringExtra("email");
        companyname = intent.getStringExtra("company_name");
        address = intent.getStringExtra("address");
        gstnumber = intent.getStringExtra("company_gst_no");
        // Use Toolbar to replace default activity action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display home menu item.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initilizeView();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeUpdateProfileWebService();
            }
        });
    }

    private void initilizeView() {
        etName = (EditText) findViewById(R.id.name);
        etMobileNo = (EditText) findViewById(R.id.mob_no);
        etemaild = (EditText) findViewById(R.id.email);
        etcomapnyname = (EditText) findViewById(R.id.company_name);
        etaddress = (EditText) findViewById(R.id.address);
        etGstnumber = (EditText) findViewById(R.id.gstnumber);
        submit = (Button) findViewById(R.id.submit);
        mContext = this;
        dialogManager = new ProgressDialogManager(mContext);
        loginSessionManger = new LoginSessionManger(mContext);
        etName.setText(username);
        etMobileNo.setText(mobilenumber);
        etemaild.setText(emilid);
        etcomapnyname.setText(companyname);
        etaddress.setText(address);
        etGstnumber.setText(gstnumber);
    }

    /**
     * Profile Update/view Web Service
     */
    private void invokeUpdateProfileWebService() {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("retailerId", loginSessionManger.getRetailerId());
            params.put("name", etName.getText().toString());
            params.put("email", etemaild.getText().toString());
            params.put("mobileNo", etMobileNo.getText().toString());
            params.put("companyName", etcomapnyname.getText().toString());
            params.put("companyGSTNo", etGstnumber.getText().toString());
            params.put("address", etaddress.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.UPDATE_RETAILER_PROFILE, params, new JsonHttpResponseHandler() {
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
                    String status = String.valueOf(response.getString("status"));
                    if (status.equalsIgnoreCase("200")) {
//                        showInfoDialog("Success", response.getString("message"));
//                        Toast.makeText(MyProfileActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = response.getJSONObject("retailerData");
                        String name = jsonObject.getString("user_name");
                        String comp_name = jsonObject.getString("company_name");
                        String gst_number = jsonObject.getString("company_gst_no");
                        String address = jsonObject.getString("address");
                        String contact_no = jsonObject.getString("contact_no");
                        String email = jsonObject.getString("email");
                        etName.setText(name);
                        etcomapnyname.setText(comp_name);
                        etGstnumber.setText(gst_number);
                        etaddress.setText(address);
                        etMobileNo.setText(contact_no);
                        etemaild.setText(email);
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
        ShowInfoDialog infoDialog = new ShowInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        infoDialog.setArguments(bundle);
        infoDialog.show(getSupportFragmentManager(), "complaint");
    }
}

