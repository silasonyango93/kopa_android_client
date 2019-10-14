package com.softmed.tanzania.referral;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {
    String UserId;
    RecyclerView rView;
    ArrayList<ClientModel> clients_list;
    private GridLayoutManager lLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        UserId=Config.getCurrentSessionId(getActivity());
        clients_list = new ArrayList<>();
        lLayout = new GridLayoutManager(getActivity(), 1);

        rView = (RecyclerView) v.findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                clients_list.clear();

                getSpecificChwClients();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getSpecificChwClients();
        // Inflate the layout for this fragment
        return v;
    }


    private void getSpecificChwClients(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_my_clients, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {



                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("result");

                    for(int i = 0; i<jsonarray.length(); i++){

                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(i);


                            String ClientId=obj.getString("ClientId");
                            String UserId=obj.getString("UserId");
                            String FirstName=obj.getString("FirstName");
                            String MiddleName=obj.getString("MiddleName");
                            String SurName=obj.getString("SurName");
                            String PhoneNumber=obj.getString("PhoneNumber");
                            String Email=obj.getString("Email");
                            String PhysicalAddress=obj.getString("PhysicalAddress");
                            String DOB=obj.getString("DOB");
                            String Gender=obj.getString("Gender");
                            String VillageId=obj.getString("VillageId");
                            String VillageName=obj.getString("VillageName");
                            String WardId=obj.getString("WardId");
                            String WardName=obj.getString("WardName");
                            String VillageRefNo=obj.getString("VillageRefNo");
                            String WardRefNo=obj.getString("WardRefNo");
                            String IsAChildOf=obj.getString("IsAChildOf");
                            String RegistrationDate=obj.getString("RegistrationDate");



                            clients_list.add(new ClientModel(ClientId,UserId,FirstName,MiddleName,SurName,PhoneNumber,Email,PhysicalAddress,DOB,Gender,VillageId,VillageName,WardId,WardName,VillageRefNo,WardRefNo,IsAChildOf,RegistrationDate));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    MyRecyclerviewAdapter rcAdapter = new MyRecyclerviewAdapter(getActivity(),clients_list);
                    rView.setAdapter(rcAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("ggg", volleyError.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("column_name","UserId");
                params.put("search_value",UserId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

}
