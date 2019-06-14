package com.proyek1.mstasys.sqlLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SlideDAO extends DBHelper {

    public SlideDAO(Context context) {
        super(context);
    }

    public void ubahSlide(){
        String sql = "UPDATE slide SET slide='1' Where id='1'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

    public int getSlide(){
        String sql = "SELECT * FROM slide WHERE id='1' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int slide=0;
        if(cursor.moveToFirst()){
            slide = cursor.getInt(cursor.getColumnIndex("slide"));
        }
        return slide;
    }
}