package com.nekkies.wllppr;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class SwipePhotoPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public List<String> photos_links;

    public SwipePhotoPagerAdapter(Context context,
                                  List<String> photos_ids,
                                  List<String> photos_links) {
        this.context = context;
        this.photos_links = photos_links;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPhotos_links(List<String> photos_links) {
        this.photos_links = photos_links;
    }

    @Override
    public int getCount() {
        return photos_links.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((RelativeLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.item_single_photo,container,false);

        PhotoView single_photo = (PhotoView) view.findViewById(R.id.photo_fuck);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.loading_boi);



        GlideApp.with(context)
                .load(photos_links.get(position))
                .into(single_photo);



        ((ViewPager) container).addView(view,0);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
