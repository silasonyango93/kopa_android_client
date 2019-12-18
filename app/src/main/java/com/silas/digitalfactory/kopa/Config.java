package com.silas.digitalfactory.kopa;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import java.io.IOException;

public class Config {
    public static DatabaseHelper myDb;
    public static String UNEMPLOYED_STATUS = "1";
    public static String CREDENTIALS_ROW_KEY = "1";
    public static final String ip = "http://104.197.5.158:80/";
    public static final String login_url =ip + "System_user_login";
    public static final String add_company_clients =ip + "add_company_clients";
    public static final String get_all_company_clients =ip + "get_all_company_clients";
    public static final String get_my_employment_categories =ip + "get_specific_employment_categories";
    public static final String get_my_company_details =ip + "get_system_user_company_details";
    public static final String fetch_current_client_id =ip + "get_specific_company_clients";
    public static final String add_loan_application =ip + "add_loan_application";
    public static final String client_any_search =ip + "client_any_search";
    public static final String update_client_employment_details =ip + "update_company_clients_employment_details";
    public static final String update_individual_company_clients_encoded_image =ip + "update_individual_company_clients_encoded_image";
    public static final String pending_loan_with_current_company =ip + "pending_loan_with_current_company";
    public static final String submit_loan_installment =ip + "add_loan_repayment_installments";



    public static final String chw_jurisdiction_villages =ip + "inner_join_villages_with_chw_jurisdiction_villages";
    public static final String chw_jurisdiction_facilities =ip + "get_specific_facilities";
    public static final String client_registration =ip + "client_registration";
    public static final String get_my_clients =ip + "get_specific_client_registration";
    public static final String  get_my_facility=ip + "inner_join_facility_with_facility_staff";
    public static final String  get_all_clients=ip + "get_all_client_registration";















    public static boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showMessage(Context context,String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


//    public static String getCurrentSessionId(Context context){
//        myDb = new DatabaseHelper(context);
//        Cursor res = myDb.getCurrentSession();
//
//        while (res.moveToNext()) {
//            UserId=res.getString(1);
//
//
//        }
//
//        return UserId;
//    }



}
