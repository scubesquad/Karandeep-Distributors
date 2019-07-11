package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.model.Order;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Order> enquiries;

    public OrderDetailsAdapter(Context mContext, List<Order> enquiries) {
        this.mContext = mContext;
        this.enquiries = enquiries;
    }

    /**
     * called when the adapter is created and is used to initialize your ViewHolder(s).
     *
     * @param parent   ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_row, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Binds data getting from enquiries ArrayList
     *
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Order order = enquiries.get(position);
        holder.product_name.setText("Product Name :" + order.getProductName());
        holder.product_price.setText("Product Price :" + order.getProductPrice());
        holder.product_qty.setText("Product Quantity :" + order.getQuantity());
//        Picasso.with(mContext).load(IMAGE_INITIAL_URL + OrderDetails.getURL()).into(holder.image);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return enquiries.size();
    }

    /**
     * it is a helper class that holds the View of a row or rows. One or more ViewHolder is created for each viewType.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, product_price, product_qty;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_price = (TextView) view.findViewById(R.id.product_price);
            product_qty = (TextView) view.findViewById(R.id.product_qty);
            image = view.findViewById(R.id.image);
        }
    }
}
