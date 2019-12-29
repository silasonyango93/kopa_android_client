package com.silas.digitalfactory.kopa;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<Entry> entries;
    int numOfPendingLoans = 0;
    int numOfCompletedLoans = 0;
    int numOfBadDebts = 0;
    View v;

    public FragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_two, container, false);
        // Inflate the layout for this fragment
        pref = getActivity().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        getPendingLoans();

        return v;
    }


    private void getPendingLoans(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_a_company_pending_loans, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");


                    numOfPendingLoans = jsonarray.length();
                    getFullyPaidLoans();

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
                params.put("companyId",pref.getString("CompanyId", null));
                params.put("isFullyPaidStatus","0");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    private void getFullyPaidLoans(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_a_company_pending_loans, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");


                    numOfCompletedLoans = jsonarray.length();
                    getBadDebts();


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
                params.put("companyId",pref.getString("CompanyId", null));
                params.put("isFullyPaidStatus","1");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void getBadDebts(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_a_company_pending_loans, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");


                    numOfBadDebts = jsonarray.length();
                    drawGraph ();


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
                params.put("companyId",pref.getString("CompanyId", null));
                params.put("isFullyPaidStatus","999");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }



    public void drawGraph () {
        PieChart pieChart = (PieChart) v.findViewById(R.id.chart);
        entries = new ArrayList<>();
        entries.add(new Entry(Float.valueOf(numOfCompletedLoans), 0));
        entries.add(new Entry(Float.valueOf(numOfPendingLoans), 1));
        entries.add(new Entry(Float.valueOf(numOfBadDebts), 2));



        PieDataSet dataset = new PieDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Settled Loans");
        labels.add("Pending Loans");
        labels.add("Bad Debts");



        PieData data = new PieData(labels, dataset);
        pieChart.setData(data);
        pieChart.setDescription("A pie chart showing both successful and pending referrals");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateY(5000);
    }

}
