package com.sk.library.listener;

import com.sk.library.Bean.ErrorBean;

/**
 * Created by sk on 16/3/15.
 * model return request date to presenter
 */
public interface LoadDataListener<T> {

    /**
     * url net is not block, and data is right
     *
     * @param data
     */
    void onSuccess(T data);

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
