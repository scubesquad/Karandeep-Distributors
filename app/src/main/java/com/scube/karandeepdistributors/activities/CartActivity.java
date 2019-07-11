package com.scube.karandeepdistributors.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.adapter.CartAdapter;
import com.scube.karandeepdistributors.model.Order;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.Converter;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Cart Activity which shows count of purchased products
 */
public class CartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static CartAdapter cartAdapter;
    private RecyclerView cartRecyclerview;
    Spinner spinner;
    private static Context mContext;
    ProgressDialogManager dialogManager;
    LoginSessionManger loginSessionManger;
    Button placeOrder;
    ArrayList<Order> brands;
    String quantity;
    TextView username, userEmail;
    String id, quantity1;
    ArrayList<Order> placeOrderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initilizeview();
        final CartClass cartClass = new CartClass();
        brands = cartClass.getCartArrayList();
        fetchData();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username3);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.useremail3);
        username.setText(loginSessionManger.getKeyName());
        userEmail.setText(loginSessionManger.getKeyEmail());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeplaceOrderWebservice(brands, loginSessionManger.getRetailerId());
            }
        });
        try {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    quantity = parent.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeview() {
        mContext = CartActivity.this;
        cartRecyclerview = (RecyclerView) findViewById(R.id.recycler_view_cart);
        spinner = findViewById(R.id.spinner);
        placeOrder = (Button) findViewById(R.id.placeOrder);
        loginSessionManger = new LoginSessionManger(mContext);
        dialogManager = new ProgressDialogManager(mContext);
        placeOrderArrayList = new ArrayList<>();
    }

    /**
     * Setting Adapter data with Order Arraylist
     */
    private void fetchData() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        cartAdapter = new CartAdapter(mContext, brands, quantity);
        cartRecyclerview.setLayoutManager(mLayoutManager);
        cartRecyclerview.setItemAnimator(new DefaultItemAnimator());
        cartRecyclerview.setAdapter(cartAdapter);
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
     * @param item
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

    public void refreshMenu() {
        invalidateOptionsMenu();
    }

    /**
     * Listener for handling events on navigation items.
     *
     * @param item MenuItem: The selected item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile2) {
            invokeProfileWebService();
        } else if (id == R.id.nav_home2) {
            Intent intent = new Intent(this, MainActivity.class);// New activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_myOrders2) {
            Intent intent = new Intent(this, MyOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about_us2) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_route_plan2) {
            Intent intent = new Intent(this, RoutePlanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contactus2) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logout2) {
            invokeLogoutWerService();
//            count = 0;
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
                        startActivity(new Intent(mContext, SignInActivity.class));
                        finish();
                        brands.clear();
                        cartAdapter.notifyDataSetChanged();
                        refreshMenu();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Web Service for placing Order
     *
     * @param stringArrayList productsArraylist
     * @param retailerId      retailer/user Id
     */
    private void invokeplaceOrderWebservice(ArrayList<Order> stringArrayList, String retailerId) {
        dialogManager.showDialog();
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(stringArrayList).getAsJsonArray();
        Log.e("Array", String.valueOf(myCustomArray));
        RequestParams params = new RequestParams();
        params.put("retailerId", retailerId);
        params.put("products", myCustomArray);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.PLACE_ORDER, params, new JsonHttpResponseHandler() {
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
                        showInfoDialog("Message", response.getString("message"));
                        brands.clear();
                        cartAdapter.notifyDataSetChanged();
                        refreshMenu();
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
        ShowInfoDialog infoDialog = new ShowInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        infoDialog.setArguments(bundle);
        infoDialog.show(getSupportFragmentManager(), "complaint");
    }
}
