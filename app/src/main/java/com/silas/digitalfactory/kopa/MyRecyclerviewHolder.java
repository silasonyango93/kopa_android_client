package com.silas.digitalfactory.kopa;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class MyRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvName,tvRegisteredDate,tvVillageName;
    String ClientId;
    NetworkImageView nivPersonalPhoto;

    public MyRecyclerviewHolder(View itemView) {
        super(itemView);


        itemView.setOnClickListener(this);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvRegisteredDate = (TextView) itemView.findViewById(R.id.tv_time);
        tvVillageName = (TextView) itemView.findViewById(R.id.tv_village);
        nivPersonalPhoto = (NetworkImageView)itemView.findViewById(R.id.person_photo);

    }



    @Override
    public void onClick(View view) {

    }
}
