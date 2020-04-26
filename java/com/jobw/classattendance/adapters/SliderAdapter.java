package com.adivid.zpattendanceadmin.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.adivid.zpattendanceadmin.R;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    Context context;
    ArrayList<Bitmap> bitmapArray;

    public SliderAdapter(Context context, ArrayList<Bitmap> bitmapArray) {
        this.context = context;
        this.bitmapArray = bitmapArray;

    }

    @Override
    public int getCount() {
      return bitmapArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView imageView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.slider_item,container,false);
        imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmapArray.get(position));
        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
