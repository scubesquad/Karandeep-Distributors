package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.activities.OrderDetailsActivity;
import com.scube.karandeepdistributors.model.Order;

import java.util.ArrayList;

/**
 * Order Adapter
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    Context context;
    View itemView;
    private LayoutInflater inflater;
    ArrayList<Order> orderArrayList;

    public MyOrderAdapter(Context mContext, ArrayList<Order> orders) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.orderArrayList = orders;
    }
    /**
     * called when the adapter is created and is used to initialize your ViewHolder(s).
     * @param parent   ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public MyOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorder_menu_list_row, parent, false);
        return new MyOrderAdapter.MyViewHolder(itemView);
    }
    /**
     * Binding of data getting from allCategory ArrayList
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.e("ArrayList",orderArrayList.toString());
        String s = orderArrayList.get(position).getBrandName();
        holder.date.setText(orderArrayList.get(position).getOrderDate());
        holder.orderId.setText(orderArrayList.get(position).getOrderId());
        holder.price.setText(orderArrayList.get(position).getOrderPrice());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = orderArrayList.get(position);
                Intent intent = new Intent(context,OrderDetailsActivity.class);
                intent.putExtra("orders",order);
                context.startActivity(intent);
              /*  context.startActivity(new Intent(context, OrderDetailsActivity.class).
                        putExtra("brand_name", orderArrayList.get(position).getBrandName()).
                        putExtra("product_name", orderArrayList.get(position).getProductName()).
                        putExtra("volume", orderArrayList.get(position).getProductVolume()).
                        putExtra("quantity", orderArrayList.get(position).getQuantity()).
                        putExtra("price", orderArrayList.get(position).getProductPrice()).
                        putExtra("image", orderArrayList.get(position).getProductImage()));*/
            }
        });
    }
    /**
     * size Of arraylist
     * @return
     */
    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, price, orderId;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date_value);
            price = (TextView) view.findViewById(R.id.total_value);
            orderId = (TextView) view.findViewById(R.id.order_id_value);
            thumbnail = (ImageView) view.findViewById(R.id.home_thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
