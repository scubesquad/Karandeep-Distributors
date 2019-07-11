package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.activities.BrandDetailsActivity;
import com.scube.karandeepdistributors.activities.MainActivity;
import com.scube.karandeepdistributors.model.Brands;
import com.scube.karandeepdistributors.model.Order;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Brand Details Adapter
 */
public class BrandDetailsAdapter extends RecyclerView.Adapter<BrandDetailsAdapter.MyViewHolder> {
    Context context;
    View itemView;
    private LayoutInflater inflater;
    private ArrayList<Brands> brandsArrayList;
    String volume;
    LoginSessionManger loginSessionManger;
    Class<MainActivity> mainActivity;
    int s;

    /**
     * Base class providing the adapter to populate pages inside of a ViewPager.
     *
     * @param mContext
     * @param brandsArrayList
     */
    public BrandDetailsAdapter(Context mContext, ArrayList<Brands> brandsArrayList) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.brandsArrayList = brandsArrayList;
        mainActivity = MainActivity.class;
    }

    /**
     * called when the adapter is created and is used to initialize your ViewHolder(s).
     *
     * @param parent   ViewGroup
     * @param viewType int
     * @return
     */
    @NonNull
    @Override
    public BrandDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_brand_menu_row, parent, false);
        return new BrandDetailsAdapter.MyViewHolder(itemView);
    }

    /**
     * Binding of data getting from allCategory ArrayList
     *
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        loginSessionManger = new LoginSessionManger(context);
        holder.title.setText(brandsArrayList.get(position).getProductName());
        holder.price.setText("\u20B9" + brandsArrayList.get(position).getPrice());
        holder.brand_name.setText(brandsArrayList.get(position).getBrandName());
        holder.code.setText(brandsArrayList.get(position).getCode());
        volume = brandsArrayList.get(position).getVolume();
        try {
            holder.spinner.setText(volume);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(brandsArrayList.get(position).getProduct_img().isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.tree_736885__340);
        }else {
            try {
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.build().load(brandsArrayList.get(position).getProduct_img()).into(holder.thumbnail);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            holder.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    s = Integer.parseInt(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartClass cartClass = new CartClass();
                Order order = new Order();
                order.setProduct_id(brandsArrayList.get(position).getProduct_id());
                order.setBrandName(brandsArrayList.get(position).getBrandName());
                order.setProductName(brandsArrayList.get(position).getProductName());
                order.setProductVolume(String.valueOf(brandsArrayList.get(position).getVolume()));
                order.setProductPrice(brandsArrayList.get(position).getPrice());
                order.setProductCode(brandsArrayList.get(position).getCode());
                order.setQuantity(String.valueOf(s));
                cartClass.addToCart(order);
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                if (context instanceof BrandDetailsActivity) {
                    ((BrandDetailsActivity) context).refreshMenu();
                }
            }
        });
    }

    /**
     * size of arraylist
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return brandsArrayList.size();
    }

    /**
     * Set quantity of product
     *
     * @param value
     */
    public void setProductQuantity(int value) {
        String quantity = String.valueOf(value);
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, volume, price, brand_name, code, spinner;
        public ImageView thumbnail, addToCart;
        Spinner quantity;

        public MyViewHolder(View view) {
            super(view);
            price = (TextView) view.findViewById(R.id.price_value);
            title = (TextView) view.findViewById(R.id.product_name);
            brand_name = (TextView) view.findViewById(R.id.brand_name);
            thumbnail = (ImageView) view.findViewById(R.id.home_thumbnail);
            spinner = view.findViewById(R.id.volume_spinner);
            volume = view.findViewById(R.id.volume_size);
            addToCart = view.findViewById(R.id.addToCart);
            code = view.findViewById(R.id.code);
            quantity = view.findViewById(R.id.quantity_spinner);
        }
    }
}
