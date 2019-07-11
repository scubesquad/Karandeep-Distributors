package com.scube.karandeepdistributors.activities;
/**
 * Contact Us Activity
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.Converter;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.scube.karandeepdistributors.activities.BrandDetailsActivity.quantity;

/**
 * Contact Us Activity which gives information about Location and contact Details of Company
 */
public class ContactUsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static Context mContext;
    ProgressDialogManager dialogManager;
    LoginSessionManger loginSessionManger;
    TextView username, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeView();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username4);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.useremail4);
        username.setText(loginSessionManger.getKeyName());
        userEmail.setText(loginSessionManger.getKeyEmail());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Binding of components
     */
    private void initializeView() {
        mContext = ContactUsActivity.this;
        dialogManager = new ProgressDialogManager(mContext);
        loginSessionManger = new LoginSessionManger(mContext);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method passes the MenuItem selected.
     *
     * @param menu element must be the root node for the file and can hold one or more <item> and <group> elements.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setVisible(true);
        CartClass cartClass = new CartClass();
        int count = cartClass.getTotalNumberOfItems();
        menuItem.setIcon(Converter.convertLayoutToImage(mContext, count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    /**
     * passes the MenuItem selected. You can identify the item by calling getItemId(), which returns the unique ID
     *
     * @param item MenuItem: The selected item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_action) {
            Intent intent = new Intent(mContext, CartActivity.class);
            intent.putExtra("Quantity", quantity);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Listener for handling events on navigation items.
     * @param item MenuItem: The selected item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile6) {
            invokeProfileWebService();
        } else if (id == R.id.nav_home6) {
            Intent intent = new Intent(this, MainActivity.class);// New activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_myOrders6) {
            Intent intent = new Intent(this, MyOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about_us6) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_route_plan6) {
            Intent intent = new Intent(this, RoutePlanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contactus6) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logout6) {
            invokeLogoutWerService();
//            count = 0;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Logout WebService for clearing session data from device
     */
    private void invokeLogoutWerService() {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("retailerId", loginSessionManger.getRetailerId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.LOGOUT_API, params, new JsonHttpResponseHandler() {
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
                        loginSessionManger.deleteAll();
                        startActivity(new Intent(mContext, SignInActivity.class));
                        finish();
//                        refreshMenu();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshMenu() {
        invalidateOptionsMenu();
    }

    /**
     * Profile Update/view Web Service
     */
    private void invokeProfileWebService() {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("retailerId", loginSessionManger.getRetailerId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.GET_RETAILER_PROFILE, params, new JsonHttpResponseHandler() {
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
                        JSONObject jsonObject = response.getJSONObject("retailerData");
                        loginSessionManger.saveUserProfile(
                                jsonObject.getString("user_name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("contact_no"),
                                jsonObject.getString("company_name"),
                                jsonObject.getString("address"),
                                jsonObject.getString("company_gst_no"));
//                        showInfoDialog("Success", response.getString("message"));
                        Intent intent = new Intent(mContext, MyProfileActivity.class);
                        intent.putExtra("user_name", jsonObject.getString("user_name"));
                        intent.putExtra("email", jsonObject.getString("email"));
                        intent.putExtra("contact_no", jsonObject.getString("contact_no"));
                        intent.putExtra("company_name", jsonObject.getString("company_name"));
                        intent.putExtra("address", jsonObject.getString("address"));
                        intent.putExtra("company_gst_no", jsonObject.getString("company_gst_no"));
                        startActivity(intent);
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
     * @param title   title of message
     * @param message description of message
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
