package com.silas.digitalfactory.kopa;

import android.content.Context;
import android.content.res.Resources;
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
        MyRecyclerviewHolder rcv = new MyRecyclerviewHolder(layoutView, context);

        return rcv;
    }


    @Override
    public void onBindViewHolder(MyRecyclerviewHolder holder, int position) {
        int star = 0, isBlackListed = 0;
        Resources res = context.getResources();
        holder.tvName.setText(clients_list.get(position).getByClientName());
        holder.tvRegisteredDate.setText(clients_list.get(position).getByRegistrationDate());
        holder.tvVillageName.setText(clients_list.get(position).getByClientEmail());
        holder.clientObject=(clients_list.get(position));

        star = clients_list.get(position).getStar();
        isBlackListed = clients_list.get(position).getIsBlackListed();

        if(isBlackListed == 0 && star == Config.GREY_STAR) {
            holder.imvOverallRating.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
        } else if(isBlackListed == 0 && star == Config.GREEN_STAR) {
            holder.imvOverallRating.setImageDrawable(res.getDrawable(R.mipmap.green_start));
        } else if(isBlackListed == 0 && star == Config.YELLOW_STAR) {
            holder.imvOverallRating.setImageDrawable(res.getDrawable(R.mipmap.full_star));
        } else if(isBlackListed == 1) {
            holder.imvOverallRating.setImageDrawable(res.getDrawable(R.mipmap.red_start));
        }

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
