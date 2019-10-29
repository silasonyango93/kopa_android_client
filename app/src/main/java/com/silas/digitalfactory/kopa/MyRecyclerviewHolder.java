package com.silas.digitalfactory.kopa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

public class MyRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvName,tvRegisteredDate,tvVillageName;
    String ClientId;
    NetworkImageView nivPersonalPhoto;
    Context context;
    LayoutInflater inflater;

    public MyRecyclerviewHolder(View itemView, Context context) {
        super(itemView);


        itemView.setOnClickListener(this);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvRegisteredDate = (TextView) itemView.findViewById(R.id.tv_time);
        tvVillageName = (TextView) itemView.findViewById(R.id.tv_village);
        nivPersonalPhoto = (NetworkImageView)itemView.findViewById(R.id.person_photo);
        this.context = context;
        inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.customer_details,null);

    }



    @Override
    public void onClick(View view) {
        Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show();
    }
}
