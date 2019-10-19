package com.silas.digitalfactory.kopa;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChwHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
  String mDate,strDOB,strGender,strFirstName,strMiddleName,strSurname,strPhoneNumber,strEmail,strPhysicalAddress,strParent;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  AlertDialog alertDialog,myAlertDialog;
  LayoutInflater inflater;
  DatabaseHelper myDb;
  public ImageView imgUploadPreview;
  ImageView imgCalender,imgProfPic,imgNatIdPic;
  private int mYear, mMonth, mDay, mHour, mMinute;
  public int Year,Month,Day,Hour,Minute,Seconds;
  Button btnNext;
  View b;
  String path;
  private Bitmap bitmap;
  private Uri filePath;
  public Button btSubmitNothing,btCancelNothing;
  private int PICK_IMAGE_REQUEST = 1;
  ArrayList<MyBasket> list;
  ListView listview;

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
      // Handle the camera action
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
    imgNatIdPic=(ImageView) v.findViewById(R.id.nationalid_photo);

    final EditText etFirstName=(EditText)v.findViewById(R.id.et_first_name);
    final EditText etMiddleName=(EditText)v.findViewById(R.id.et_middle_name);
    final EditText etSurname=(EditText)v.findViewById(R.id.et_surname);
    final EditText etPhoneNumber=(EditText)v.findViewById(R.id.et_phone_number);
    final EditText etEmail=(EditText)v.findViewById(R.id.et_email);
    final EditText etPhysicalAddress=(EditText)v.findViewById(R.id.et_physical_address);
    final EditText etParent=(EditText)v.findViewById(R.id.et_parent);
    final CheckBox cb_male =(CheckBox)v.findViewById(R.id.cb_male);
    final CheckBox cb_female =(CheckBox)v.findViewById(R.id.cb_female);
    btnNext = (Button) v.findViewById(R.id.btMore);



    cb_male.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //is chkIos checked?
        if (((CheckBox) v).isChecked()) {
          cb_female.toggle();
          strGender="Male";
        }

      }
    });


    cb_female.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //is chkIos checked?
        if (((CheckBox) v).isChecked()) {
          cb_male.toggle();
          strGender="FeMale";
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
        alertDialog.cancel();


        strFirstName=etFirstName.getText().toString();
        strMiddleName=etMiddleName.getText().toString();
        strSurname=etSurname.getText().toString();
        strPhoneNumber=etPhoneNumber.getText().toString();
        strEmail=etEmail.getText().toString();
        strPhysicalAddress=etPhysicalAddress.getText().toString();
        strParent=etParent.getText().toString();
        prepLocation();

      }
    });
    personalInfoPop(v);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void personalInfoPop(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(v);
    builder.setCancelable(true);
    alertDialog = builder.create();
    alertDialog.setCancelable(true);
    alertDialog.setCanceledOnTouchOutside(true);
    alertDialog.show();

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


  public void prepLocation()
  {
    View v= inflater.inflate(R.layout.location_pop, null);

    listview = (ListView) v.findViewById(R.id.my_list);
     list = new ArrayList<>();
    Cursor res = myDb.getAllRows("chw_village_jurisdiction");

    if (res.getCount() == 0) {
      //Show message
      //showMessage("No PaintShares Available", "You currently have no PaintShares saved");
      return;
    }



    while (res.moveToNext()) {

      list.add(new MyBasket(res.getString(1),res.getString(2),res.getString(3)));

    }




    //attaching adapter to the listview


    btnNext = (Button) v.findViewById(R.id.btMore);




    btnNext.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onClick(View view) {
        alertDialog.cancel();
        //prepClinic();

      }
    });
    locationPop(v);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void locationPop(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(v);
    builder.setCancelable(true);
    myAlertDialog = builder.create();

    MyListAdapter adapter = new MyListAdapter(this, R.layout.my_custom_list, list,strFirstName,strMiddleName,strSurname,strPhoneNumber,strEmail,strPhysicalAddress,strParent,strGender,strDOB,myAlertDialog);
    listview.setAdapter(adapter);
    myAlertDialog.setCancelable(false);
    myAlertDialog.setCanceledOnTouchOutside(false);
    myAlertDialog.show();


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


  public void uploadMultipart() {

    String path = getPath(filePath);

    if (path.isEmpty()){//Do nothing and wait
    }
    else {
      //Uploading code
      try {
        String uploadId = UUID.randomUUID().toString();

        //Creating a multi part request
        new MultipartUploadRequest(this, uploadId, Config.upload_service)
                .addFileToUpload(path, "image") //Adding file
                .setNotificationConfig(new UploadNotificationConfig())
                .setMaxRetries(2)
                .startUpload(); //Starting the upload


      } catch (Exception exc) {
        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
      }}

    finish();
    startActivity(getIntent());

  }

}