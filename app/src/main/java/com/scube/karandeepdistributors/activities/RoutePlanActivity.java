package com.scube.karandeepdistributors.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.adapter.RoutePlanAdapter;
import com.scube.karandeepdistributors.model.RoutePlan;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.Converter;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Route Plan Activity class
 */
public class RoutePlanActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    LoginSessionManger loginSessionManger;
    ProgressDialogManager dialogManager;
    ArrayList<RoutePlan> routePlans;
    RoutePlanAdapter routePlanAdapter;
    RecyclerView recyclerView;
    public TextView sundayArea, sundayPerson, mondayArea, mondayPerson, tuesdayArea, tuesdayPerson,
            wedArea, wedPerson, thurdArea, thursPerson, friArea, friPerson, satArea, satPerson;
    TextView username, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeView();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view7);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username7);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.useremail7);
        username.setText(loginSessionManger.getKeyName());
        userEmail.setText(loginSessionManger.getKeyEmail());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        invokeRouteplanWebService(loginSessionManger.getRetailerId());
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
     * Component Binding
     */
    private void initializeView() {
        mContext = this;
        dialogManager = new ProgressDialogManager(mContext);
        loginSessionManger = new LoginSessionManger(mContext);
        routePlans = new ArrayList<>();
        sundayArea = (TextView) findViewById(R.id.textView51);
        sundayPerson = (TextView) findViewById(R.id.textView61);
        mondayArea = (TextView) findViewById(R.id.textView5);
        mondayPerson = (TextView) findViewById(R.id.textView6);
        tuesdayArea = (TextView) findViewById(R.id.textView8);
        tuesdayPerson = (TextView) findViewById(R.id.textView9);
        wedArea = (TextView) findViewById(R.id.textView13);
        wedPerson = (TextView) findViewById(R.id.textView14);
        thurdArea = (TextView) findViewById(R.id.textView17);
        thursPerson = (TextView) findViewById(R.id.textView18);
        friArea = (TextView) findViewById(R.id.textView21);
        friPerson = (TextView) findViewById(R.id.textView22);
        satArea = (TextView) findViewById(R.id.textView25);
        satPerson = (TextView) findViewById(R.id.textView29);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    /**
     * This method passes the MenuItem selected.
     * @param menu element must be the root node for the file and can hold one or more <item> and <group> elements.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Listener for handling events on navigation items.
     * @param item MenuItem: The selected item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile4) {
            invokeProfileWebService();
//            startActivity(new Intent(mContext,MyProfileActivity.class));
        } else if (id == R.id.nav_home4) {
            startActivity(new Intent(mContext, MainActivity.class));
        } else if (id == R.id.nav_myOrders4) {
            startActivity(new Intent(mContext, MyOrderActivity.class));
        } else if (id == R.id.nav_about_us4) {
            startActivity(new Intent(mContext, AboutUsActivity.class));
        } else if (id == R.id.nav_route_plan4) {
            startActivity(new Intent(mContext, RoutePlanActivity.class));
        } else if (id == R.id.nav_contactus4) {
            startActivity(new Intent(mContext, ContactUsActivity.class));
        } else if (id == R.id.nav_logout) {
            invokeLogoutWerService();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
//                        showInfoDialog("Success", response.getString("message"));
                        startActivity(new Intent(mContext, SignInActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Web Service for Route Plan
     * @param retailerId Retailer Id
     */

    private void invokeRouteplanWebService(String retailerId) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        params.put("retailerId", retailerId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.GET_ROUTE_PLAN, params, new JsonHttpResponseHandler() {
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
                        JSONArray jsonArray = response.getJSONArray("routePlanData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            RoutePlan routePlan = new RoutePlan();
                            routePlan.setSundayArea(jsonObject.getString("area"));
                            routePlan.setSundayPerson(jsonObject.getString("person"));
                            routePlan.setMondayArea(jsonObject.getString("area"));
                            routePlan.setMondayPerson(jsonObject.getString("person"));
                            routePlan.setTueArea(jsonObject.getString("area"));
                            routePlan.setTuePerson(jsonObject.getString("person"));
                            routePlan.setWedArea(jsonObject.getString("area"));
                            routePlan.setWedPerson(jsonObject.getString("person"));
                            routePlan.setThursAre(jsonObject.getString("area"));
                            routePlan.setThursPerson(jsonObject.getString("person"));
                            routePlan.setFriAra(jsonObject.getString("area"));
                            routePlan.setFriPerson(jsonObject.getString("person"));
                            routePlan.setSatArea(jsonObject.getString("area"));
                            routePlan.setSatPerson(jsonObject.getString("person"));
                            routePlans.add(routePlan);
//                            fetchData();
                        }
                        fetchData(routePlans);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchData(ArrayList<RoutePlan> routePlans) {
        String s = routePlans.get(0).getSundayArea();
        String q = routePlans.get(0).getSundayPerson();
        String s1 = routePlans.get(1).getMondayArea();
        String q1 = routePlans.get(1).getMondayPerson();
        String s2 = routePlans.get(2).getTueArea();
        String q2 = routePlans.get(2).getTuePerson();
        String s3 = routePlans.get(3).getWedArea();
        String q3 = routePlans.get(3).getWedPerson();
        String s4 = routePlans.get(4).getThursAre();
        String q4 = routePlans.get(4).getThursPerson();
        String s5 = routePlans.get(4).getFriAra();
        String q5 = routePlans.get(4).getFriPerson();
        String s6 = routePlans.get(5).getSatArea();
        String q6 = routePlans.get(5).getSatPerson();
        sundayArea.setText(routePlans.get(0).getSundayArea());
        sundayPerson.setText(routePlans.get(0).getSundayPerson());
        mondayArea.setText(routePlans.get(1).getMondayArea());
        mondayPerson.setText(routePlans.get(1).getMondayPerson());
        tuesdayArea.setText(routePlans.get(2).getTueArea());
        tuesdayPerson.setText(routePlans.get(2).getTuePerson());
        wedArea.setText(routePlans.get(3).getWedArea());
        wedPerson.setText(routePlans.get(3).getWedPerson());
        thurdArea.setText(routePlans.get(4).getThursAre());
        thursPerson.setText(routePlans.get(4).getThursPerson());
        friArea.setText(routePlans.get(5).getFriAra());
        friPerson.setText(routePlans.get(5).getFriPerson());
        satArea.setText(routePlans.get(6).getSatArea());
        satPerson.setText(routePlans.get(6).getSatPerson());
    }
    /**
     * Information Dialog box
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
