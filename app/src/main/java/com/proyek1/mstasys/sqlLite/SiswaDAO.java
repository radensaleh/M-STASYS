package com.proyek1.mstasys.sqlLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SiswaDAO extends DBHelper{

    public SiswaDAO(Context context) {
        super(context);
    }

    public List<String> getUser(){
        List<String> user = new ArrayList<>();
        String sql = "SELECT * FROM siswa WHERE id='1'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        String nis;
        String password;
        String status;
        if(cursor.moveToFirst()){
            nis=cursor.getString(cursor.getColumnIndex("nis"));
            password=cursor.getString(cursor.getColumnIndex("password"));
            status=cursor.getString(cursor.getColumnIndex("status"));
            user.add(nis);
            user.add(password);
            user.add(status);
        }else{
            return null;
        }
        return user;
    }

    public void setUser(String nis, String password, String status){
        String sql="INSERT INTO siswa VALUES('1', '"+ nis +"', '"+password+"', '"+status+"' );";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }

    public void ubahPassword(String password){
        String sql="UPDATE siswa SET password='"+password+"' where id='1' ";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }

    public void deleteUser(){
        String sql="DELETE FROM siswa WHERE id='1'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }
}
