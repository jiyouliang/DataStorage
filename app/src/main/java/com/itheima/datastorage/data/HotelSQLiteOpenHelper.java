package com.itheima.datastorage.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by youliang.ji on 2016/5/1.
 */
public class HotelSQLiteOpenHelper extends SQLiteOpenHelper {

    public HotelSQLiteOpenHelper(Context context) {
        super(context, "hotel.db", null, 1);//酒店数据
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建酒店列表数据表
        db.execSQL("CREATE TABLE hotel_list(id INTEGER PRIMARY KEY AUTOINCREMENT, pagenum INTEGER, data VARCHAR(20))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
