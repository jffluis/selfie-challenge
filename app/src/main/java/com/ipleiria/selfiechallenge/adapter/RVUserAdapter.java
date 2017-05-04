package com.ipleiria.selfiechallenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.User;

import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVUserAdapter extends RecyclerView.Adapter<RVUserAdapter.ViewHolder> {

    private List<User> userList;

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

    public RVUserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.points.setText(user.getPoints()+" PTS");
        //holder.photo.setImageBitmap(user.getPhoto()...blabla converter para bitmap);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}


