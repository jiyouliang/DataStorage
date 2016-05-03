package com.itheima.datastorage.ui;

/**数据回调监听器
 * Created by youliang.ji on 2016/5/3.
 */
public interface OnDataResponseLisener {


    /**
     * 数据回调方法
     * @param respCode 返回码，用于区别是网络数据还是缓存数据
     * @param data 返回数据
     */
    void onResponse(int respCode, String data);
}
