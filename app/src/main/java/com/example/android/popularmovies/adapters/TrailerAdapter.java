package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.OnYoutubeClickListener;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class TrailerAdapter extends PagerAdapter {
    private List<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private final OnYoutubeClickListener mOnYoutubeClickListener;
    private String BASE_URL = "http://img.youtube.com/vi/";


    public TrailerAdapter(Context context,List<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        mOnYoutubeClickListener =(OnYoutubeClickListener) context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_trailer, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.youtube_trailer_image);
        ImageView mYoutube = (ImageView)imageLayout.findViewById(R.id.youtube_image);


        //imageView.setImageResource(IMAGES.get(position));
        Log.d("URL",BASE_URL + IMAGES.get(position) +"/0.jpg");
        Picasso.get().load(BASE_URL + IMAGES.get(position) +"/0.jpg")
                .placeholder(R.drawable.photo)
                .into(imageView);
        view.addView(imageLayout, 0);
        mYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnYoutubeClickListener.OnYoutubeclicked(IMAGES.get(position));
            }
        });
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
