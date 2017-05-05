package com.ipleiria.selfiechallenge.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.util.ValueIterator;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.activity.MainActivity;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 04/05/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<Challenge> challengeList;

    private final Context context;



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView address, name;
        Button button_enter_challenge;

        ViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.address);
            name = (TextView) view.findViewById(R.id.name);
            button_enter_challenge = (Button) view.findViewById(R.id.enter_challenge);
        }

        @Override
        public void onClick(View view) {

        }
    }



    public RVAdapter(List<Challenge> challengeList, Context context) {
        this.challengeList = challengeList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);
        holder.address.setText("não há");
        holder.name.setText(challenge.getName());

        holder.button_enter_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "OUUUUU" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                ((MainActivity)context).startCamera();
            }
        });
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

}


