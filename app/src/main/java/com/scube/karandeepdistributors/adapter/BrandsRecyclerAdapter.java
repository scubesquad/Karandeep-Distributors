package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.activities.BrandDetailsActivity;
import com.scube.karandeepdistributors.model.Brands;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BrandsRecyclerAdapter extends RecyclerView.Adapter<BrandsRecyclerAdapter.MyViewHolder> {
    Context context;
    View itemView;
    ArrayList<Brands> brandsArrayList = new ArrayList<>();
    private LayoutInflater inflater;

    /**
     * called when the adapter is created and is used to initialize your ViewHolder(s).
     * @param parent   ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_menu_list_row, parent, false);
        return new BrandsRecyclerAdapter.MyViewHolder(itemView);
    }
    public BrandsRecyclerAdapter(Context mContext, ArrayList<Brands> brands) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.brandsArrayList = brands;
    }

    /**
     * Binding of data getting from allCategory ArrayList
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.title.setText(brandsArrayList.get(position).getId());
        holder.title.setText(brandsArrayList.get(position).getBrandName());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build().load(brandsArrayList.get(position).getLogo()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BrandDetailsActivity.class).
                        putExtra("id", brandsArrayList.get(position).getBrandId()));
            }
        });
    }

    /**
     * size Of arraylist
     * @return
     */
    @Override
    public int getItemCount() {
        return brandsArrayList.size();
    }
    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, badge;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.home_title);
            thumbnail = (ImageView) view.findViewById(R.id.home_thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            badge = (ImageView) view.findViewById(R.id.icon_badge);
            count = (TextView) view.findViewById(R.id.count);
        }
    }
}
