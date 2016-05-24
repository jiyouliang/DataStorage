package com.itheima.datastorage;


import android.support.v4.util.LruCache;

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
}
