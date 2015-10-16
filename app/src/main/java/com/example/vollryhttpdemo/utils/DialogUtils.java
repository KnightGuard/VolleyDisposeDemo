package com.example.vollryhttpdemo.utils;


import android.content.Context;
import android.widget.Toast;

import com.example.vollryhttpdemo.custom.CustomToast;

public class DialogUtils {

    /**
     * Toast提示
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        if (context != null) {
            //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            CustomToast.showToast(context, msg, Toast.LENGTH_SHORT);
        }
    }
    /**
     * Toast提示
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, int msg) {
        if (context != null) {
            //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            CustomToast.showToast(context, msg, Toast.LENGTH_SHORT);
        }
    }

}
