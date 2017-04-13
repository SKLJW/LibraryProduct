package com.sk.libraryproduct.http;

import android.content.Context;

import com.sk.library.http.HttpFactory;
import com.sk.library.http.HttpListener;
import com.sk.library.http.IHttpHelper;
import com.sk.library.http.okhttp.OkhttpHttpManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sk on 16/3/15.
 */
public class HttpManager {

//    private IHttpHelper iHttpHelper;
    OkhttpHttpManager okhttpHttpManager;
    private static HttpManager httpManager = null;

    private HttpManager(Context context) {
//        iHttpHelper = HttpFactory.getHttpHelper(context);
        okhttpHttpManager = new OkhttpHttpManager(context);
    }

    public static HttpManager getInstance(Context context) {
        if (httpManager == null) {
            synchronized (HttpManager.class) {
                if (httpManager == null)
                    httpManager = new HttpManager(context);
            }
        }
        return httpManager;
    }

    /**
     * test mvp newwork url
     */
    public void getData(HttpListener listener) {
        Map<String, String> params = new HashMap<>();
        okhttpHttpManager.setmListener(listener);
        okhttpHttpManager.doPost("http://edian.guoanshequ.net/index.php?com=com_appService&method=createToken&device=00000000-10b6-7b97-10b6-7b9700000000&push=04ede0d2d06acd604d2db48cdeb5a035f072d31c&time=1456347952&brand=SM-G9009D&type=android&os=5.0&client=1", params);
    }
}
