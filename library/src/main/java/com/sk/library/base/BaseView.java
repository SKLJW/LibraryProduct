package com.sk.library.base;

import com.sk.library.Bean.ErrorBean;

/**
 * Created by sk on 16/3/15.
 */
public interface BaseView {

    void onError(ErrorBean errorBean);

    void onFialure(String errorMsg);

}
