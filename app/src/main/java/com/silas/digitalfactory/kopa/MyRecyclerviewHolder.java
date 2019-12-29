package com.silas.digitalfactory.kopa;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvName,tvRegisteredDate,tvVillageName;
    NetworkImageView nivPersonalPhoto;
    Context context;
    LayoutInflater inflater,infLoanApplication;
    AlertDialog alertDialog,aldLoanApplication,installmentDialog,ratingDialog,blackListDialog;
    View bView, viewLoanApplication;
    ClientModel clientObject;
    private ImageLoader imageLoader;
    String strLoanSettlementDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public int Year,Month,Day,Hour,Minute,Seconds;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageView imvFirstStar,imvSecondStar,imvThirdStar,imvFourthStar,imvFifthStar;

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


        imvFirstStar = (ImageView) bView.findViewById(R.id.first_star);
        imvSecondStar = (ImageView) bView.findViewById(R.id.second_star);
        imvThirdStar = (ImageView) bView.findViewById(R.id.third_star);
        imvFourthStar = (ImageView) bView.findViewById(R.id.fourth_star);
        imvFifthStar = (ImageView) bView.findViewById(R.id.fifth_star);

        imvFirstStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View p = inflater.inflate(R.layout.rating_list_pop, null);
                popStartRatingDialog(p,context);

            }
        });

        imvSecondStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View p = inflater.inflate(R.layout.rating_list_pop, null);
                popStartRatingDialog(p,context);

            }
        });
        imvThirdStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View p = inflater.inflate(R.layout.rating_list_pop, null);
                popStartRatingDialog(p,context);

            }
        });
        imvFourthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View p = inflater.inflate(R.layout.rating_list_pop, null);
                popStartRatingDialog(p,context);

            }
        });
        imvFifthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View p = inflater.inflate(R.layout.rating_list_pop, null);
                popStartRatingDialog(p,context);

            }
        });

        TextView tvName = (TextView)  bView.findViewById(R.id.tv_name);
        TextView tvGender = (TextView)  bView.findViewById(R.id.tv_gender);
        TextView tvEmail = (TextView)  bView.findViewById(R.id.tv_email);
        TextView tvDob = (TextView)  bView.findViewById(R.id.tv_dob);
        TextView tvNationalId = (TextView)  bView.findViewById(R.id.tv_national_id);
        TextView tvPhoneNumber = (TextView)  bView.findViewById(R.id.tv_phone_number);
        TextView tvPhysicalAddress = (TextView)  bView.findViewById(R.id.tv_physical_address);
        TextView tvEmploymentstatus = (TextView)  bView.findViewById(R.id.tv_employment_status);
        TextView tvEmploymentCategory = (TextView)  bView.findViewById(R.id.tv_employment_category);
        TextView tvOccupation = (TextView)  bView.findViewById(R.id.tv_occupation);
        TextView tvEmploymentStation = (TextView)  bView.findViewById(R.id.tv_employment_station);
        TextView tvLoanAmount = (TextView)  bView.findViewById(R.id.tv_loan_amount);
        TextView tvRemainingLoanAmount = (TextView)  bView.findViewById(R.id.tv_remaining_loan_amount);

        ImageView imvPencil = (ImageView) bView.findViewById(R.id.new_loan);
        ImageView imvDollar = (ImageView) bView.findViewById(R.id.loan_installment);
        ImageView imvBlacklist = (ImageView) bView.findViewById(R.id.blacklist);

        imvPencil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getIfClientHasPendingLoanWithCurrentCompany();
            }
        });

        imvDollar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkLoanStatus();
            }
        });

        imvBlacklist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkLoanStatusBeforeBlackListing();
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

        tvEmploymentstatus.setText(clientObject.getEmploymentStatus());
        tvEmploymentCategory.setText(clientObject.getEmploymentCategoryId());
        tvOccupation.setText(clientObject.getOccupation());
        tvEmploymentStation.setText(clientObject.getEmploymentStation());

        tvLoanAmount.setText(clientObject.getLoanAmount());
        tvRemainingLoanAmount.setText(clientObject.getRemainingLoanAmount());
        calculateAverageLoanRating();
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

              String strLoanAmount =  etLoanAmount.getText().toString().trim();
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



    private void getIfClientHasPendingLoanWithCurrentCompany(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.pending_loan_with_current_company, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length()>0) {
                        Toast.makeText(context,"This client has a pending loan with us", Toast.LENGTH_LONG).show();
                    } else {
                        prepareLoanApplication();
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
                params.put("clientId",clientObject.getByClientId());
                params.put("companyId",pref.getString("CompanyId", null));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }



    private void checkLoanStatus(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.pending_loan_with_current_company, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        Toast.makeText(context,"This client has no pending loan with us", Toast.LENGTH_LONG).show();
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(0);


                            String LoanApplicationId=obj.getString("LoanApplicationId");
                            String LoanAmount=obj.getString("LoanAmount");
                            prepareInstallmentPop(LoanApplicationId,LoanAmount);


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
                params.put("clientId",clientObject.getByClientId());
                params.put("companyId",pref.getString("CompanyId", null));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    public void prepareInstallmentPop(final String loanApplicationId, final String loanAmount) {
        View viewInstallmentPop = infLoanApplication.inflate(R.layout.installment_pop,null);
        final EditText etInstallmentAmount = (EditText) viewInstallmentPop.findViewById(R.id.et_installment);
        Button btnSubmit = (Button) viewInstallmentPop.findViewById(R.id.btMore);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int fetchedLoanoanAmount,installmentAmount,remainingLoanAmount;
                String isFullyPaid = "0";
                String strInstallmentAmount =  etInstallmentAmount.getText().toString().trim();

                try {
                    installmentAmount = Integer.parseInt(strInstallmentAmount);
                    fetchedLoanoanAmount = Integer.parseInt(loanAmount);

                    remainingLoanAmount = fetchedLoanoanAmount- installmentAmount;
                    if(remainingLoanAmount <= 0) {isFullyPaid = "1";}
                    submitInstallmentAmount(loanApplicationId,strInstallmentAmount,remainingLoanAmount,isFullyPaid);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

            }
        });
        popDisplayInstallmentForm(viewInstallmentPop);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popDisplayInstallmentForm(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(true);
        installmentDialog = builder.create();
        installmentDialog.setCancelable(true);
        installmentDialog.setCanceledOnTouchOutside(true);
        installmentDialog.show();

    }



    private void submitInstallmentAmount(final String loanApplicationId, final String strInstallmentAmount, final int remainingLoanAmount, final String isFullyPaid){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.submit_loan_installment, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject dataObject = object.getJSONObject("results");
                    Boolean isSubmissionSuccessful = dataObject.getBoolean("success");

                    if(isSubmissionSuccessful) {
                        deductLoanRepaymentInstallment(loanApplicationId,remainingLoanAmount,isFullyPaid);
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
                params.put("LoanApplicationId",loanApplicationId);
                params.put("InstallmentAmount",strInstallmentAmount);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }




    private void deductLoanRepaymentInstallment(final String loanApplicationId, final int remainingLoanAmount, final String isFullyPaid){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.deduct_paid_installment, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject dataObject = object.getJSONObject("results");
                    Boolean isSubmissionSuccessful = dataObject.getBoolean("success");

                    if(isSubmissionSuccessful) {
                        Toast.makeText(context, "Loan installment submitted successfully", Toast.LENGTH_LONG).show();
                        installmentDialog.cancel();
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
                params.put("ColumnName","LoanApplicationId");
                params.put("ColumnValue",loanApplicationId);
                params.put("IsFullyPaid",isFullyPaid);
                params.put("RemainingLoanAmount",String.valueOf(remainingLoanAmount));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    public void populateStarRatingDialog(final View v, Context c)
    {
        final ListView listview = (ListView) v.findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<String>();


        list.add("1");
        list.add("1.5");
        list.add("2");
        list.add("2.5");
        list.add("3");
        list.add("3.5");
        list.add("4");
        list.add("4.5");
        list.add("5");

        final StableArrayAdapter adapter = new StableArrayAdapter(c,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                String val= ((TextView) view).getText().toString();
                Resources res = context.getResources();


                if(val.equals("1")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("1.5")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("2")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("2.5")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("3")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("3.5")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("4")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("4.5")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                } else if(val.equals("5")) {
                    imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                    checkExistingLoanBeforeLoanRating(val);
                    ratingDialog.cancel();
                }


            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popStartRatingDialog(View v,Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        populateStarRatingDialog(v,c);
        builder.setView(v);
        builder.setCancelable(true);
        builder.setTitle("Star Rating");
        //editText.setText("test label");
        ratingDialog = builder.create();
        ratingDialog.show();


    }


    private void updateLoanRating(final String loanApplicationId, final String starRating){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.update_loan_rating, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject dataObject = object.getJSONObject("results");
                    Boolean isSubmissionSuccessful = dataObject.getBoolean("success");

                    if(isSubmissionSuccessful) {
                        Toast.makeText(context, starRating + " star rating", Toast.LENGTH_SHORT).show();
                        ratingDialog.cancel();
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
                params.put("ColumnName","LoanApplicationId");
                params.put("ColumnValue",loanApplicationId);
                params.put("LoanRating",starRating);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void checkExistingLoanBeforeLoanRating(final String starRating){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.pending_loan_with_current_company, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        Toast.makeText(context,"This client has no pending loan with us", Toast.LENGTH_LONG).show();
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(0);


                            String LoanApplicationId=obj.getString("LoanApplicationId");
                            updateLoanRating(LoanApplicationId,starRating);


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
                params.put("clientId",clientObject.getByClientId());
                params.put("companyId",pref.getString("CompanyId", null));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    private void calculateAverageLoanRating(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_specific_loan_application, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        Toast.makeText(context,"This client has no pending loan with us", Toast.LENGTH_LONG).show();
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index

                        Float averageRating = Float.valueOf(0);
                        for(int i = 0; i<jsonarray.length(); i++) {

                            JSONObject obj = null;
                            try {

                                obj = jsonarray.getJSONObject(i);


                                String LoanRating = obj.getString("LoanRating");
                                Float flLoanRating = Float.parseFloat(LoanRating);
                                averageRating = averageRating + flLoanRating;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        Resources res = context.getResources();
                        if(averageRating >= 1 && averageRating < 1.5) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating > 0 && averageRating < 1) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating <= 0) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 1.5 && averageRating < 2) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 2 && averageRating < 2.5) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 2.5 && averageRating < 3) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 3 && averageRating < 3.5) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 3.5 && averageRating < 4) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 4 && averageRating < 4.5) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.empty_star));
                        } else if(averageRating >= 4.5 && averageRating < 5) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.half_star));
                        } else if(averageRating == 5) {
                            imvFirstStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvSecondStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvThirdStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFourthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
                            imvFifthStar.setImageDrawable(res.getDrawable(R.mipmap.full_star));
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
                params.put("column_name","clientId");
                params.put("search_value",clientObject.getByClientId());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void checkLoanStatusBeforeBlackListing(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.pending_loan_with_current_company, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonarray= object.getJSONArray("results");

                    if(jsonarray.length() == 0) {
                        Toast.makeText(context,"This client has no pending loan with us", Toast.LENGTH_LONG).show();
                    } else if(jsonarray.length() > 0) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {

                            obj = jsonarray.getJSONObject(0);


                            String LoanApplicationId=obj.getString("LoanApplicationId");
                            prepareBlackListPop(LoanApplicationId);


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
                params.put("clientId",clientObject.getByClientId());
                params.put("companyId",pref.getString("CompanyId", null));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    public void prepareBlackListPop(final String loanApplicationId) {
        View viewBlackListPop = infLoanApplication.inflate(R.layout.blacklist_pop,null);
        Button btnSubmit = (Button) viewBlackListPop.findViewById(R.id.btCancel);
        Button btnCancel = (Button) viewBlackListPop.findViewById(R.id.btSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                blackListDialog.cancel();
            }
        });

        popDisplayBlackListDialog(viewBlackListPop);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popDisplayBlackListDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(true);
        blackListDialog = builder.create();
        blackListDialog.setCancelable(true);
        blackListDialog.setCanceledOnTouchOutside(true);
        blackListDialog.show();

    }
}
