package com.softmed.tanzania.referral;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context mcontext;
    public static final String DATABASE_NAME="Klabu.db";
    public static final String TABLE_NAME="Users";
    public static final String COL_0="dbId";
    public static final String COL_1="UserId";
    public static final String COL_2="RoleId";
    public static final String COL_3="FirstName";
    public static final String COL_4="MiddleName";
    public static final String COL_5="SurName";
    public static final String COL_6="JobRefNo";
    public static final String COL_91="WardId";
    public static final String COL_92="WardName";
    public static final String COL_93="WardRefNo";
    public static final String COL_7="mKey";


    public static final String TABLE_VILLAGE_JURISDICTION="chw_village_jurisdiction";
    public static final String COL_8="dbId";
    public static final String COL_9="VillageId";
    public static final String COL_10="VillageName";
    public static final String COL_11="VillageRefNo";


    public static final String TABLE_FACILITY_JURISDICTION="chw_facility_jurisdiction";
    public static final String COL_12="dbId";
    public static final String COL_13="FacilityId";
    public static final String COL_14="FacilityName";
    public static final String COL_15="PhysicalAddress";
    public static final String COL_16="FacilityRefNo";


    public static final String TABLE_CREDENTIALS="credentials";
    public static final String COL_17="dbId";
    public static final String COL_18="JobRefNo";
    public static final String COL_19="Password";
    public static final String COL_20="CredRowKey";

    public static final String TABLE_MY_FACILITY="my_facility";
    public static final String COL_21="dbId";
    public static final String COL_22="FacilityId";
    public static final String COL_23="FacilityName";
    public static final String COL_24="PhysicalAddress";
    public static final String COL_25="FacilityRefNo";
    public static final String COL_26="FacRowKey";




    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
        this.mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,UserId VARCHAR(200),RoleId VARCHAR(200),FirstName VARCHAR(200),MiddleName VARCHAR(200),SurName VARCHAR(200),JobRefNo VARCHAR(200),WardId VARCHAR(200),WardName VARCHAR(200),WardRefNo VARCHAR(200),mKey VARCHAR(200))");
        db.execSQL("CREATE TABLE " + TABLE_VILLAGE_JURISDICTION + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,VillageId VARCHAR(200),VillageName VARCHAR(200),VillageRefNo VARCHAR(200))");
        db.execSQL("CREATE TABLE " + TABLE_FACILITY_JURISDICTION + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,FacilityId VARCHAR(200),FacilityName VARCHAR(200),PhysicalAddress VARCHAR(500),FacilityRefNo VARCHAR(200))");
        db.execSQL("CREATE TABLE " + TABLE_CREDENTIALS + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,JobRefNo VARCHAR(200),Password VARCHAR(200),CredRowKey VARCHAR(500))");
        db.execSQL("CREATE TABLE " + TABLE_MY_FACILITY + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,FacilityId VARCHAR(200),FacilityName VARCHAR(200),PhysicalAddress VARCHAR(500),FacilityRefNo VARCHAR(200),FacRowKey VARCHAR(500))");

        boolean cred_success=insertCredentials("8032","8032","row",db);
        if(cred_success==true){Toast.makeText(mcontext, "Local authentication ready", Toast.LENGTH_LONG).show();}else{Toast.makeText(mcontext, "Local authentication environment setup failed", Toast.LENGTH_LONG).show();}


        boolean fac_success=insertMyFacility("8032","8032","8032","8032","row",db);
        if(fac_success==true){Toast.makeText(mcontext, "Local authentication ready", Toast.LENGTH_LONG).show();}else{Toast.makeText(mcontext, "Local authentication environment setup failed", Toast.LENGTH_LONG).show();}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_VILLAGE_JURISDICTION);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_FACILITY_JURISDICTION);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CREDENTIALS);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_MY_FACILITY);
        onCreate(db);

    }

    public boolean insertData(String UserId,String RoleId,String FirstName,String MiddleName,String SurName,String JobRefNo,String WardId,String WardName,String WardRefNo,String mKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,UserId);
        contentValues.put(COL_2, RoleId);
        contentValues.put(COL_3, FirstName);
        contentValues.put(COL_4, MiddleName);
        contentValues.put(COL_5, SurName);
        contentValues.put(COL_6, JobRefNo);
        contentValues.put(COL_91, WardId);
        contentValues.put(COL_92, WardName);
        contentValues.put(COL_93, WardRefNo);
        contentValues.put(COL_7, mKey);

        long result=db.insert(TABLE_NAME,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }


    public boolean insertVillageJurisdiction(String VillageId,String VillageName,String VillageRefNo)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_9, VillageId);
        contentValues.put(COL_10, VillageName);
        contentValues.put(COL_11, VillageRefNo);


        long result=db.insert(TABLE_VILLAGE_JURISDICTION,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }



    public boolean insertFacilityJurisdiction(String FacilityId,String FacilityName,String PhysicalAddress,String FacilityRefNo)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_13, FacilityId);
        contentValues.put(COL_14, FacilityName);
        contentValues.put(COL_15, PhysicalAddress);
        contentValues.put(COL_16, FacilityRefNo);


        long result=db.insert(TABLE_FACILITY_JURISDICTION,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }



    public boolean insertCredentials(String JobRefNo,String Password,String CredRowKey,SQLiteDatabase db)
    {
        //SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_18, JobRefNo);
        contentValues.put(COL_19, Password);
        contentValues.put(COL_20, CredRowKey);


        long result=db.insert(TABLE_CREDENTIALS,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }


    public boolean insertMyFacility(String FacilityId,String FacilityName,String PhysicalAddress,String FacilityRefNo,String FacRowKey,SQLiteDatabase db)
    {
        //SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_22, FacilityId);
        contentValues.put(COL_23, FacilityName);
        contentValues.put(COL_24, PhysicalAddress);
        contentValues.put(COL_25, FacilityRefNo);
        contentValues.put(COL_26, FacRowKey);



        long result=db.insert(TABLE_MY_FACILITY,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }





    public Cursor getAllCredentials()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TABLE_NAME,null);
        return res;
    }

    public Integer deleteCredentials(String mKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME, " holder = ?",new String[] {mKey});
    }


    public boolean updateCredentials(String UserId,String RoleId,String FirstName,String MiddleName,String SurName,String JobRefNo,String WardId,String WardName,String WardRefNo,String mKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,UserId);
        contentValues.put(COL_2, RoleId);
        contentValues.put(COL_3, FirstName);
        contentValues.put(COL_4, MiddleName);
        contentValues.put(COL_5, SurName);
        contentValues.put(COL_6, JobRefNo);
        contentValues.put(COL_91, WardId);
        contentValues.put(COL_92, WardName);
        contentValues.put(COL_93, WardRefNo);
        contentValues.put(COL_7, mKey);


        db.update(TABLE_NAME,contentValues, "mKey = ?",new String[] {mKey});

        return true;

    }


    public Integer deleteEntireTable(String TableName)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TableName,null,null);
    }





    public boolean updateLocalCreds(String JobRefNo,String Password,String CredRowKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_18, JobRefNo);
        contentValues.put(COL_19, Password);
        contentValues.put(COL_20, CredRowKey);


        db.update(TABLE_CREDENTIALS,contentValues, "CredRowKey = ?",new String[] {CredRowKey});

        return true;

    }


    public boolean updateMyFacility(String FacilityId,String FacilityName,String PhysicalAddress,String FacilityRefNo,String FacRowKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_22, FacilityId);
        contentValues.put(COL_23, FacilityName);
        contentValues.put(COL_24, PhysicalAddress);
        contentValues.put(COL_25, FacilityRefNo);
        contentValues.put(COL_26, FacRowKey);


        db.update(TABLE_MY_FACILITY,contentValues, "FacRowKey = ?",new String[] {FacRowKey});

        return true;

    }



    public Cursor getLocalCreds(String CredRowKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TABLE_CREDENTIALS+ " WHERE " +COL_20+ " LIKE '%" +CredRowKey+ "%';",null);
        return res;
    }


    public Cursor getAllRows(String TableName)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TableName,null);
        return res;
    }

}
