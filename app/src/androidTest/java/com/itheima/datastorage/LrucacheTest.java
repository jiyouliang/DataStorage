package com.itheima.datastorage;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.LruCache;

import com.itheima.datastorage.util.HotelSQLiteOpenHelper;
import com.itheima.datastorage.util.LogUtil;

import java.util.Map;

/**
 * Created by youliang.ji on 2016/5/24.
 */
public class LrucacheTest extends ApplicationTest {

    private LruCache<String, String> lruCache;

    public void test(){
        if(lruCache == null){
            lruCache = new LruCache<String, String>(9){
                @Override
                protected int sizeOf(String key, String value) {
                    return value.getBytes().length;
                }
            };
        }

        lruCache.put("1", "AAA");
        lruCache.put("2", "BBB");
        print();
        lruCache.put("3", "CCC");
        print();
        lruCache.put("4", "DDD");
        print();
        String value = lruCache.get("2");
        LogUtil.d("key=2,value="+value);
        print();
        String value4 = lruCache.get("4");
        LogUtil.d("key=4,value=" + value4);
        print();
        lruCache.put("5","EEE");
        print();


    }

    private void print(){
        LogUtil.d("==========打印开始==========");
        for(Map.Entry<String, String> entry : lruCache.snapshot().entrySet()){
            LogUtil.d("key="+entry.getKey()+",value="+entry.getValue());
        }
        LogUtil.d("==========打印结束==========");
    }

    public void test2(){
        String value1 = "1464054613534";
        String value2 = "1464054629950";


    }

    /**
     *测试数据库排序
     */
    public void test4(){
        HotelSQLiteOpenHelper sqLiteOpenHelper = new HotelSQLiteOpenHelper(getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db != null){
            Cursor cursor = db.query(HotelSQLiteOpenHelper.HOTEL_TABLE, new String[]{"pagenum"}, null, null, null, null, "time ASC");
            if(cursor != null){
                if (cursor.moveToFirst()){
                    int pagenum = cursor.getInt(0);
                    LogUtil.d("pagenum="+pagenum);
                }
                cursor.close();
            }
            db.close();
        }

    }
}
