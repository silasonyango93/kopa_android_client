package com.silas.digitalfactory.kopa;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

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

public class SearchActivity extends AppCompatActivity {
    AutoCompleteTextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    private GridLayoutManager lLayout;
    View v;
    ArrayList<ClientModel> clients_list;
    TextView tvBackgroundIcon,tvBackgroundText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        tvBackgroundIcon=(TextView) findViewById(R.id.tvBackground);
        tvBackgroundText=(TextView) findViewById(R.id.tvBackground2);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        tvBackgroundIcon.setTypeface(font);

        tvBackgroundIcon.setVisibility(View.GONE);
        tvBackgroundText.setVisibility(View.GONE);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflator.inflate(R.layout.actionbar, null);

        getSupportActionBar().setCustomView(v);

        TextView tvSearch = (TextView)findViewById(R.id.dot);

        tvSearch.setTypeface(font);

        clients_list = new ArrayList<>();
        lLayout = new GridLayoutManager(getBaseContext(), 1);

        rView = (RecyclerView) findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                clients_list.clear();

                getSpecificChwClients();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getSpecificChwClients();

    }


    private void getSpecificChwClients(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_all_company_clients, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {



                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    for(int i = 0; i<jsonarray.length(); i++){

                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(i);


                            String ClientId=obj.getString("ClientId");
                            String ClientFirstName=obj.getString("ClientFirstName");
                            String ClientMiddleName=obj.getString("ClientMiddleName");
                            String ClientSurname=obj.getString("ClientSurname");
                            String ClientNationalId=obj.getString("ClientNationalId");
                            String ClientProfilePicName=obj.getString("ClientProfilePicName");
                            String GenderId=obj.getString("GenderId");
                            String ClientDOB=obj.getString("ClientDOB");
                            String ClientPhoneNumber=obj.getString("ClientPhoneNumber");
                            String ClientPhysicalAddress=obj.getString("ClientPhysicalAddress");
                            String ClientEmail=obj.getString("ClientEmail");
                            String ClientRegistrationDate=obj.getString("ClientRegistrationDate");



                            clients_list.add(new ClientModel(ClientId,ClientFirstName,ClientMiddleName,ClientSurname,ClientNationalId,ClientProfilePicName,GenderId,ClientDOB,ClientPhoneNumber,ClientPhysicalAddress,ClientEmail,ClientRegistrationDate));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    MyRecyclerviewAdapter rcAdapter = new MyRecyclerviewAdapter(getBaseContext(),clients_list);
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

//                params.put("column_name","UserId");
//                params.put("search_value",UserId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }
}
