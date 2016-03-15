package com.sk.libraryproduct.http;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.sk.library.Bean.ErrorBean;
import com.sk.library.http.HttpListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by sk on 16/2/25.
 * parser json or xml for result of http requestion
 */
public abstract class HttpHandler implements HttpListener {


    @Override
    public void onHttpSuccess(String result) {
        if (isDataValid(result))
            onSuccess(result);
        else {
            try {
                JSONObject jsonObj = new JSONObject(result);
                if (jsonObj.has("status")) {
                    onError(new ErrorBean(jsonObj));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onHttpFailure(Object result) {
        String errorMsg = null;
        if (result instanceof Call) {
            //okHttp http request

        } else if (result instanceof VolleyError) {
            //volley
            if (result instanceof TimeoutError)
                errorMsg = "time out";
            else if (((VolleyError) result).networkResponse != null) {
                int serverCode = ((VolleyError) result).networkResponse.statusCode;
                switch (serverCode) {

                }
            }
        }
        onFailure(errorMsg);
    }

    /**
     * 判断json是否有效
     *
     * @param json
     * @return
     */
    private boolean isDataValid(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            if (jsonObj.has("status")) {
                String status = jsonObj.getString("status");
                if (status != null && status.equals("1"))
                    return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract void onSuccess(String result);

    public abstract void onError(ErrorBean errorBean);

    public abstract void onFailure(String errorMsg);
}
