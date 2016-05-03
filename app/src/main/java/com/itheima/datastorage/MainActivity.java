package com.itheima.datastorage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.datastorage.hotel.HotelListActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 进入酒店列表页
     * @param v
     */
    public void hotelList(View v){
        Intent intent = new Intent(this, HotelListActivity.class);
        startActivity(intent);
    }

}
