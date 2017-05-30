package com.ipleiria.selfiechallenge.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.POI;
import com.ipleiria.selfiechallenge.utils.Constants;
import com.ipleiria.selfiechallenge.utils.PhotoUtil;

import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVPOIAdapter extends RecyclerView.Adapter<RVPOIAdapter.ViewHolder> {

    private List<POI> poiList;
    private Location location;
    private final Activity activity;
    public static final int CAMERA_SMART = 33;
    private GoogleApiClient client;
    private RVPOIAdapter _this;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, address;
        ImageView photo;
        Button btn_enter;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_poi);
            address = (TextView) view.findViewById(R.id.address_poi);
            photo = (ImageView) view.findViewById(R.id.img_poi);
            btn_enter = (Button) view.findViewById(R.id.btn_enter);
        }
    }

    public RVPOIAdapter(List<POI> poiList, Activity activity) {
        this.activity = activity;
        this.poiList = poiList;
        _this = this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi, parent, false);

        client = new GoogleApiClient.Builder(activity)
                .addApi(Awareness.API)
                .build();
        client.connect();

        getLocation();


        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final POI poi = poiList.get(position);
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

        holder.btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(location != null) {
                    if (location.distanceTo(poi.getLocation()) <= Constants.DISTANCE_TO_POI) {
                        PhotoUtil.startCamera(activity, CAMERA_SMART);
                        Instance.getInstance().posToDelete = position;
                        System.out.println("position to delete: " + position);
                    } else {
                        showError("You must be less than " + Constants.DISTANCE_TO_POI + "m from the POI to enter challenge!");
                    }
                }else {
                    showError("Couldn't get your location");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    private void getLocation() {
        checkLocationPermission();
        Awareness.SnapshotApi.getLocation(client)
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        if (!locationResult.getStatus().isSuccess()) {
                            showError("Couldn't get your location");
                        }
                        location = locationResult.getLocation();
                    }
                });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        3);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        3);
            }
            return false;
        } else {
            return true;
        }
    }

    private void showError(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Information");
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_error);

        String positiveText = "OK";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

}


