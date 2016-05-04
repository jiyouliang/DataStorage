package com.itheima.datastorage.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by youliang.ji on 2016/5/4.
 */
public class ToastUtil {

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
