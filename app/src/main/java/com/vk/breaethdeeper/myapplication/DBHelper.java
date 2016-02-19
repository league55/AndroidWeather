package com.vk.breaethdeeper.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mixmax on 18.02.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    Context c;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 2);
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table countries (" +
                "name text primary key," +
                "code text" +
                ")");
        db.execSQL("create table cities (" +
                "id integer primary key," +
                "name text," +
                "country text"
                + ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /*
    }

        db.execSQL("drop table if exists countries");



    */
    }
}
