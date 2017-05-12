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
import com.ipleiria.selfiechallenge.model.POI;

import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVPOIAdapter extends RecyclerView.Adapter<RVPOIAdapter.ViewHolder> {

    private List<POI> poiList;
    private final Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, address;
        ImageView photo;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_poi);
            address = (TextView) view.findViewById(R.id.address_poi);
            photo = (ImageView) view.findViewById(R.id.img_poi);
        }
    }

    public RVPOIAdapter(List<POI> poiList, Activity activity) {
        this.activity = activity;
        this.poiList = poiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        POI poi = poiList.get(position);
        holder.name.setText(poi.getName());
        holder.address.setText(poi.getAddress());
        Glide.with(activity).load(poi.getUrlPhoto()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.photo) {
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
        return poiList.size();
    }
}


