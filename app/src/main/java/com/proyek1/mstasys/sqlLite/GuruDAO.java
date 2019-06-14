package com.proyek1.mstasys.sqlLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GuruDAO extends DBHelper {

    public GuruDAO(Context context){
        super(context);
    }

    public List<String> getUser(){
        List<String> user = new ArrayList<>();
        String sql = "SELECT * FROM guru WHERE id='1'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        String nip;
        String password;
        String status;
        if(cursor.moveToFirst()){
            nip=cursor.getString(cursor.getColumnIndex("nip"));
            password=cursor.getString(cursor.getColumnIndex("password"));
            status=cursor.getString(cursor.getColumnIndex("status"));
            user.add(nip);
            user.add(password);
            user.add(status);
        }else{
            return null;
        }
        return user;
    }

    public void setUser(String nip, String password, String status){
        String sql="INSERT INTO guru VALUES('1', '"+ nip +"', '"+password+"', '"+status+"' );";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }

    public void ubahPassword(String password){
        String sql="UPDATE guru SET password='"+password+"' where id='1' ";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }

    public void deleteUser(){
        String sql="DELETE FROM guru WHERE id='1'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }
}
