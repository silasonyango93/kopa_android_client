package com.silas.digitalfactory.kopa;


import android.content.SharedPreferences;
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
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        UserId="";
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
                            String EmploymentStatus=obj.getString("EmploymentStatus");
                            String EmploymentCategoryId=obj.getString("CategoryDescription");
                            String Occupation=obj.getString("Occupation");
                            String EmploymentStation=obj.getString("EmploymentStation");

                            checkLoanStatus(ClientId,ClientFirstName,ClientMiddleName,ClientSurname,ClientNationalId,ClientProfilePicName,GenderId,ClientDOB,ClientPhoneNumber,ClientPhysicalAddress,ClientEmail,ClientRegistrationDate,EmploymentStatus,EmploymentCategoryId,Occupation,EmploymentStation);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void checkLoanStatus(final String clientId, final String clientFirstName, final String clientMiddleName, final String clientSurname, final String clientNationalId, final String clientProfilePicName, final String genderId, final String clientDOB, final String clientPhoneNumber, final String clientPhysicalAddress, final String clientEmail, final String clientRegistrationDate, final String employmentStatus, final String employmentCategoryId, final String occupation, final String employmentStation){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.pending_loan_with_current_company, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        String LoanAmount = "N/A", RemainingLoanAmount="N/A";
                        clients_list.add(new ClientModel(clientId,clientFirstName,clientMiddleName,clientSurname,clientNationalId,clientProfilePicName,genderId,clientDOB,clientPhoneNumber,clientPhysicalAddress,clientEmail,clientRegistrationDate,employmentStatus,employmentCategoryId,occupation,employmentStation,LoanAmount,RemainingLoanAmount));
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(0);

                            String LoanAmount=obj.getString("LoanAmount");
                            String RemainingLoanAmount=obj.getString("RemainingLoanAmount");
                            clients_list.add(new ClientModel(clientId,clientFirstName,clientMiddleName,clientSurname,clientNationalId,clientProfilePicName,genderId,clientDOB,clientPhoneNumber,clientPhysicalAddress,clientEmail,clientRegistrationDate,employmentStatus,employmentCategoryId,occupation,employmentStation,LoanAmount,RemainingLoanAmount));

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
                params.put("clientId",clientId);
                params.put("companyId",pref.getString("CompanyId", null));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

}
