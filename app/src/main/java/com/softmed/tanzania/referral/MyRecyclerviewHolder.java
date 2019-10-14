package com.softmed.tanzania.referral;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvName,tvRegisteredDate,tvVillageName;
    String ClientId;

    public MyRecyclerviewHolder(View itemView) {
        super(itemView);


        itemView.setOnClickListener(this);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvRegisteredDate = (TextView) itemView.findViewById(R.id.tv_time);
        tvVillageName = (TextView) itemView.findViewById(R.id.tv_village);

    }



    @Override
    public void onClick(View view) {

    }
}
