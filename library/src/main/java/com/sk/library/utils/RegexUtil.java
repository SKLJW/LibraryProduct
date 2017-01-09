package com.sk.library.utils;

import android.text.TextUtils;

/**
 * 验证
 * author :Created by sk.
 * date : 2016/11/14.
 * email: wangchao@lepu.cn
 */

public class RegexUtil {

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String mobiles) {
        String telRegex = "^1[2-9]\\d{9}$";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
}
