package com.sk.library.http;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 初始化retrofit
 * Created by sk on 2017/4/13.
 */

public abstract class RetrofitUtils {

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    public static Retrofit getRetrofit(String url) {
        if (mRetrofit == null) {
            if (mOkHttpClient == null)
                mOkHttpClient = OkhttpUtils.getOkhttpClient();

            mRetrofit = new Retrofit.Builder().baseUrl(url).
                    addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                    client(mOkHttpClient).
                    build();
        }
        return mRetrofit;
    }
}
