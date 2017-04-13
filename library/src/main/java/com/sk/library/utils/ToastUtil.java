package com.sk.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * toast工具类
 * 
 * @author liaoguangdong
 *
 */
public class ToastUtil {
    private ToastUtil() {
        
    }
    
    /**
     * 弹出一个toast
     * @param context
     * @param resId
     */
    public static void show(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出一个toast
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
