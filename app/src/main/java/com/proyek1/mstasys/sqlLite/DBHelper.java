package com.proyek1.mstasys.sqlLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "m-stasys";
    protected static final String TABLE_NAME = "slide";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //context.deleteDatabase(DATABASE_NAME); //untuk delete database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS 'slide' ('id' VARCHAR(2) NOT NULL PRIMARY KEY, 'slide' BOOLEAN NOT NULL); ";
        db.execSQL(sql);

        String siswa = "CREATE TABLE IF NOT EXISTS siswa (id VARCHAR(10) PRIMARY KEY, nis VARCHAR(50) NOT NULL," +
                "password VARCHAR(50) NOT NULL, status VARCHAR(3) NOT NULL );";
        db.execSQL(siswa);

        String guru = "CREATE TABLE IF NOT EXISTS guru (id VARCHAR(10) PRIMARY KEY, nip VARCHAR(50) NOT NULL," +
                "password VARCHAR(50) NOT NULL, status VARCHAR(3) NOT NULL );";
        db.execSQL(guru);

        sql = " INSERT INTO 'slide' VALUES ('1', 'false') ";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + "";
        db.execSQL(sql);
        onCreate(db);
    }
}
