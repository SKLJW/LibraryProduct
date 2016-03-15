package com.sk.library.http;

/**
 * Created by sk on 16/2/25.
 * handle http request result
 */
public interface HttpListener {


    /**
     * http request success
     * @param result
     */
    void onHttpSuccess(String result);


    /**
     * http request failure
     * @param result
     */
    void onHttpFailure(Object result);
}
