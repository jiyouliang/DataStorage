package com.itheima.datastorage.net;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.itheima.datastorage.util.HotelSQLiteOpenHelper;
import com.itheima.datastorage.util.LogUtil;

import java.util.Map;

/**
 * 数据中心：获取数据，存储数据
 * Created by youliang.ji on 2016/5/23.
 */
public class DataCenter {

    private static DataCenter instance;
    private Context context;
    private final static String url = "http://192.168.0.104:8080/VolleyTest/HotelList";
    private MemoryLrucache memoryCache;
    private HotelSQLiteOpenHelper sqLiteOpenHelper;
    private String cacheData;
    private MyHandler handler;

    private DataCenter() {
    }

    public static DataCenter getInstance(Context context) {
        if (instance == null) {
            synchronized (DataCenter.class) {
                instance = new DataCenter();
                instance.context = context;
            }
        }
        return instance;
    }

    /**
     * 获取酒店列表数据
     *
     * @param reqFlag     请求标识
     * @param queue       请求队列
     * @param pagenum     页码
     * @param listener    成功回调
     * @param errListener 错误回调
     */
    public void getHotelList(final String reqFlag, RequestQueue queue, final int pagenum,
                             final Response.Listener<String> listener, Response.ErrorListener errListener) {
        if (exitInMemory(pagenum)) {
            listener.onResponse(getFromMemory(pagenum));
            return;
        } else if(exitInCache(pagenum)){
            LogUtil.d("缓冲中存在第页"+pagenum+"数据:"+cacheData);
            listener.onResponse(cacheData);
            return;
        }else {
            getFromInternet(reqFlag, queue, pagenum, listener, errListener);
        }
    }

    /**
     * 从网络获取数据
     *
     * @param reqFlag
     * @param queue
     * @param pagenum
     * @param listener
     * @param errListener
     */
    private void getFromInternet(String reqFlag, RequestQueue queue, final int pagenum, final Response.Listener<String> listener, Response.ErrorListener errListener) {
        StringRequest request = new StringRequest(url + "?pagenum=" + pagenum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("从网络获取第"+pagenum+"页数据");
                showToast("从网络获取第" + pagenum+"页数据");
                //存储到内存中
                storeToMemory(pagenum, response);
                storeToCache(pagenum, response);
                listener.onResponse(response);
            }
        }, errListener);
        request.setTag(reqFlag);
        queue.add(request);//发起网络请求
    }

    /**
     * 数据存储到内存中
     *
     * @param data
     */
    private void storeToMemory(int pagenum, String data) {
        if (memoryCache == null) {
            memoryCache = new MemoryLrucache();
        }
        LogUtil.d("将第" + pagenum + "页数据存储到内存");
        showToast("将第" + pagenum + "页数据存储到内存");
        memoryCache.put(pagenum + "", data);
        printMemoryData();
    }

    private String getFromMemory(int pagenum) {
        String data = memoryCache.get(pagenum + "");
        LogUtil.d("从内存获取第" + pagenum + "页数据:" + data);
        showToast("从内存获取第" + pagenum + "页数据");
        printMemoryData();
        return data;
    }

    private boolean exitInMemory(int pagenum) {
        if (memoryCache == null) {
            LogUtil.d("内存中不存在第" + pagenum + "页数据");
            showToast("内存中不存在第" + pagenum + "页数据");
            return false;
        }
        String data = memoryCache.get(pagenum + "");
        boolean exit = !TextUtils.isEmpty(data);
        if (exit) {
            LogUtil.d("内存中存在第" + pagenum + "页数据");
            showToast("内存中存在第" + pagenum + "页数据");
        } else {
            LogUtil.d("内存中不存在第" + pagenum + "页数据");
            showToast("内存中不存在第" + pagenum + "页数据");
        }
        return exit;
    }

    class MemoryLrucache extends LruCache<String, String> {
        private final static int MAX_SIZE = 3 * 1024;

        public MemoryLrucache() {
            super(MAX_SIZE);
        }

        @Override
        protected int sizeOf(String key, String value) {
            return value.getBytes().length;
        }
    }

    /**
     * 打印内存中的数据
     */
    private void printMemoryData() {
        Map<String, String> snapshot = memoryCache.snapshot();
        LogUtil.d("打印内存中的数据 start");
        for (Map.Entry<String, String> entry : snapshot.entrySet()) {
            LogUtil.d("key=" + entry.getKey() + ",value=" + entry.getValue());
        }
        LogUtil.d("打印内存中的数据 end");
    }

    /**
     * 存储到缓冲中
     *
     * @param pagenum
     * @param data
     */
    private void storeToCache(int pagenum, String data) {
        if (sqLiteOpenHelper == null) {
            sqLiteOpenHelper = new HotelSQLiteOpenHelper(context);
        }
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pagenum", pagenum);
        contentValues.put("data", data);
        contentValues.put("time", System.currentTimeMillis()+"");
        db.insert(HotelSQLiteOpenHelper.HOTEL_TABLE, null, contentValues);

        LogUtil.d("将第" + pagenum + "页数据存储到缓存中");
        showToast("将第" + pagenum + "页数据存储到缓存中");
        db.close();
    }

    private boolean exitInCache(int pagenum) {
        if (sqLiteOpenHelper == null) {
            sqLiteOpenHelper = new HotelSQLiteOpenHelper(context);
        }
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        boolean exitInCache = false;
        Cursor cursor = db.query(HotelSQLiteOpenHelper.HOTEL_TABLE, new String[]{"data"}, "pagenum=?", new String[]{pagenum + ""}, null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                cacheData = cursor.getString(0);
                if(!TextUtils.isEmpty(cacheData)){
                    exitInCache = true;
                    storeToMemory(pagenum, cacheData);
                }
            }
            cursor.close();
        }
        db.close();
        if(!exitInCache) {
            LogUtil.d("缓冲中不存在第页"+pagenum+"数据");
            showToast("缓冲中不存在第页"+pagenum+"数据");
        }
        return exitInCache;
    }

    private void showToast(final String msg){
        if(handler == null){
            handler = new MyHandler();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteAllData(){

        new Thread(){
            @Override
            public void run() {
                deleteMemoryCache();
                deleteCache();
                super.run();
            }
        }.start();
    }

    /**
     * 删除内存中数据
     */
    private void deleteMemoryCache(){
        LogUtil.d("删除内存中的数据");
        showToast("删除内存数据");
        memoryCache = null;
    }

    /**
     * 删除缓冲数据
     */
    private void deleteCache(){
        if (sqLiteOpenHelper == null) {
            sqLiteOpenHelper = new HotelSQLiteOpenHelper(context);
        }
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db != null){
            int delete = db.delete(HotelSQLiteOpenHelper.HOTEL_TABLE, null, null);
            LogUtil.d("删除缓冲中的数据："+delete);
            db.close();
        }
        cacheData = null;
        showToast("删除缓存数据");
    }

    class MyHandler extends Handler{}
}
