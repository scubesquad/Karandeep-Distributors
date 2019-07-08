package com.scube.karandeepdistributors.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.model.Banner;

import java.util.ArrayList;

/**
 * Banner Adapter
 */
public class BannerAdapter extends PagerAdapter {

    private ArrayList<Integer> banners;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Base class providing the adapter to populate pages inside of a ViewPager.
     * @param context
     * @param bannerArrayList
     */
    public BannerAdapter(Context context, ArrayList<Integer> bannerArrayList) {
        this.context = context;
        this.banners=bannerArrayList;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Remove a page for the given position.
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by instantiateItem(View, int).
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    /**
     * get size of banner arraylist
     * @return
     */
    @Override
    public int getCount() {
        return banners.size();
    }

    /**
     * The adapter is responsible for adding the view to the container given here
     */
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        myImage.setImageResource(banners.get(position));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    /**
     * Determines whether a page View is associated with a specific key object as returned by instantiateItem(ViewGroup, int).
     * @param view Determines whether a page View is associated with a specific key object as returned by instantiateItem(ViewGroup, int)
     * @param object Object to check for association with view
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
