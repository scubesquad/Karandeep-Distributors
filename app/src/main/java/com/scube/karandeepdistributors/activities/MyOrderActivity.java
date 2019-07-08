package com.scube.karandeepdistributors.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.adapter.MyOrderAdapter;
import com.scube.karandeepdistributors.model.Order;
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
 * Order Activity class
 */
public class MyOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    public static MyOrderAdapter myOrderAdapter;
    private Context mContext;
    private RecyclerView myOrderRecyclerView;
    LoginSessionManger loginSessionManger;
    ProgressDialogManager dialogManager;
    private ArrayList<Order> orderProductsArrayList;
    private ArrayList<Order> orderDataArraylist = new ArrayList<>();
    public ArrayList<Order> filterArraylist = new ArrayList<>();
    private Spinner monthSpinner, yearSpinner;
    String searchkey;
    String month, year;
    TextView username, userEmail, tvNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        initializeView();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view5);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username5);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.useremail5);
        username.setText(loginSessionManger.getKeyName());
        userEmail.setText(loginSessionManger.getKeyEmail());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        invokeFilterWebService(year, month, searchkey);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderDataArraylist.clear();
                if (parent.getSelectedItemPosition() == 0) {
                    month = "01";
                }
                if (parent.getSelectedItemPosition() == 1) {
                    month = "02";
                }
                if (parent.getSelectedItemPosition() == 2) {
                    month = "03";
                }
                if (parent.getSelectedItemPosition() == 3) {
                    month = "04";
                }
                if (parent.getSelectedItemPosition() == 4) {
                    month = "05";
                }
                if (parent.getSelectedItemPosition() == 5) {
                    month = "06";
                }
                if (parent.getSelectedItemPosition() == 6) {
                    month = "07";
                }
                if (parent.getSelectedItemPosition() == 7) {
                    month = "08";
                }
                if (parent.getSelectedItemPosition() == 8) {
                    month = "09";
                }
                if (parent.getSelectedItemPosition() == 9) {
                    month = "10";
                }
                if (parent.getSelectedItemPosition() == 10) {
                    month = "11";
                }
                if (parent.getSelectedItemPosition() == 11) {
                    month = "12";
                }
                invokeFilterWebService(year, month, searchkey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getSelectedItem().toString();
//                orderDataArraylist.clear();
                if (parent.getSelectedItemPosition() == 0) {
                    year = "2019";
                }
                if (parent.getSelectedItemPosition() == 1) {
                    year = "2020";
                }
                invokeFilterWebService(year, month, searchkey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Component Binding
     */
    private void initializeView() {
        mContext = this;
        myOrderRecyclerView = findViewById(R.id.recycler_view_orders);
        dialogManager = new ProgressDialogManager(mContext);
        loginSessionManger = new LoginSessionManger(mContext);
        monthSpinner = (Spinner) findViewById(R.id.month_spinnerview);
        yearSpinner = (Spinner) findViewById(R.id.yearPinnserview);
        tvNoResult = (TextView) findViewById(R.id.noDatafound);
    }

    /**
     * Setting Adapter data with brand Arraylist
     */
    private void fetchData() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        myOrderAdapter = new MyOrderAdapter(mContext, orderDataArraylist);
        myOrderRecyclerView.setLayoutManager(mLayoutManager);
        myOrderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderRecyclerView.setAdapter(myOrderAdapter);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (id == R.id.nav_profile3) {
//            startActivity(new Intent(mContext,MyProfileActivity.class));
            invokeProfileWebService();
        } else if (id == R.id.nav_home3) {
            startActivity(new Intent(mContext, MainActivity.class));
        } else if (id == R.id.nav_myOrders3) {
            startActivity(new Intent(mContext, MyOrderActivity.class));
        } else if (id == R.id.nav_about_us3) {
            startActivity(new Intent(mContext, AboutUsActivity.class));
        } else if (id == R.id.nav_route_plan3) {
            startActivity(new Intent(mContext, RoutePlanActivity.class));
        } else if (id == R.id.nav_contactus3) {
            startActivity(new Intent(mContext, ContactUsActivity.class));
        } else if (id == R.id.nav_logout3) {
            invokeLogoutWerService();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Web Service which sorts the data as per year and month selection
     *
     * @param year  selected year for sorting
     * @param month selected month for sorting
     */
    private void invokeFilterWebService(String year, String month, String searchkey) {
        dialogManager.showDialog();
        RequestParams params = new RequestParams();
        params.put("retailerId", loginSessionManger.getRetailerId());
        params.put("month", month);
        params.put("year", year);
        params.put("searchKey", searchkey);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("apikey", Constants.API_KEY);
        client.addHeader("accesstoken", loginSessionManger.getKeyAccesstoken());
        client.post(Constants.GET_ORDER_HISTORY, params, new JsonHttpResponseHandler() {
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
//                orderDataArraylist.clear();
                dialogManager.hideDialog();
                try {
                    String status = String.valueOf(response.getString("status"));
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = response.getJSONArray("ordersData");
                        orderDataArraylist.clear();
                        if (jsonArray.length() >= 1) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Order order = new Order();
                                order.setOrderId(jsonArray.getJSONObject(i).getString("order_id"));
                                order.setOrderPrice(jsonArray.getJSONObject(i).getString("order_price"));
                                order.setOrderDate(jsonArray.getJSONObject(i).getString("order_date"));
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("order_products");
                                orderProductsArrayList = new ArrayList<>();
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    order.setProduct_id(jsonArray1.getJSONObject(j).getString("product_id"));
                                    order.setProductName(jsonArray1.getJSONObject(j).getString("product_name"));
                                    order.setBrandId(jsonArray1.getJSONObject(j).getString("brand_id"));
                                    order.setBrandName(jsonArray1.getJSONObject(j).getString("brand_name"));
                                    order.setCategoryId(jsonArray1.getJSONObject(j).getString("category_id"));
                                    order.setProductImage(jsonArray1.getJSONObject(j).getString("product_image"));
                                    order.setProductCode(jsonArray1.getJSONObject(j).getString("product_code"));
                                    order.setProductVolume(jsonArray1.getJSONObject(j).getString("product_volume"));
                                    order.setProductPrice(jsonArray1.getJSONObject(j).getString("product_price"));
                                    order.setQuantity(jsonArray1.getJSONObject(j).getString("quantity"));
                                    orderProductsArrayList.add(order);
                                }
                                Log.e("enquiryObject1s", String.valueOf(orderProductsArrayList.size()));
                                order.setOrderArrayList(orderProductsArrayList);
                                orderDataArraylist.add(order);
                                filterArraylist.addAll(orderDataArraylist);
                                fetchData();
                            }
                        } else {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    protected void onResume() {
        refreshMenu();
        super.onResume();
    }

    private void refreshMenu() {
        invalidateOptionsMenu();
    }

    /**
     * This method passes the MenuItem selected.
     *
     * @param menu element must be the root node for the file and can hold one or more <item> and <group> elements.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_order, menu);
        final MenuItem item = menu.findItem(R.id.action_search_order);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        EditText edit = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        edit.setHint("Search...");
        edit.setHintTextColor(Color.parseColor("#FFFFFF"));
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setVisible(true);
        CartClass cartClass = new CartClass();
        int count = cartClass.getTotalNumberOfItems();
        menuItem.setIcon(Converter.convertLayoutToImage(mContext, count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        invokeFilterWebService(year, month, newText);
        return true;
    }
}
