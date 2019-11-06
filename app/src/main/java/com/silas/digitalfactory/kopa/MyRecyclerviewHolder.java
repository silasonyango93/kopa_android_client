package com.silas.digitalfactory.kopa;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvName,tvRegisteredDate,tvVillageName;
    NetworkImageView nivPersonalPhoto;
    Context context;
    LayoutInflater inflater,infLoanApplication;
    AlertDialog alertDialog,aldLoanApplication;
    View bView, viewLoanApplication;
    ClientModel clientObject;
    private ImageLoader imageLoader;
    String strLoanSettlementDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public int Year,Month,Day,Hour,Minute,Seconds;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public MyRecyclerviewHolder(View itemView, Context context) {
        super(itemView);


        itemView.setOnClickListener(this);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvRegisteredDate = (TextView) itemView.findViewById(R.id.tv_time);
        tvVillageName = (TextView) itemView.findViewById(R.id.tv_village);
        nivPersonalPhoto = (NetworkImageView)itemView.findViewById(R.id.person_photo);
        this.context = context;
        inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        infLoanApplication =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        pref = context.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }



    @Override
    public void onClick(View view) {
        prepareMoreDetails();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popDisplayMoreClientDetails(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }


    public void prepareMoreDetails() {
        bView = inflater.inflate(R.layout.customer_details,null);
        NetworkImageView nivProfilePicture = (NetworkImageView) bView.findViewById(R.id.profile_pic);
        TextView tvName = (TextView)  bView.findViewById(R.id.tv_name);
        TextView tvGender = (TextView)  bView.findViewById(R.id.tv_gender);
        TextView tvEmail = (TextView)  bView.findViewById(R.id.tv_email);
        TextView tvDob = (TextView)  bView.findViewById(R.id.tv_dob);
        TextView tvNationalId = (TextView)  bView.findViewById(R.id.tv_national_id);
        TextView tvPhoneNumber = (TextView)  bView.findViewById(R.id.tv_phone_number);
        TextView tvPhysicalAddress = (TextView)  bView.findViewById(R.id.tv_physical_address);
        ImageView imvPencil = (ImageView) bView.findViewById(R.id.new_loan);

        imvPencil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                prepareLoanApplication();
            }
        });


        imageLoader = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(clientObject.getImageUrl(), ImageLoader.getImageListener(nivProfilePicture, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        nivProfilePicture.setImageUrl(clientObject.getImageUrl(),imageLoader);
        tvName.setText(clientObject.getByClientName());
        tvGender.setText(clientObject.getGender());
        tvEmail.setText(clientObject.getByClientEmail());
        tvDob.setText(clientObject.getClientDOB());
        tvNationalId.setText(clientObject.getClientNationalId());
        tvPhoneNumber.setText(clientObject.getClientPhoneNumber());
        tvPhysicalAddress.setText(clientObject.getClientPhysicalAddress());
        popDisplayMoreClientDetails(bView);
    }

    public void prepareLoanApplication() {
        viewLoanApplication = infLoanApplication.inflate(R.layout.loan_application_pop,null);
        final EditText etLoanAmount = (EditText) viewLoanApplication.findViewById(R.id.et_loan_amount);
        ImageView imvCalender = (ImageView) viewLoanApplication.findViewById(R.id.calender);
        Button btnSubmit = (Button) viewLoanApplication.findViewById(R.id.btMore);

        imvCalender.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

              String strLoanAmount =  etLoanAmount.getText().toString();
                submitLoanApplication(strLoanAmount);
            }
        });
        popDisplayLoanApplication(viewLoanApplication);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popDisplayLoanApplication(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(true);
        aldLoanApplication = builder.create();
        aldLoanApplication.setCancelable(true);
        aldLoanApplication.setCanceledOnTouchOutside(true);
        aldLoanApplication.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getDate()
    {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        Year=year;
                        Month=(monthOfYear + 1);
                        Day=dayOfMonth;

                        strLoanSettlementDate =(Year + "-" + Month + "-" + Day);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();



    }


    private void submitLoanApplication(final String strLoanAmount){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.add_loan_application, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
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
                params.put("ClientId",pref.getString("ClientId", null));
                params.put("CompanyId",pref.getString("CompanyId", null));
                params.put("CompanyBranchId",pref.getString("CompanyBranchId", null));
                params.put("SystemUserId",pref.getString("SystemUserId", null));
                params.put("LoanAmount",strLoanAmount);
                params.put("ExpectedSettlementDate",strLoanSettlementDate);
                params.put("LoanRating","0");
                params.put("IsFullyPaid","0");
                params.put("RemainingLoanAmount",strLoanAmount);
                params.put("EmploymentStatus",clientObject.getEmploymentStatus());
                params.put("EmploymentCategoryId",clientObject.getEmploymentCategoryId());
                params.put("Occupation",clientObject.getOccupation());
                params.put("EmploymentStation",clientObject.getEmploymentStation());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }
}
