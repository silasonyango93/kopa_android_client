package com.silas.digitalfactory.kopa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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

public class SearchActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    AutoCompleteTextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int isBlackListed, star;
    private GridLayoutManager lLayout;
    View v;
    ArrayList<ClientModel> clients_list;
    TextView tvBackgroundIcon,tvBackgroundText;
    String CurrentlyTyped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
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
        textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        lLayout = new GridLayoutManager(getBaseContext(), 1);

        rView = (RecyclerView) findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                clients_list.clear();

                searchClient(CurrentlyTyped);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        final long delay = 1000; // 1 seconds after user stops typing
        final long[] last_text_edit = {0};


        final Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    // TODO: do what you need here
                    // ............
                    // ............
                    clients_list.clear();
                    searchClient(CurrentlyTyped) ;
                    swipeRefreshLayout.setRefreshing(false);


                }
            }
        };

        final Handler handler = new Handler();
        textView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {

                CurrentlyTyped = s.toString().trim();
                CurrentlyTyped =CurrentlyTyped.toLowerCase();

                /*  search(CurrentlyTyped) ;*/

                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit[0] = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }


            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

//You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);

            }
        });


    }


    private void searchClient(final String typedParameter){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.client_any_search, new Response.Listener<String>() {
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



                            calculateAverageLoanRating(ClientId,ClientFirstName,ClientMiddleName,ClientSurname,ClientNationalId,ClientProfilePicName,GenderId,ClientDOB,ClientPhoneNumber,ClientPhysicalAddress,ClientEmail,ClientRegistrationDate,EmploymentStatus,EmploymentCategoryId,Occupation,EmploymentStation);


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

                params.put("searchParameter",typedParameter);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    private void checkLoanStatus(final String clientId, final String clientFirstName, final String clientMiddleName, final String clientSurname, final String clientNationalId, final String clientProfilePicName, final String genderId, final String clientDOB, final String clientPhoneNumber, final String clientPhysicalAddress, final String clientEmail, final String clientRegistrationDate, final String employmentStatus, final String employmentCategoryId, final String occupation, final String employmentStation, final int star, final int isBlackListed){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.pending_loan_with_current_company, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        String LoanAmount = "N/A", RemainingLoanAmount="N/A";
                        clients_list.add(new ClientModel(clientId,clientFirstName,clientMiddleName,clientSurname,clientNationalId,clientProfilePicName,genderId,clientDOB,clientPhoneNumber,clientPhysicalAddress,clientEmail,clientRegistrationDate,employmentStatus,employmentCategoryId,occupation,employmentStation,LoanAmount,RemainingLoanAmount,star,isBlackListed));
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(0);

                            String LoanAmount=obj.getString("LoanAmount");
                            String RemainingLoanAmount=obj.getString("RemainingLoanAmount");
                            clients_list.add(new ClientModel(clientId,clientFirstName,clientMiddleName,clientSurname,clientNationalId,clientProfilePicName,genderId,clientDOB,clientPhoneNumber,clientPhysicalAddress,clientEmail,clientRegistrationDate,employmentStatus,employmentCategoryId,occupation,employmentStation,LoanAmount,RemainingLoanAmount,star,isBlackListed));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    MyRecyclerviewAdapter rcAdapter = new MyRecyclerviewAdapter(SearchActivity.this,clients_list);
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

        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    private void calculateAverageLoanRating(final String clientId, final String clientFirstName, final String clientMiddleName, final String clientSurname, final String clientNationalId, final String clientProfilePicName, final String genderId, final String clientDOB, final String clientPhoneNumber, final String clientPhysicalAddress, final String clientEmail, final String clientRegistrationDate, final String employmentStatus, final String employmentCategoryId, final String occupation, final String employmentStation){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_specific_loan_application, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        Toast.makeText(SearchActivity.this,"This client has no pending loan with us", Toast.LENGTH_LONG).show();
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index

                        Float averageRating = Float.valueOf(0);
                        for(int i = 0; i<jsonarray.length(); i++) {

                            JSONObject obj = null;
                            try {

                                obj = jsonarray.getJSONObject(i);


                                String LoanRating = obj.getString("LoanRating");
                                String isFullyPaidstatus = obj.getString("IsFullyPaid");

                                if(isFullyPaidstatus.equals("999")) {
                                    isBlackListed = 1;
                                }

                                Float flLoanRating = Float.parseFloat(LoanRating);
                                averageRating = averageRating + flLoanRating;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        if(averageRating <= 2.5) {
                            star = Config.GREY_STAR;
                        } else if(averageRating > 2.5 && averageRating < 4) {
                            star = Config.GREEN_STAR;
                        } else if(averageRating > 4) {
                            star = Config.YELLOW_STAR;
                        }

                        checkLoanStatus(clientId,clientFirstName,clientMiddleName,clientSurname,clientNationalId,clientProfilePicName,genderId,clientDOB,clientPhoneNumber,clientPhysicalAddress,clientEmail,clientRegistrationDate,employmentStatus,employmentCategoryId,occupation,employmentStation,star,isBlackListed);



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
                params.put("column_name","clientId");
                params.put("search_value",clientId);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }
}
