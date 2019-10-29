package com.silas.digitalfactory.kopa;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class MyRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvName,tvRegisteredDate,tvVillageName;
    NetworkImageView nivPersonalPhoto;
    Context context;
    LayoutInflater inflater;
    AlertDialog alertDialog;
    View bView;
    ClientModel clientObject;
    private ImageLoader imageLoader;

    public MyRecyclerviewHolder(View itemView, Context context) {
        super(itemView);


        itemView.setOnClickListener(this);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvRegisteredDate = (TextView) itemView.findViewById(R.id.tv_time);
        tvVillageName = (TextView) itemView.findViewById(R.id.tv_village);
        nivPersonalPhoto = (NetworkImageView)itemView.findViewById(R.id.person_photo);
        this.context = context;
        inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);





    }



    @Override
    public void onClick(View view) {
        prepareMoreDetails();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popDisplayMoreClientDetails(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }


    public void prepareMoreDetails() {
        bView = inflater.inflate(R.layout.customer_details,null);
        NetworkImageView nivProfilePicture = (NetworkImageView) bView.findViewById(R.id.profile_pic);
        TextView tvName = (TextView)  bView.findViewById(R.id.tv_name);
        TextView tvGender = (TextView)  bView.findViewById(R.id.tv_gender);
        TextView tvEmail = (TextView)  bView.findViewById(R.id.tv_email);


        imageLoader = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(clientObject.getImageUrl(), ImageLoader.getImageListener(nivProfilePicture, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        nivProfilePicture.setImageUrl(clientObject.getImageUrl(),imageLoader);
        tvName.setText(clientObject.getByClientName());
        tvGender.setText(clientObject.getGender());
        tvEmail.setText(clientObject.getByClientEmail());
        popDisplayMoreClientDetails(bView);
    }
}
