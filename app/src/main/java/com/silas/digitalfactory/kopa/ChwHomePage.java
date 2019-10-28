package com.silas.digitalfactory.kopa;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChwHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
  String mDate,strDOB,strGender,strFirstName,strMiddleName,strSurname,strPhoneNumber,strEmail,strPhysicalAddress,strNatId;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  AlertDialog alertDialog,myAlertDialog,basicAlertDialog,alertDialog2,empDetailsDialog,loanAppDialog;
  LayoutInflater inflater;
  DatabaseHelper myDb;
  public ImageView imgUploadPreview;
  ImageView imgCalender,imgProfPic,imgNatIdPic;
  private int mYear, mMonth, mDay, mHour, mMinute;
  public int Year,Month,Day,Hour,Minute,Seconds;
  Button btnNext;
  View b;
  String loanExpectedReturnDate, strClientUUID;
  String path;
  private Bitmap bitmap;
  private Uri filePath;
  public Button btSubmitNothing,btCancelNothing;
  private int PICK_IMAGE_REQUEST = 1;
  ArrayList<MyBasket> list;
  ArrayList<EmploymentCategoriesModel> employment_categories_list;
  ListView listview;
  SharedPreferences pref;
  SharedPreferences.Editor editor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chw_home_page);
    myDb = new DatabaseHelper(this);
    inflater=this.getLayoutInflater();
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    setupViewPager(viewPager);

    tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    editor = pref.edit();
    employment_categories_list = new ArrayList<>();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        prepPersonalInfo();
        // getAll();
      }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);


    fetchEmploymentCategories();
  }


  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new FragmentOne(), "CLIENTS");
    adapter.addFragment(new FragmentTwo(), "STATISTICS");
    adapter.addFragment(new FragmentThree(), "BACK REFERRALS");
    viewPager.setAdapter(adapter);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.chw_home_page, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      Intent intent = new Intent(
              getBaseContext(),SearchActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }


  class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
      return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mFragmentTitleList.get(position);
    }
  }


  private void showFileChooser() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
      filePath = data.getData();
      try {
        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
        imgUploadPreview.setImageBitmap(bitmap);
        previewUpload();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void previewUpload() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(b);
    builder.setCancelable(true);
    alertDialog = builder.create();
    alertDialog.show();
  }


  public void prepPersonalInfo()
  {
    View v= inflater.inflate(R.layout.personal_pop, null);
    imgCalender=(ImageView) v.findViewById(R.id.calender);
    imgProfPic=(ImageView) v.findViewById(R.id.personal_photo);

    final EditText etFirstName=(EditText)v.findViewById(R.id.et_first_name);
    final EditText etMiddleName=(EditText)v.findViewById(R.id.et_middle_name);
    final EditText etSurname=(EditText)v.findViewById(R.id.et_surname);
    final EditText etPhoneNumber=(EditText)v.findViewById(R.id.et_phone_number);
    final EditText etEmail=(EditText)v.findViewById(R.id.et_email);
    final EditText etPhysicalAddress=(EditText)v.findViewById(R.id.et_physical_address);
    final EditText etNatId=(EditText)v.findViewById(R.id.et_natid);
    final CheckBox cb_male =(CheckBox)v.findViewById(R.id.cb_male);
    final CheckBox cb_female =(CheckBox)v.findViewById(R.id.cb_female);
    btnNext = (Button) v.findViewById(R.id.btMore);



    cb_male.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //is chkIos checked?
        if (((CheckBox) v).isChecked()) {
          cb_female.toggle();
          strGender="1";
        }

      }
    });


    cb_female.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //is chkIos checked?
        if (((CheckBox) v).isChecked()) {
          cb_male.toggle();
          strGender="2";
        }

      }
    });



    imgCalender.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onClick(View view) {
        //alertDialog.cancel();
        getDate();


      }
    });

    imgProfPic.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onClick(View view) {
        b= inflater.inflate(R.layout.upload_preview, null);
        imgUploadPreview = (ImageView) b.findViewById(R.id.img_upload_preview);
        btSubmitNothing = (Button) b.findViewById(R.id.btn_submit_nothing);
        btCancelNothing = (Button) b.findViewById(R.id.btn_cancel_nothing);
        btCancelNothing.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            alertDialog.cancel();
          }
        });

        btSubmitNothing.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            alertDialog.cancel();
          }
        });

        showFileChooser();


      }
    });


    btnNext.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onClick(View view) {

        strFirstName=etFirstName.getText().toString();
        strMiddleName=etMiddleName.getText().toString();
        strSurname=etSurname.getText().toString();
        strPhoneNumber=etPhoneNumber.getText().toString();
        strEmail=etEmail.getText().toString();
        strPhysicalAddress=etPhysicalAddress.getText().toString();
        strNatId=etNatId.getText().toString();
        strClientUUID = UUID.randomUUID().toString();

        int permissionCheck = ContextCompat.checkSelfPermission(ChwHomePage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(ChwHomePage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        } else {
          uploadMultipart(strFirstName,strMiddleName,strSurname,strPhoneNumber,strEmail,strPhysicalAddress,strNatId,strGender,strDOB);
          basicAlertDialog.cancel();
          prepEmploymentDetails();
        }

      }
    });
    personalInfoPop(v);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void personalInfoPop(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(v);
    builder.setCancelable(true);
    basicAlertDialog = builder.create();
    basicAlertDialog.setCancelable(true);
    basicAlertDialog.setCanceledOnTouchOutside(true);
    basicAlertDialog.show();

  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void getDate()
  {
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);


    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

              @Override
              public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {

                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                //txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                Year=year;
                Month=(monthOfYear + 1);
                Day=dayOfMonth;

                strDOB =(Year + "-" + Month + "-" + Day);

              }
            }, mYear, mMonth, mDay);
    datePickerDialog.show();



  }


  public void prepEmploymentDetails()
  {

    View v= inflater.inflate(R.layout.employment_details_pop, null);

    ImageView imgJob=(ImageView) v.findViewById(R.id.personal_photo);
      final EditText etOccupation=(EditText)v.findViewById(R.id.et_occupation);
      final EditText etEmploymentStation=(EditText)v.findViewById(R.id.et_station);
    Button btnNext = (Button) v.findViewById(R.id.btMore);

    imgJob.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        View p = inflater.inflate(R.layout.employment_category_listview, null);
        popPage2(p,ChwHomePage.this);
      }
    });

    btnNext.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

          String strEmploymentStatus = "1";

          String strEmploymentCategoryId = pref.getString("EmploymentCategoryId", null);

          if(strEmploymentCategoryId.equals(Config.UNEMPLOYED_STATUS)) {
              strEmploymentStatus = "0";
          }

          String strOccupation=etOccupation.getText().toString();
          String strEmploymentStation=etEmploymentStation.getText().toString();
          empDetailsDialog.cancel();
          prepLoanApplication(strEmploymentStatus,strEmploymentCategoryId,strOccupation,strEmploymentStation);
      }
    });


    employmentDetailsPop(v);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void employmentDetailsPop(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(v);
    builder.setCancelable(true);
      empDetailsDialog = builder.create();
      empDetailsDialog.setCancelable(true);
      empDetailsDialog.setCanceledOnTouchOutside(true);
      empDetailsDialog.show();

  }

  public String getPath(Uri uri) {

    if (uri!=null)
    {

      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
      cursor.moveToFirst();
      String document_id = cursor.getString(0);
      document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
      cursor.close();

      cursor = getContentResolver().query(
              android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
              null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
      cursor.moveToFirst();
      path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
      cursor.close();}
    else { Toast.makeText(getApplicationContext(),
            "You forgot to upload an image of your product!", Toast.LENGTH_LONG)
            .show();
      path="";
    }

    return path;
  }


  public void uploadMultipart(String strFirstName, String strMiddleName, String strSurname, String strPhoneNumber, String strEmail, String strPhysicalAddress, String strNatId, String strGender, String strDOB) {

    String path = getPath(filePath);

    if (path.isEmpty()){//Do nothing and wait
    }
    else {
      //Uploading code

      try {

        String uploadId = UUID.randomUUID().toString();
        //Creating a multi part request
        new MultipartUploadRequest(this, uploadId, Config.add_company_clients)
                .addFileToUpload(path, "file") //Adding file
                .addParameter("ClientFirstName",strFirstName)
                .addParameter("ClientMiddleName",strMiddleName)
                .addParameter("ClientSurname",strSurname)
                .addParameter("ClientNationalId",strNatId)
                .addParameter("GenderId",strGender)
                .addParameter("ClientDOB",strDOB)
                .addParameter("ClientPhoneNumber",strPhoneNumber)
                .addParameter("ClientPhysicalAddress",strPhysicalAddress)
                .addParameter("ClientEmail",strEmail)
                .addParameter("ClientUniqueId",strClientUUID)
                .setNotificationConfig(new UploadNotificationConfig())
                .setMaxRetries(2)
                .startUpload(); //Starting the upload


      } catch (Exception exc) {
        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
      }}

  }


  public void fetchEmploymentCategories(){
    StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_my_employment_categories, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        //Displaying our grid
        try {
          JSONObject object = new JSONObject(s);
          JSONArray jsonarray= object.getJSONArray("results");

          employment_categories_list.add(new EmploymentCategoriesModel(Config.UNEMPLOYED_STATUS,"999","Unemployed"));

          for(int i = 0; i<jsonarray.length(); i++){

            //Creating a json object of the current index
            JSONObject obj = null;
            try {

              Toast.makeText(getApplicationContext(),
                      s, Toast.LENGTH_LONG)
                      .show();

              obj = jsonarray.getJSONObject(i);


              String EmploymentCategoryId=obj.getString("EmploymentCategoryId");
              String CompanyId=obj.getString("CompanyId");
              String CategoryDescription=obj.getString("CategoryDescription");



              employment_categories_list.add(new EmploymentCategoriesModel(EmploymentCategoryId,CompanyId,CategoryDescription));


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
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("column_name","CompanyId");
        stringMap.put("search_value",pref.getString("CompanyId", null));

        return stringMap;
      }
    };
    Volley.newRequestQueue(this).add(stringRequest);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void popPage2(View v, Context c) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);



    builder.setView(v);

    builder.setCancelable(true);
    // builder.setTitle("Submit verification code");

    //editText.setText("test label");
    alertDialog2 = builder.create();
    getAll2(v,c);
    alertDialog2.show();






  }


  public void getAll2(final View v, Context c)
  {
    final ListView listview = (ListView) v.findViewById(R.id.listview);

    MyListAdapter2 adapter = new MyListAdapter2(this, R.layout.my_custom_list, employment_categories_list,pref,alertDialog2);

    //attaching adapter to the listview
    listview.setAdapter(adapter);
  }


    public void prepLoanApplication(final String strEmploymentStatus, final String strEmploymentCategoryId, final String strOccupation, final String strEmploymentStation)
    {
        fetchClientId(strClientUUID);
        View v= inflater.inflate(R.layout.loan_application_pop, null);


        Button btnSubmit = (Button) v.findViewById(R.id.btMore);
        ImageView imgCalender = (ImageView) v.findViewById(R.id.calender);
      final EditText etLoanAmount = (EditText) v.findViewById(R.id.et_loan_amount);


      imgCalender.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
          //alertDialog.cancel();
          getExpectedLoanReturnDate();


        }
      });


      btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              String strLoanAmount = etLoanAmount.getText().toString();

              submitLoanApplication(strEmploymentStatus, strEmploymentCategoryId, strOccupation, strEmploymentStation, strLoanAmount, loanExpectedReturnDate);
              loanAppDialog.cancel();
            }
        });


        loanApplicationPop(v);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void loanApplicationPop(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setCancelable(true);
      loanAppDialog = builder.create();
      loanAppDialog.setCancelable(true);
      loanAppDialog.setCanceledOnTouchOutside(true);
      loanAppDialog.show();

    }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void getExpectedLoanReturnDate()
  {
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);


    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

              @Override
              public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {

                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                //txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                Year=year;
                Month=(monthOfYear + 1);
                Day=dayOfMonth;

                loanExpectedReturnDate =(Year + "-" + Month + "-" + Day);

              }
            }, mYear, mMonth, mDay);
    datePickerDialog.show();



  }


  private void submitLoanApplication(final String strEmploymentStatus, final String strEmploymentCategoryId, final String strOccupation, final String strEmploymentStation, final String strLoanAmount, final String loanExpectedReturnDate){


    StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.add_loan_application, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();

        //Displaying our grid
        try {
          JSONObject object = new JSONObject(s);
          JSONArray jsonarray= object.getJSONArray("results");



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
        params.put("ClientId",pref.getString("ClientId", null));
        params.put("CompanyId",pref.getString("CompanyId", null));
        params.put("CompanyBranchId",pref.getString("CompanyBranchId", null));
        params.put("SystemUserId",pref.getString("SystemUserId", null));
        params.put("LoanAmount",strLoanAmount);
        params.put("ExpectedSettlementDate",loanExpectedReturnDate);
        params.put("LoanRating","0");
        params.put("IsFullyPaid","0");
        params.put("RemainingLoanAmount",strLoanAmount);
        params.put("EmploymentStatus",strEmploymentStatus);
        params.put("EmploymentCategoryId",strEmploymentCategoryId);
        params.put("Occupation",strOccupation);
        params.put("EmploymentStation",strEmploymentStation);



        return params;
      }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    //Adding our request to the queue
    requestQueue.add(stringRequest);
  }



  private void fetchClientId(final String strClientUUID){


    StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.fetch_current_client_id, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {


          Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
        //Displaying our grid
        try {
          JSONObject object = new JSONObject(s);
          JSONArray jsonarray= object.getJSONArray("results");


            //Creating a json object of the current index
            JSONObject obj = null;
            try {
              //getting json object from current index
              obj = jsonarray.getJSONObject(0);

              String ClientId =obj.getString("ClientId");

              editor.putString("ClientId", ClientId);
              editor.commit();

            } catch (JSONException e) {
              e.printStackTrace();
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
        params.put("column_name","ClientUniqueId");
        params.put("search_value",strClientUUID);

        return params;
      }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    //Adding our request to the queue
    requestQueue.add(stringRequest);
  }


}