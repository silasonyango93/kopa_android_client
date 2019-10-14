package com.softmed.tanzania.referral;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyListAdapter extends ArrayAdapter<MyBasket> {
    DatabaseHelper myDb;
    AlertDialog myAlertDialog;
    LayoutInflater inflater;
    Button btContinueBook,btCancelBook;
    List<MyBasket> mybasketList;
    Context context;
    int resource;
    String strFirstName,strMiddleName,strSurname,strPhoneNumber,strEmail,strPhysicalAddress,strParent,strGender,strDOB;
    String strVillageId,strVillageName,strVillageRefNo,strWardId,strWardName,strWardRefNo,strUserId;


    public MyListAdapter(Context context, int resource, List<MyBasket> mybasketList, String strFirstName, String strMiddleName, String strSurname, String strPhoneNumber, String strEmail, String strPhysicalAddress, String strParent, String strGender,String strDOB,AlertDialog myAlertDialog) {
        super(context, resource, mybasketList);
        this.context = context;
        this.resource = resource;
        this.mybasketList = mybasketList;

        this.strFirstName = strFirstName;
        this.strMiddleName = strMiddleName;
        this.strSurname = strSurname;
        this.strPhoneNumber = strPhoneNumber;
        this.strEmail = strEmail;
        this.strPhysicalAddress = strPhysicalAddress;
        this.strParent = strParent;
        this.strGender = strGender;
        this.strDOB = strDOB;
        this.myAlertDialog = myAlertDialog;



        inflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        myDb = new DatabaseHelper(context);
        getWardDetails();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);
        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        CheckBox cbVillage = (CheckBox) view.findViewById(R.id.cb_village);
        final MyBasket basket_object = mybasketList.get(position);

        textViewName.setText(basket_object.getByName());




        cbVillage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    strVillageId=basket_object.getByServerId();
                    strVillageName=basket_object.getByName();
                    strVillageRefNo=basket_object.getByRefNo();
                    myAlertDialog.cancel();
                    submitClientDetails();

                }

            }
        });




        return view;
    }


    private void submitClientDetails(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.client_registration, new Response.Listener<String>() {
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
                            //getting json object from current index
                            obj = jsonarray.getJSONObject(i);



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
                params.put("UserId",strUserId);
                params.put("FirstName",strFirstName);
                params.put("MiddleName",strMiddleName);
                params.put("SurName",strSurname);
                params.put("PhoneNumber",strPhoneNumber);
                params.put("Email",strEmail);
                params.put("PhysicalAddress",strPhysicalAddress);
                params.put("DOB",strDOB);
                params.put("Gender",strGender);
                params.put("VillageId",strVillageId);
                params.put("VillageName",strVillageName);
                params.put("WardId",strWardId);
                params.put("WardName",strWardName);
                params.put("VillageRefNo",strVillageRefNo);
                params.put("WardRefNo",strWardRefNo);
                params.put("IsAChildOf",strParent);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    public void getWardDetails(){

        Cursor res = myDb.getAllCredentials();

        if (res.getCount() == 0) {
            //Show message
            //showMessage("No PaintShares Available", "You currently have no PaintShares saved");
            return;
        }



        while (res.moveToNext()) {
            strUserId=res.getString(1);
            strWardId=res.getString(7);
            strWardName=res.getString(8);
            strWardRefNo=res.getString(9);

        }
    }


}