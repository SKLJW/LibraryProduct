package com.sk.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 调用系统
 * author :Created by sk.
 * date : 2016/11/8.
 * email: wangchao@lepu.cn
 */

public class SystemUtil {


    /**
     * 拨打电话
     *
     * @param context
     * @param tel
     */
    public static void callPhone(Context context, String tel) {
        if (isPad(context))
            return;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + tel);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 关闭软件盘
     *
     * @param context
     * @param target
     */
    public static void closeSoftware(Context context, View target) {
        if (target != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }


    /**
     * 关闭软件盘
     *
     * @param aty
     */
    public static void closeSoftware(Activity aty) {
        View target = aty.getWindow().peekDecorView();
        if (target != null) {
            InputMethodManager inputmanger = (InputMethodManager) aty.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }

}
