package com.sk.library.http.okhttp;

import android.content.Context;
import android.util.Log;

import com.sk.library.http.HttpListener;
import com.sk.library.http.IHttpHelper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by sk on 16/2/23.
 * okhttp http manager
 */
public class OkhttpHttpManager implements IHttpHelper {

    public static final MediaType FILETYPE = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private Context mContext;
    private HttpListener mListener;

    public OkhttpHttpManager(Context mContext, HttpListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void doGet(String baseUrl, Map<String, String> params) {

        String url = null;
        if (params == null)
            url = baseUrl;
        else
            url = getUrl(baseUrl, params);
        Log.d("okhttp__get__url", url);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new OkhttpCallback());
    }

    @Override
    public void doPost(String baseUrl, Map<String, String> params) {
        String url = getUrl(baseUrl, params);
        Log.d("okhttp__post__url", url);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(baseUrl).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new OkhttpCallback());
    }

    @Override
    public void doPostFile(String baseUrl, String filePath) {
        Log.d("okhttp__post__file", "commit___file");
        File file = new File(filePath);
        Request request = new Request.Builder().url(baseUrl).post(RequestBody.create(FILETYPE, file)).build();
        okHttpClient.newCall(request).enqueue(new OkhttpCallback());
    }


    /**
     * post请求，打印完整url串
     *
     * @param baseUrl
     * @param params
     */
    private String getUrl(String baseUrl, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(baseUrl);
        if (params != null && params.size() > 0) {
            builder.append("&");
            Set<String> keys = params.keySet();
            Iterator<String> iterator = keys.iterator();
            for (int i = 0; i < params.size(); i++) {
                String key = iterator.next();
                String value = params.get(key);
                if (null != value) {
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                }
                if (null != value && i < params.size() - 1) {
                    builder.append("&");
                }
            }
        }
        return builder.toString();
    }

    /**
     * okhttp http request return result
     */
    class OkhttpCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("http", "failure");
            mListener.onHttpFailure(call);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful())
                mListener.onHttpSuccess(response.body().string());
        }
    }
}
