package com.sk.library.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 初始化okhttpClient
 * Created by sk on 2017/4/13.
 */

public class OkhttpUtils {

    public static final long TIMEOUT = 10;

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkhttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().
                    readTimeout(TIMEOUT, TimeUnit.SECONDS).
                    writeTimeout(TIMEOUT, TimeUnit.SECONDS).
                    connectTimeout(TIMEOUT, TimeUnit.SECONDS).build();
        }
        return okHttpClient;
    }
}
