package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
     * Binding of data getting from allCategory ArrayLis
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {
        int price = 0;
        holder.title.setText(orderArrayList.get(position).getProductName());
        holder.code.setText(orderArrayList.get(position).getProductCode());
        holder.tvprice.setText(orderArrayList.get(position).getProductPrice());
        try {
            volume = orderArrayList.get(position).getProductVolume();
            holder.volume_spinner.setSelection(Integer.parseInt(volume));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            price = Integer.parseInt(orderArrayList.get(position).getProductPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if (firstWord.contains(" ")) {
                        firstWord = firstWord.substring(0, firstWord.indexOf(" "));

                        result = Integer.parseInt(firstWord);
                        Log.e("Size", firstWord);
                    }
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
                Order order = new Order();
                order.setQuantity(String.valueOf(size));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * size of arraylist
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
        public TextView title, volume, code, tvprice, quantityValue, result;
        public ImageView thumbnail, badge, cancel;
        public CardView cardView;
        Spinner volume_spinner;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.product_name);
            code = (TextView) view.findViewById(R.id.product_code);
            thumbnail = (ImageView) view.findViewById(R.id.home_thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            badge = (ImageView) view.findViewById(R.id.icon_badge);
            volume = (TextView) view.findViewById(R.id.product_volume);
            volume_spinner = (Spinner) view.findViewById(R.id.product_quantity_spinner);
            quantityValue = (TextView) view.findViewById(R.id.product_quantity);
            tvprice = (TextView) view.findViewById(R.id.product_price);
            result = (TextView) view.findViewById(R.id.calculationResult);
            cancel = (ImageView) view.findViewById(R.id.cancel);
        }
    }
}
