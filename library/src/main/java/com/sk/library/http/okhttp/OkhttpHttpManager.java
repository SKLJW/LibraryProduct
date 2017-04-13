package com.sk.library.http.okhttp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sk.library.http.HttpListener;
import com.sk.library.http.IHttpHelper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sk on 16/2/23.
 * okhttp http manager
 */
public class OkhttpHttpManager implements IHttpHelper {

    public static final MediaType FILETYPE = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final long TIMEOUT = 10;

    private OkHttpClient okHttpClient;
    private Context mContext;
    private HttpListener mListener;

    public OkhttpHttpManager(Context mContext) {
        this.mContext = mContext;
<<<<<<< HEAD
        okHttpClient = new OkHttpClient();
=======
        this.mListener = mListener;
        okHttpClient = new OkHttpClient.Builder().
                readTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT, TimeUnit.SECONDS).
                connectTimeout(TIMEOUT, TimeUnit.SECONDS).build();
>>>>>>> origin/master
    }

    public void setmListener(HttpListener httpListener) {
        this.mListener = httpListener;
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
        okHttpClient.newCall(request).enqueue(new OkhttpCallback(mListener));
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
        okHttpClient.newCall(request).enqueue(new OkhttpCallback(mListener));
    }

    @Override
    public void doPostFile(String baseUrl, String filePath) {
        Log.d("okhttp__post__file", "commit___file");
        File file = new File(filePath);
        Request request = new Request.Builder().url(baseUrl).post(RequestBody.create(FILETYPE, file)).build();
        okHttpClient.newCall(request).enqueue(new OkhttpCallback(mListener));
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

        HttpListener httpListener;

        OkhttpCallback(HttpListener httpListener) {
            this.httpListener = httpListener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            new ResultTask(e, httpListener).execute();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful())
                new ResultTask(response.body().string(), httpListener).execute();
            else
                new ResultTask(response, httpListener).execute();
        }
    }


    class ResultTask extends AsyncTask<Void, Void, Object> {
        Object result;
        HttpListener httpLister;

        ResultTask(Object result, HttpListener httpLister) {
            this.result = result;
            this.httpLister = httpLister;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o instanceof String)
                httpLister.onHttpSuccess((String) o);
            else if (o instanceof Call)
                httpLister.onHttpFailure(o);
            else
                httpLister.onHttpFailure(o);
        }
    }
}
