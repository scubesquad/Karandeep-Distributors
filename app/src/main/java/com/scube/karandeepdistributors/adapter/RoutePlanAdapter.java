package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.model.RoutePlan;

import java.util.ArrayList;

/**
 * Route Plan Adapter
 */
public class RoutePlanAdapter extends RecyclerView.Adapter<RoutePlanAdapter.MyViewHolder> {
    Context context;
    View itemView;
    private LayoutInflater inflater;
    private ArrayList<RoutePlan> routePlans;
    int quantity;
    public RoutePlanAdapter(Context context, ArrayList<RoutePlan> brandsArrayList) {
        this.context = context;
        this.routePlans = brandsArrayList;
    }
    /**
     * called when the adapter is created and is used to initialize your ViewHolder(s).
     * @param parent   ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public RoutePlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_route_plan, parent, false);
        return new RoutePlanAdapter.MyViewHolder(itemView);
    }

    /**
     * Binding of data getting from allCategory ArrayList
     * @param holder   Optimizing listView with the ViewHolder Pattern.
     * @param position position of every item in recyclerview
     */
    @Override
    public void onBindViewHolder(RoutePlanAdapter.MyViewHolder holder, int position) {
        holder.sundayArea.setText(routePlans.get(position).getSundayArea());
        holder.sundayPerson.setText(routePlans.get(position).getSundayPerson());
        holder.mondayArea.setText(routePlans.get(position).getMondayArea());
        holder.mondayPerson.setText(routePlans.get(position).getMondayPerson());
        holder.tuesdayArea.setText(routePlans.get(position).getTueArea());
        holder.tuesdayPerson.setText(routePlans.get(position).getTuePerson());
        holder.wedArea.setText(routePlans.get(position).getWedArea());
        holder.wedPerson.setText(routePlans.get(position).getWedPerson());
        holder.thurdArea.setText(routePlans.get(position).getThursAre());
        holder.thursPerson.setText(routePlans.get(position).getThursPerson());
        holder.friArea.setText(routePlans.get(position).getFriAra());
        holder.friPerson.setText(routePlans.get(position).getFriPerson());
        holder.satArea.setText(routePlans.get(position).getSatArea());
        holder.satPerson.setText(routePlans.get(position).getSatPerson());
    }

    /**
     * Count of items from arraylist
     * @return
     */
    @Override
    public int getItemCount() {
        return routePlans.size();
    }
    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sundayArea, sundayPerson, mondayArea, mondayPerson, tuesdayArea, tuesdayPerson,
                wedArea, wedPerson, thurdArea, thursPerson, friArea, friPerson, satArea, satPerson;

        public MyViewHolder(View itemView) {
            super(itemView);
            sundayArea = (TextView) itemView.findViewById(R.id.textView51);
            sundayPerson = (TextView) itemView.findViewById(R.id.textView61);
            mondayArea = (TextView) itemView.findViewById(R.id.textView5);
            mondayPerson = (TextView) itemView.findViewById(R.id.textView6);
            tuesdayArea = (TextView) itemView.findViewById(R.id.textView8);
            tuesdayPerson = (TextView) itemView.findViewById(R.id.textView9);
            wedArea = (TextView) itemView.findViewById(R.id.textView13);
            wedPerson = (TextView) itemView.findViewById(R.id.textView14);
            thurdArea = (TextView) itemView.findViewById(R.id.textView17);
            thursPerson = (TextView) itemView.findViewById(R.id.textView18);
            friArea = (TextView) itemView.findViewById(R.id.textView21);
            friPerson = (TextView) itemView.findViewById(R.id.textView22);
            satArea = (TextView) itemView.findViewById(R.id.textView25);
            satPerson = (TextView) itemView.findViewById(R.id.textView29);
        }
    }
}
