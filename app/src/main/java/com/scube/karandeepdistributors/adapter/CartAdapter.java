package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scube.karandeepdistributors.CartClass;
import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.model.Order;

import java.util.ArrayList;

/**
 * Cart Adapter
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    View itemView;
    ArrayList<Order> orderArrayList;
    private LayoutInflater inflater;
    //    int calculation, price;
    String squantity;
    String volume;

    public CartAdapter(Context mContext, ArrayList<Order> orderArrayList, String quantity) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.orderArrayList = orderArrayList;
        squantity = quantity;
    }

    /**
     * called when the adapter is created and is used to initialize your ViewHolder(s).
     *
     * @param parent   ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_menu_list_row, parent, false);
        return new CartAdapter.MyViewHolder(itemView);
    }

    /**
     * Binding of data getting from allCategory ArrayList
     *
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {
        int price = 0;
        CartClass cartClass = new CartClass();
        final Order order = new Order();
        holder.title.setText(orderArrayList.get(position).getProductName());
        holder.brandTitle.setText(orderArrayList.get(position).getBrandName());
        holder.code.setText(orderArrayList.get(position).getProductCode());
        holder.volume.setText(orderArrayList.get(position).getProductVolume());
        holder.tvprice.setText(orderArrayList.get(position).getProductPrice());
        holder.id.setText(orderArrayList.get(position).getProduct_id());
        try {
            volume = orderArrayList.get(position).getQuantity();
//            order.setQuantity(String.valueOf(Integer.parseInt(volume) - 1));
            holder.volume_spinner.setSelection(Integer.parseInt(volume) - 1);
//            cartClass.addToCart(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            price = Integer.parseInt(orderArrayList.get(position).getProductPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.update_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, holder.id.getText().toString(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < orderArrayList.size(); i++) {
                    if (orderArrayList.get(i).getProduct_id().equalsIgnoreCase(holder.id.getText().toString())) {
                        String s = holder.volume_spinner.getSelectedItem().toString();
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        orderArrayList.get(i).setQuantity(s);
                    }
                }
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartClass cartClass = new CartClass();
                String integer = orderArrayList.get(position).getQuantity();
                try {
                    Integer temp = Integer.valueOf(integer);
                    temp--;
                    integer = String.valueOf(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
                cartClass.removeItem(position);
//                Toast.makeText(context, holder.title.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        final int finalPrice = price;
        holder.volume_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String size, firstWord = null;
                int calculation = 0, result = 0;
                size = parent.getSelectedItem().toString();
                try {
                    firstWord = size;
                    result = Integer.parseInt(firstWord);
                    Log.e("Size", firstWord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.quantityValue.setText(size);
                try {
                    calculation = result * finalPrice;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.result.setText("\u20B9" + calculation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        return orderArrayList.size();
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, volume, code, tvprice, quantityValue, brandTitle, result, id;
        public ImageView thumbnail, badge, cancel;
        public RelativeLayout cardView;
        Spinner volume_spinner;
        Button update_qty;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.orderId);
            title = (TextView) view.findViewById(R.id.product_name);
            code = (TextView) view.findViewById(R.id.product_code);
            thumbnail = (ImageView) view.findViewById(R.id.home_thumbnail);
            update_qty = (Button) view.findViewById(R.id.update_qty);
            badge = (ImageView) view.findViewById(R.id.icon_badge);
            volume = (TextView) view.findViewById(R.id.product_volume);
            volume_spinner = (Spinner) view.findViewById(R.id.product_quantity_spinner);
            quantityValue = (TextView) view.findViewById(R.id.product_quantity);
            tvprice = (TextView) view.findViewById(R.id.product_price);
            brandTitle = (TextView) view.findViewById(R.id.brand_name);
            result = (TextView) view.findViewById(R.id.calculationResult);
            cancel = (ImageView) view.findViewById(R.id.cancel);
        }
    }
}
