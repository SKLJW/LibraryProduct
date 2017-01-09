package com.sk.library.listener;


import com.sk.library.Bean.ErrorBean;

/**
 * model return request date to presenter
 * author :Created by sk.
 * date : 2016/9/27.
 */

public interface MulDataListener<T, K> {

    /**
     * url net is not block, and data is right
     *
     * @param data
     */
    void onSuccess(T data, K k);

    /**
     * url net is not block, but data is error
     *
     * @param errorBean
     */
    void onError(ErrorBean errorBean);

    /**
     * url net is block
     *
     * @param msg
     */
    void onFailure(String msg);
}
