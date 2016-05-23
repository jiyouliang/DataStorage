package com.itheima.datastorage.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by youliang.ji on 2016/5/23.
 */
public class HotelSQLiteOpenHelper extends SQLiteOpenHelper {
    public final static String HOTEL_TABLE = "hotel_list";

    public HotelSQLiteOpenHelper(Context context) {
        super(context, "heima.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+HOTEL_TABLE+"(id INTEGER PRIMARY KEY AUTOINCREMENT,pagenum INTEGER,data VARCHAR(20),time VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
