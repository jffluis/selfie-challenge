package com.ipleiria.selfiechallenge.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.fragments.PhotoViewerFragment;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.utils.PhotoUtil;
import com.ipleiria.selfiechallenge.utils.Constants;

import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<Challenge> challengeList;
    private final Activity activity;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView address, name, created_by;
        Button button_enter_challenge;
        ImageView photo1, photo2, photo3, photo4, photo5, photo6;



        ViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.address);
            name = (TextView) view.findViewById(R.id.name);
            created_by = (TextView)view.findViewById(R.id.created_by);
            button_enter_challenge = (Button) view.findViewById(R.id.enter_challenge);
            photo1 = (ImageView) view.findViewById(R.id.photo1);
            photo2 = (ImageView) view.findViewById(R.id.photo2);
            photo3 = (ImageView) view.findViewById(R.id.photo3);
            photo4 = (ImageView) view.findViewById(R.id.photo4);
            photo5 = (ImageView) view.findViewById(R.id.photo5);
            photo6 = (ImageView) view.findViewById(R.id.photo6);

        }

        @Override
        public void onClick(View view) {

        }
    }



    public RVAdapter(List<Challenge> challengeList, Activity activity) {
        this.challengeList = challengeList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Challenge challenge = challengeList.get(position);
        holder.address.setText(challenge.getDescription());
        holder.name.setText(challenge.getName());
        holder.created_by.setText(Constants.CREATEDBY + challenge.getUser().getName());
        holder.button_enter_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Instance.getInstance().setCurrentChallenge(challenge);
                PhotoUtil.startCamera(activity);
            }
        });

        if(challenge.getPhotos().size() > 0){

            Glide
                    .with(activity)
                    .load(challenge.getPhotos().get(0))
                    .crossFade()
                    .animate(R.anim.animacao)
                    .into(holder.photo1);

            holder.photo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Instance.getInstance().challengeToSee = challenge;
                    Instance.getInstance().selectedPhotoPos = 0;

                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, PhotoViewerFragment.newInstance(0)).addToBackStack(null).commit();

                }
            });

        } else {
            holder.photo1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait));
        }
        if(challenge.getPhotos().size() > 1){
            Glide
                    .with(activity)
                    .load(challenge.getPhotos().get(1))
                    .crossFade()
                    .animate(R.anim.animacao)
                    .into(holder.photo2);

            holder.photo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Instance.getInstance().challengeToSee = challenge;
                    Instance.getInstance().selectedPhotoPos = 1;

                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, PhotoViewerFragment.newInstance(position)).addToBackStack(null).commit();

                }
            });
        } else {
            holder.photo1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait));
        }
        if(challenge.getPhotos().size() > 2){
            Glide
                    .with(activity)
                    .load(challenge.getPhotos().get(2))
                    .crossFade()
                    .animate(R.anim.animacao)
                    .into(holder.photo3);

            holder.photo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Instance.getInstance().challengeToSee = challenge;
                    Instance.getInstance().selectedPhotoPos = 2;

                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, PhotoViewerFragment.newInstance(position)).addToBackStack(null).commit();

                }
            });
        }
        if(challenge.getPhotos().size() > 3){
            Glide
                    .with(activity)
                    .load(challenge.getPhotos().get(3))
                    .crossFade()
                    .animate(R.anim.animacao)
                    .into(holder.photo4);

            holder.photo4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Instance.getInstance().challengeToSee = challenge;
                    Instance.getInstance().selectedPhotoPos = 3;

                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, PhotoViewerFragment.newInstance(position)).addToBackStack(null).commit();

                }
            });
        } else {
            holder.photo1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait));
        }
        if(challenge.getPhotos().size() > 4){
            Glide
                    .with(activity)
                    .load(challenge.getPhotos().get(4))
                    .crossFade()
                    .animate(R.anim.animacao)
                    .into(holder.photo5);

            holder.photo5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Instance.getInstance().challengeToSee = challenge;
                    Instance.getInstance().selectedPhotoPos = 4;

                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, PhotoViewerFragment.newInstance(position)).addToBackStack(null).commit();

                }
            });
        } else {
            holder.photo1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait));
        }
        if(challenge.getPhotos().size() > 5){
            Glide
                    .with(activity)
                    .load(challenge.getPhotos().get(5))
                    .crossFade()
                    .animate(R.anim.animacao)
                    .into(holder.photo6);

            holder.photo6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Instance.getInstance().challengeToSee = challenge;
                    Instance.getInstance().selectedPhotoPos = 5;

                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, PhotoViewerFragment.newInstance(position)).addToBackStack(null).commit();

                }
            });
        } else {
            holder.photo1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait));
        }

        int pos = getItemViewType(position);

        if(challenge.getPhotos().size() > 0){
            if(challenge.getPhotos().get(0) == null) {
                holder.photo1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait));
            } else {
                Glide
                        .with(activity)
                        .load(challenge.getPhotos().get(0))
                        .crossFade()
                        .animate(R.anim.animacao)
                        .into(holder.photo1);
            }
        }


    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

}


