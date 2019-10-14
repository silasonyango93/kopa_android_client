package com.softmed.tanzania.referral;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends Activity implements View.OnClickListener {
    DatabaseHelper myDb;
    TextView tvPrompt;
    ImageView img;
    EditText et_email, et_password;
    private RequestPermissionHandler mRequestPermissionHandler;
    SessionManager sessionManager;
    ProgressDialog dialog;
    private String email;
    private String password;

    public  String UserId,WardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // sessionManager = new SessionManager(getBaseContext());
        setContentView(R.layout.activity_sign_in);
        myDb = new DatabaseHelper(getBaseContext());
        tvPrompt=(TextView) findViewById(R.id.prompt);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "JosefinSans-Light.ttf");
        mRequestPermissionHandler = new RequestPermissionHandler();
        // tvConservation.setTypeface(custom_font);
        tvPrompt.setTypeface(custom_font);

        img=(ImageView) findViewById(R.id.img1);

        img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.meme,  700, 700));

        findViewById(R.id.btn_submit_login).setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        et_email = (EditText) findViewById(R.id.et_emailLogin);
        et_password = (EditText) findViewById(R.id.et_passwordLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_login:
                if (checkEmpty()) {


                    boolean network_connection= false;
                    try {
                        network_connection = Config.isConnected();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(network_connection==true){SignIn(email, password);}else{

                        Cursor res = myDb.getLocalCreds("row");

                        if (res.getCount() == 0) {

                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {


                           String JobRefNo = res.getString(1);
                           String Password = res.getString(2);

                           if(JobRefNo.equals(email)&&Password.equals(password))
                            {Toast.makeText(getBaseContext(), "You are currently logged into the local environment", Toast.LENGTH_LONG).show();
                              Intent intent = new Intent(
                                        getBaseContext(),ChwHomePage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                            }
                            else{Toast.makeText(getBaseContext(), "Incorrect credentials", Toast.LENGTH_LONG).show();}

                        }

                    }



                }
                break;

            // startActivity(in);
        }
    }

    private boolean checkEmpty() {
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        et_email.setError(null);
        et_password.setError(null);
        if (email.isEmpty()) {
            et_email.setError(getString(R.string.error_email_required));
            et_email.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            et_password.setError(getString(R.string.error_password_required));
            et_password.requestFocus();
            return false;
        } else {
            return true;

        }

    }

    private void SignIn(final String email, final String password) {
        String tag_string_req = "req_login";
        dialog.setMessage("Signing in ...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (error) {//When response returns error
                        String errorMessage = jObj.getString("error_msg");
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                        hideDialog();
                    } else {

                        String row="row";
                        boolean cred_success=myDb.updateLocalCreds(email,password,row);
                        if(cred_success==true){Toast.makeText(getBaseContext(), "Local authentication updated", Toast.LENGTH_LONG).show();}else{Toast.makeText(getBaseContext(), "Local authentication environment setup failed", Toast.LENGTH_LONG).show();}
                        UserId=jObj.getString("UserId");
                        WardId=jObj.getString("WardId");
                        String WardName=jObj.getString("WardName");
                        String WardRefNo=jObj.getString("WardRefNo");
                        String UserId = jObj.getString("UserId");
                        String RoleId = jObj.getString("RoleId");
                        String FirstName = jObj.getString("FirstName");
                        String MiddleName = jObj.getString("MiddleName");
                        String SurName = jObj.getString("SurName");
                        String JobRefNo = jObj.getString("JobRefNo");
                        String mKey="User";
                        updateCredentials(UserId,RoleId,FirstName,MiddleName,SurName,JobRefNo,WardId,WardName,WardRefNo,mKey);

                        if(RoleId.equals("1")){getVillageJurisdictions();getFacilityJurisdictions();

                            Intent intent = new Intent(
                                    getBaseContext(),ChwHomePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);

                        }else{


                            getMyFacility();

                            Intent intent = new Intent(
                                    getBaseContext(),FacilityHomePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);




                        }







                        hideDialog();
                        //finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    hideDialog();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AttemptedJobRefNo", email);
                params.put("AttemptedPassword", password);
                return params;
            }
        };
        Mimi.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}



    public void updateCredentials(String UserId,String RoleId,String FirstName,String MiddleName,String SurName,String JobRefNo,String WardId,String WardName,String WardRefNo,String mKey) {

        Cursor res = myDb.getAllCredentials();

        if (res.getCount() == 0) {
            boolean success= myDb.insertData(UserId,RoleId,FirstName,MiddleName,SurName,JobRefNo,WardId,WardName,WardRefNo,mKey);
            if(success==true){Toast.makeText(getBaseContext(), "Login was successful", Toast.LENGTH_LONG).show();}else{Toast.makeText(getBaseContext(), "Error while logging in", Toast.LENGTH_LONG).show();}
            return;
        }else{boolean success= myDb.updateCredentials(UserId,RoleId,FirstName,MiddleName,SurName,JobRefNo,WardId,WardName,WardRefNo,mKey);
            if(success==true){Toast.makeText(getBaseContext(), "Login was successful", Toast.LENGTH_LONG).show();}else{Toast.makeText(getBaseContext(), "Error while logging in", Toast.LENGTH_LONG).show();}}



    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }



    private void getVillageJurisdictions(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.chw_jurisdiction_villages, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {



                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("result");
                    myDb.deleteEntireTable("chw_village_jurisdiction");
                    for(int i = 0; i<jsonarray.length(); i++){

                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonarray.getJSONObject(i);


                            //getting image url and title from json object

                            String VillageId=obj.getString("VillageId");
                            String VillageName=obj.getString("VillageName");
                            String VillageRefNo=obj.getString("VillageRefNo");


                           boolean success= myDb.insertVillageJurisdiction(VillageId,VillageName,VillageRefNo);
                           if(success==true){Toast.makeText(getBaseContext(), "Locations configuration is ready", Toast.LENGTH_LONG).show();}else{Toast.makeText(getBaseContext(), "Error while configuring locations", Toast.LENGTH_LONG).show();}
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

                params.put("UserId",UserId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }





    private void getFacilityJurisdictions(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.chw_jurisdiction_facilities, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {



                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("result");
                    myDb.deleteEntireTable("chw_facility_jurisdiction");
                    for(int i = 0; i<jsonarray.length(); i++){

                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonarray.getJSONObject(i);


                            //getting image url and title from json object

                            String FacilityId=obj.getString("FacilityId");
                            String FacilityName=obj.getString("FacilityName");
                            String PhysicalAddress=obj.getString("PhysicalAddress");
                            String FacilityRefNo=obj.getString("FacilityRefNo");


                            boolean success= myDb.insertFacilityJurisdiction(FacilityId,FacilityName,PhysicalAddress,FacilityRefNo);
                            if(success==true){Toast.makeText(getBaseContext(), "Facilities configuration is ready", Toast.LENGTH_LONG).show();}else{Toast.makeText(getBaseContext(), "Error while configuring Facilities", Toast.LENGTH_LONG).show();}
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
                params.put("column_name","WardId");
                params.put("search_value",WardId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }




    private void getMyFacility(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_my_facility, new Response.Listener<String>() {
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


                            //getting image url and title from json object

                            String FacilityId=obj.getString("FacilityId");
                            String FacilityName=obj.getString("FacilityName");
                            String PhysicalAddress=obj.getString("PhysicalAddress");
                            String FacilityRefNo=obj.getString("FacilityRefNo");



                            boolean success= myDb.updateMyFacility(FacilityId,FacilityName,PhysicalAddress,FacilityRefNo,"row");
                            if(success==true){Toast.makeText(getBaseContext(), "Your facility configuration is ready", Toast.LENGTH_LONG).show();}else{Toast.makeText(getBaseContext(), "Error while configuring Your facility configuration", Toast.LENGTH_LONG).show();}
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

                params.put("UserId",UserId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }



}

