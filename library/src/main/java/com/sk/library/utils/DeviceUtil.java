package com.sk.library.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;



/**
 * author sk
 * data   2016/9/24
 */
public class DeviceUtil {


    /**
     * 获取手机唯一标示  设备ID--Android系统ID--UUID
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            //todo get Adroid ID  获取android系统ID
            imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(imei)) {
                //// TODO: 2016/9/24  获取随机UUID 
                imei = UUID.randomUUID().toString();
            }
        }
        return imei;
    }

    /**
     * 获取设备品牌
     *
     * @return
     */
    public static String getDeviceBrand() {
        // TODO Auto-generated method stub
        String brand = android.os.Build.MODEL;
        brand = brand.replace(" ", "");
        return brand;
    }

    /**
     * 获取设备版本
     *
     * @return
     */
    public static String getDeviceOs() {
        String os = android.os.Build.VERSION.RELEASE;
        return os;
    }
}

