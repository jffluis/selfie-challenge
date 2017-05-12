package com.ipleiria.selfiechallenge.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.User;

import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVUserAdapter extends RecyclerView.Adapter<RVUserAdapter.ViewHolder> {

    private List<User> userList;
    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, points;
        ImageView photo;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            points = (TextView) view.findViewById(R.id.points);
            photo = (ImageView) view.findViewById(R.id.img);
        }
    }

    public RVUserAdapter(List<User> userList, Activity activity) {
        this.userList = userList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.points.setText(user.getPoints()+" Points");

        Glide.with(activity).load(user.getPhotoURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.photo) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.photo.setImageDrawable(circularBitmapDrawable);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}


