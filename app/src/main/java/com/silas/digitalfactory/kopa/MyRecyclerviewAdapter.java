package com.silas.digitalfactory.kopa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class MyRecyclerviewAdapter  extends RecyclerView.Adapter<MyRecyclerviewHolder> {
    Context context;
    private ArrayList<ClientModel> clients_list;
    private ImageLoader imageLoader;
    public NetworkImageView networkImageView;

    public MyRecyclerviewAdapter(Context context,ArrayList<ClientModel> clients_list) {

     this.clients_list=clients_list;
     this.context=context;


    }


    @Override
    public MyRecyclerviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View layoutView = LayoutInflater.from(context).inflate(R.layout.client_card,null);
        MyRecyclerviewHolder rcv = new MyRecyclerviewHolder(layoutView);

        return rcv;
    }


    @Override
    public void onBindViewHolder(MyRecyclerviewHolder holder, int position) {
        holder.tvName.setText(clients_list.get(position).getByClientName());
        holder.tvRegisteredDate.setText(clients_list.get(position).getByRegistrationDate());
        holder.tvVillageName.setText(clients_list.get(position).getByClientEmail());
        holder.ClientId=(clients_list.get(position).getByClientId());

        imageLoader = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(clients_list.get(position).getImageUrl(), ImageLoader.getImageListener(holder.nivPersonalPhoto, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.nivPersonalPhoto.setImageUrl(clients_list.get(position).getImageUrl(),imageLoader);
    }


    @Override
    public int getItemCount() {

       return this.clients_list.size();

    }



    @Override
    public long getItemId(int position) {
        return 0;
    }

}
