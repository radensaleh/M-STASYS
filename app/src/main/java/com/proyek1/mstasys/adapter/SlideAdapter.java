package com.proyek1.mstasys.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.proyek1.mstasys.R;

public class SlideAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;

    public SlideAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public int[] slide_images = {
            R.drawable.slide_1,
            R.drawable.slide_2,
            R.drawable.slide_3,
            R.drawable.slide_4,
            R.drawable.slide_5
    };

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImage = view.findViewById(R.id.slide_images);
        slideImage.setImageResource(slide_images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
