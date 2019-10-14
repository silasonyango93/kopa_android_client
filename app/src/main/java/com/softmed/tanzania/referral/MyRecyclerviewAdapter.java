package com.softmed.tanzania.referral;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyRecyclerviewAdapter  extends RecyclerView.Adapter<MyRecyclerviewHolder> {
    Context context;
    private ArrayList<ClientModel> clients_list;

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
        holder.tvRegisteredDate.setText(clients_list.get(position).getByClientRegistrationDate());
        holder.tvVillageName.setText(clients_list.get(position).getByClientVillageName());
        holder.ClientId=(clients_list.get(position).getByClientId());
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
