package com.ipleiria.selfiechallenge.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.utils.Firebase;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;


public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private Challenge challenge;
    private LayoutInflater inflater;
    private ProgressBar progressBar;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  Challenge challenge) {
        this.activity = activity;
        this.challenge = challenge;
    }

    @Override
    public int getCount() {
        return this.challenge.getPhotos().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        LikeButton likeButton = (LikeButton) viewLayout.findViewById(R.id.like_button);



        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                //TODO
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                //TODO

            }
        });

        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        final ProgressBar progressBar = (ProgressBar) viewLayout.findViewById(R.id.progress);

        Glide
                .with(activity)
                .load(challenge.getPhotos().get(position))
                .crossFade()
                .animate(R.anim.fade_in).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .into(imgDisplay);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) activity).getSupportFragmentManager().popBackStack();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}