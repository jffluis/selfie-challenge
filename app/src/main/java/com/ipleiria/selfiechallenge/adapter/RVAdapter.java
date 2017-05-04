package com.ipleiria.selfiechallenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.POI;

import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<POI> POIList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView address, name;

        ViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.address);
            name = (TextView) view.findViewById(R.id.city);
        }
    }


    public RVAdapter(List<POI> POIList) {
        this.POIList = POIList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        POI poi = POIList.get(position);
        holder.address.setText(poi.getAddress());
        holder.name.setText(poi.getName());
    }

    @Override
    public int getItemCount() {
        return POIList.size();
    }
}


