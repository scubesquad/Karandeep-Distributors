package com.scube.karandeepdistributors.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.glide.slider.library.SliderLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.adapter.BannerAdapter;
import com.scube.karandeepdistributors.adapter.BrandsRecyclerAdapter;
import com.scube.karandeepdistributors.model.Banner;
import com.scube.karandeepdistributors.model.Brands;
import com.scube.karandeepdistributors.utils.Constants;
import com.scube.karandeepdistributors.utils.Converter;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.scube.karandeepdistributors.utils.NetworkHelper;
import com.scube.karandeepdistributors.utils.ProgressDialogManager;
import com.scube.karandeepdistributors.utils.ShowInfoDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static RecyclerView recyclerbrands;
    private static Context mContext;
    static BrandsRecyclerAdapter homeScreenAdapter;
    static ArrayList<Brands> brandsArrayList = new ArrayList<>();
    static ArrayList<Banner> bannerArrayList = new ArrayList<>();
    private static SliderLayout mDemoSlider;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.homedefault, R.drawable.message_in_a_bottle, R.drawable.rose_blue_flower_rose_blooms, R.drawable.tree_736885__340};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    NetworkHelper networkHelper;
    ProgressDialogManager dialogManager;
    LoginSessionManger loginSessionManger;
    String emailId;
    TextView username, userEmail;

    public void refreshMenu() {
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeView();
        emailId = loginSessionManger.getKeyEmail();
        recyclerbrands = (RecyclerView) findViewById(R.id.recycler_view_brands);
        init();
        invokeBannerBrandWebService();
        fetchData();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username1);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.useremail);
        username.setText(loginSessionManger.getKeyName());
        userEmail.setText(loginSessionManger.getKeyEmail());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMenu();
    }
    /**
     * Binding Of Components
     */
    private void initializeView() {
        mContext = MainActivity.this;
        loginSessionManger = new LoginSessionManger(mContext);
        networkHelper = new NetworkHelper(mContext);
        dialogManager = new ProgressDialogManager(mContext);
//        if (loginSessionManger.isAccessTokenAvailable()) {
//            useremail.setText(loginSessionManger.getKeyEmail());
//        }
    }

/*    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
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
        if (id == R.id.cart_action) {
            startActivity(new Intent(mContext,CartActivity.class));
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
        if (id == R.id.nav_profile) {
            invokeProfileWebService();
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_myOrders) {
            startActivity(new Intent(mContext, MyOrderActivity.class));
        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(mContext, AboutUsActivity.class));
        } else if (id == R.id.nav_route_plan) {
            startActivity(new Intent(mContext, RoutePlanActivity.class));
        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(mContext, ContactUsActivity.class));
        } else if (id == R.id.nav_logout) {
            invokeLogoutWerService();
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

    /**
     * Banner And Brand Web Service
     */
    private void invokeBannerBrandWebService() {
        brandsArrayList.clear();
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
        client.post(Constants.GET_BANNER_BRANDS, params, new JsonHttpResponseHandler() {
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
                        JSONArray bannerData = response.getJSONArray("bannersData");
                        for (int j = 0; j < bannerData.length(); j++) {
                            Banner banner = new Banner();
                            JSONObject jsonObject = bannerData.getJSONObject(j);
                            banner.setId(jsonObject.getString("banner_id"));
                            banner.setImage(jsonObject.getString("image"));
                            bannerArrayList.add(banner);
                        }
                        JSONArray jsonArray = response.getJSONArray("brandsData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Brands brands = new Brands();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            brands.setBrandId(jsonObject.getString("brand_id"));
                            brands.setBrandName(jsonObject.getString("brand_name"));
                            brands.setLogo(jsonObject.getString("brand_logo"));
                            brandsArrayList.add(brands);
                        }
                        fetchData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Binding Of Components
     */
    private void fetchData() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerbrands.setLayoutManager(mLayoutManager);
        homeScreenAdapter = new BrandsRecyclerAdapter(mContext, brandsArrayList);
        recyclerbrands.setItemAnimator(new DefaultItemAnimator());
        recyclerbrands.setAdapter(homeScreenAdapter);
    }

    /**
     * implementing banner images through arraylist
     */
    private void init() {
        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new BannerAdapter(MainActivity.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == bannerArrayList.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
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
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
