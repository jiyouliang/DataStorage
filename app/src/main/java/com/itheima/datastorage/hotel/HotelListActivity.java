package com.itheima.datastorage.hotel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.itheima.datastorage.R;
import com.itheima.datastorage.data.DataCenter;
import com.itheima.datastorage.model.Hotel;
import com.itheima.datastorage.ui.OnDataResponseLisener;

import java.util.ArrayList;
import java.util.List;

public class HotelListActivity extends Activity implements View.OnClickListener, OnDataResponseLisener, Response.ErrorListener {

    private ListView mListView;
    private List<Hotel> hotelList;
    private HotelListAdapter mAdapter;
    private int pagenum = 1;//当前页码
    private String url;
    private String reqFlag = this.getClass().getName();//标记volley请求，当页面finish时，通过标取消请求（volley和activity什么周期绑定）
    private RequestQueue queue;
    private View listFooterView;
    private ImageView mImageLastPage;//上一页
    private ImageView mImageNextPage;//下一页
    private TextView mTvCurPage;//当前页码
    private ProgressBar mProgressBar;
    private final static int PAGE_SIZE = 10;//每页10条数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        initView();
        initData();
        setListener();
    }

    /**
     *
     */
    private void setListener() {
        mImageLastPage.setOnClickListener(this);
        mImageNextPage.setOnClickListener(this);
    }

    private void initData() {
        url = "http://192.168.0.107:8080/VolleyTest/HotelList";
        queue = Volley.newRequestQueue(this);

//        getHotelListData();

        DataCenter.getInstance(this).getHotelList(reqFlag, pagenum, queue,this, this);

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        listFooterView = View.inflate(this, R.layout.list_footer_layout, null);
        mTvCurPage = (TextView)listFooterView.findViewById(R.id.tv_cur_page);
        mImageLastPage = (ImageView)listFooterView.findViewById(R.id.iv_last_page);
        mImageNextPage = (ImageView)listFooterView.findViewById(R.id.iv_next_page);
        mTvCurPage.setText("第1页");

        hotelList = new ArrayList<Hotel>();
        mAdapter = new HotelListAdapter(this, hotelList);
        mListView.addFooterView(listFooterView);
        mListView.setAdapter(mAdapter);

    }

    /**
     * 使用Volley获取酒店列表数据
     */
    private void getHotelListData() {

        //设置请求参数:正式开发使用这种方式提交参数，但是由于本人服务器编写能力有限，所以这里使用url提交参数（很low的方式）
     /*   final Map<String, String> params = new HashMap<String, String>();
        params.put("pagenum", (pagenum++) + "");//设置当前页码*/


//        StringRequest request = new StringRequest(url + "?pagenum=" + pagenum , this,this){
         /*   *//** 设置请求参数 *//*
            @Override
            protected Map getParams() throws AuthFailureError {
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }*/

          /*  *//**
             * 设置请求头
             * @return
             * @throws AuthFailureError
             *//*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }*/
//        };

//        request.setTag(reqFlag);
//        queue = Volley.newRequestQueue(this);
//        queue.add(request);//发起网络请求
    }


    @Override
    public void onResponse(int respCode,String result) {
        showLog(result);

        List<Hotel> hotels = JSON.parseArray(result, Hotel.class);
        if(hotels != null && hotels.size() > 0){
            hotelList.clear();
            hotelList.addAll(hotels);
            mAdapter.notifyDataSetChanged();
            mTvCurPage.setText("第" + pagenum + "页");
            if(respCode == DataCenter.RESP_CODE_INTERNET){
                DataCenter.getInstance(this).saveJsonString(pagenum, result);
            }
        }
        hideProgressBar();

        if(pagenum == 1) {//第一页，不显示返回上一页按钮
            mImageLastPage.setVisibility(View.INVISIBLE);
        }else{
            mImageLastPage.setVisibility(View.VISIBLE);
        }
        if(hotelList.size() < PAGE_SIZE ) {
            mImageNextPage.setVisibility(View.INVISIBLE);
        }else{
            mImageNextPage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressBar();
        Toast.makeText(HotelListActivity.this, "网络异常，查看MyEclipse服务器是否开启或访问地址是否正确", Toast.LENGTH_SHORT).show();
        showLog(error.getMessage());
    }

    private void showLog(String msg){
        if(msg != null)  Log.d("result", msg);
    }

    @Override
    public void finish() {
        queue.cancelAll(reqFlag);//页面消失，取消发送
        DataCenter.getInstance(this).deleteAll();
        super.finish();
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_last_page://上一页
                pagenum --;
                showProgressBar();
                DataCenter.getInstance(this).getHotelList(reqFlag, pagenum, queue, this, this);
//                getHotelListData();


                break;
            case R.id.iv_next_page://下一页
                showProgressBar();
                pagenum ++;
                DataCenter.getInstance(this).getHotelList(reqFlag, pagenum, queue, this, this);
//                getHotelListData();
                break;
        }
    }
}
