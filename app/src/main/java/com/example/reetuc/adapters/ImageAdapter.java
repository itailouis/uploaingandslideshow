package com.example.reetuc.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.reetuc.App;
import com.example.reetuc.models.ServerImages;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends PagerAdapter {

  Context mContext;
  List<ServerImages> images = new ArrayList<>();

  public ImageAdapter(Context mContext) {
    this.mContext = mContext;

  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == ((ImageView) object);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    ImageView imageView = new ImageView(mContext);
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    ServerImages image = images.get(position);
    Glide.with(mContext).load(App.BASE_IMAGE_URL+image.getImage_url()).into(imageView);
    ((ViewPager) container).addView(imageView, 0);
    return imageView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    ((ViewPager) container).removeView((ImageView) object);
  }

  @Override
  public int getCount() {
    return images.size();

  }

  public void setData(List<ServerImages> images) {
    this.images = images;
    notifyDataSetChanged();
  }
}
