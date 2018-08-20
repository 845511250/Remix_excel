package com.example.zuoyun.remix_excel.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

/**
 * Created by zuoyun on 2016/8/17.
 */
public class DbAction {
    private static DbAction dbAction=null;
    SQLiteDatabase writeDb,readDb;
    SqliteHelper helper;
    ArrayList<Integer> id=new ArrayList<>();

    private DbAction(Context context){
        helper = new SqliteHelper(context);
    }

    public static DbAction getinstance(Context context){
        if(dbAction==null)
            dbAction = new DbAction(context);
        return dbAction;
    }

    private class SqliteHelper extends SQLiteOpenHelper {
        private SqliteHelper(Context context) {
            super(context.getApplicationContext(), "FORU_DB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "create table tb_address(_id integer primary key autoincrement,fname text,lname text,phone text,country text,province text,city text,detailaddr text,postcode text)";
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }

    public void insertAddress(String fname, String lname, String phone, String country, String province, String city, String detailaddr, String postcode){
        ContentValues values = new ContentValues();
        values.put("fname",fname);
        values.put("lname",lname);
        values.put("phone",phone);
        values.put("country",country);
        values.put("province",province);
        values.put("city",city);
        values.put("detailaddr",detailaddr);
        values.put("postcode",postcode);

        writeDb = helper.getWritableDatabase();
        writeDb.insert("tb_address", null, values);
        writeDb.close();
        refreshid();
    }

    public void deleteAddress(int position){
        writeDb = helper.getWritableDatabase();
        writeDb.delete("tb_address", "_id=" + id.get(position),null);
        id.remove(position);
        writeDb.close();
    }

    public void updateAddress(int position, String fname, String lname, String phone, String country, String province, String city, String detailaddr, String postcode){
        ContentValues values = new ContentValues();
        values.put("fname",fname);
        values.put("lname",lname);
        values.put("phone",phone);
        values.put("country",country);
        values.put("province",province);
        values.put("city",city);
        values.put("detailaddr",detailaddr);
        values.put("postcode",postcode);

        writeDb = helper.getWritableDatabase();
        writeDb.update("tb_address", values, "_id=" + id.get(position), null);
        writeDb.close();
    }
//    public ArrayList<Address> getAddress(){
//        id.clear();//刷新计数器
//        ArrayList<Address> addresses=null;
//        Address address;
//        String fname,lname,phone,country,province,city,detailaddr,postcode;
//        readDb = helper.getReadableDatabase();
//        Cursor cursor = readDb.query("tb_address", null, null, null, null, null, null);
//        if(cursor!=null){
//            addresses = new ArrayList<>();
//            while (cursor.moveToNext()){
//                fname=cursor.getString(1);
//                lname=cursor.getString(2);
//                phone=cursor.getString(3);
//                country=cursor.getString(4);
//                province=cursor.getString(5);
//                city=cursor.getString(6);
//                detailaddr=cursor.getString(7);
//                postcode=cursor.getString(8);
//                address = new Address(fname, lname, phone, country, province, city, detailaddr, postcode);
//                addresses.add(address);
//
//                id.add(cursor.getInt(0));
//            }
//        }
//        readDb.close();
//        return addresses;
//    }

    private void refreshid(){
        id.clear();
        readDb = helper.getReadableDatabase();
        Cursor cursor = readDb.query("tb_address", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            id.add(cursor.getInt(0));
        }
        readDb.close();
    }


}
