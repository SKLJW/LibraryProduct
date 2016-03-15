package com.sk.library.http.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.sk.library.http.HttpListener;
import com.sk.library.http.IHttpHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sk on 16/2/23.
 * volley menager
 */
public class VolleyHttpManager implements IHttpHelper {

    private Context mContext;
    private HttpListener mListener;
    private RequestQueue mQueue;

    public VolleyHttpManager(Context mContext, HttpListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        mQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    public void doGet(String baseUrl, final Map<String, String> params) {
        String url = null;
        if (params == null)
            url = baseUrl;
        else
            url = getUrl(baseUrl, params);
        Log.d("volley__get__url", url);
        VolleyRequest volleyReq = new VolleyRequest(Request.Method.GET, url, new SuccessListener(), new FailureListener());
        volleyReq.setShouldCache(true);
        volleyReq.shouldCache();
        mQueue.add(volleyReq);
    }

    @Override
    public void doPost(String baseUrl, final Map<String, String> map) {
        String url = getUrl(baseUrl, map);
        Log.d("volley__post__url", url);
        VolleyRequest volleyReq = new VolleyRequest(Request.Method.POST, baseUrl, new SuccessListener(), new FailureListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.putAll(map);
                return params;
            }
        };
        volleyReq.setShouldCache(true);
        volleyReq.shouldCache();
        mQueue.add(volleyReq);
    }

    @Override
    public void doPostFile(String baseUrl, String filePath) {

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
     * 接口请求成功
     */
    class SuccessListener implements Response.Listener<String> {

        @Override
        public void onResponse(String s) {
            Log.d("http", "success");
            mListener.onHttpSuccess(s);
        }

    }


    /**
     * 接口请求失败返回
     */
    class FailureListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d("http", "fails");
            mListener.onHttpFailure(volleyError);
        }
    }
}
