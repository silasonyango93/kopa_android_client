package com.silas.digitalfactory.kopa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public  String UserId,WardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // sessionManager = new SessionManager(getBaseContext());
        setContentView(R.layout.activity_sign_in);
        myDb = new DatabaseHelper(getBaseContext());
        tvPrompt=(TextView) findViewById(R.id.prompt);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "JosefinSans-Light.ttf");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
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

//                        Cursor res = myDb.getLocalCreds("row");
//
//                        if (res.getCount() == 0) {
//
//                            return;
//                        }
//
//                        StringBuffer buffer = new StringBuffer();
//                        while (res.moveToNext()) {
//
//
//                           String JobRefNo = res.getString(1);
//                           String Password = res.getString(2);
//
//                           if(JobRefNo.equals(email)&&Password.equals(password))
//                            {Toast.makeText(getBaseContext(), "You are currently logged into the local environment", Toast.LENGTH_LONG).show();
//                              Intent intent = new Intent(
//                                        getBaseContext(),ChwHomePage.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                startActivity(intent);
//                            }
//                            else{Toast.makeText(getBaseContext(), "Incorrect credentials", Toast.LENGTH_LONG).show();}
//
//                        }
                        Toast.makeText(getBaseContext(), "Kindly check your internet connectivity and try again", Toast.LENGTH_LONG).show();

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
                    String SystemUserId,CompanyBranchId,UserFirstName,UserMiddleName,UserSurname,GenderId,StaffNo,UserNationalId,UserEmail,UserPhoneNumber,UserPhysicalAddress,UserRegistrationDate;
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (error == true) {//When response returns error
                        String errorMessage = jObj.getString("error_msg");
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                        hideDialog();
                    } else if (error == false) {
                        SystemUserId = jObj.getString("SystemUserId");
                        CompanyBranchId = jObj.getString("CompanyBranchId");
                        UserFirstName = jObj.getString("UserFirstName");
                        UserMiddleName = jObj.getString("UserMiddleName");
                        UserSurname = jObj.getString("UserSurname");
                        GenderId = jObj.getString("GenderId");
                        StaffNo = jObj.getString("StaffNo");
                        UserNationalId = jObj.getString("UserNationalId");
                        UserEmail = jObj.getString("UserEmail");
                        UserPhoneNumber = jObj.getString("UserPhoneNumber");
                        UserPhysicalAddress = jObj.getString("UserPhysicalAddress");
                        UserRegistrationDate = jObj.getString("UserRegistrationDate");

                        fetchMyCompanyDetails(SystemUserId,CompanyBranchId,UserFirstName,UserMiddleName,UserSurname,GenderId,StaffNo,UserNationalId,UserEmail,UserPhoneNumber,UserPhysicalAddress,UserRegistrationDate);





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
                params.put("AttemptedEmail", email);
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





    public void fetchMyCompanyDetails(final String systemUserId, final String companyBranchId, final String userFirstName, final String userMiddleName, final String userSurname, final String genderId, final String staffNo, final String userNationalId, final String userEmail, final String userPhoneNumber, final String userPhysicalAddress, final String userRegistrationDate){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_my_company_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");
                    JSONObject obj = jsonarray.getJSONObject(0);

                    String CompanyId=obj.getString("CompanyId");



                    editor.putString("SystemUserId", systemUserId);
                    editor.putString("CompanyBranchId", companyBranchId);
                    editor.putString("UserFirstName", userFirstName);
                    editor.putString("UserMiddleName", userMiddleName);
                    editor.putString("UserSurname", userSurname);
                    editor.putString("GenderId", genderId);
                    editor.putString("StaffNo", staffNo);
                    editor.putString("UserNationalId", userNationalId);
                    editor.putString("UserEmail", userEmail);
                    editor.putString("UserPhoneNumber", userPhoneNumber);
                    editor.putString("UserPhysicalAddress", userPhysicalAddress);
                    editor.putString("UserRegistrationDate", userRegistrationDate);
                    editor.putString("CompanyId", CompanyId);
                    editor.commit();

                    startSessionLog(systemUserId);


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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("SystemUserId",systemUserId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


    public void startSessionLog(final String systemUserId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.add_session_logs, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    //JSONArray jsonarray= object.getJSONArray("results");
                    JSONObject obj = object.getJSONObject("results");
                    String dbSessionLogId =obj.getString("recordId");
                    editor.putString("dbSessionLogId", dbSessionLogId);
                    editor.commit();

                    Intent intent = new Intent(
                            getBaseContext(),ChwHomePage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    hideDialog();

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("SystemUserId",systemUserId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


}

