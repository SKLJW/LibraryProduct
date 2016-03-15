package com.sk.libraryproduct.Model;

import android.content.Context;

import com.sk.library.Bean.ErrorBean;
import com.sk.library.http.HttpListener;
import com.sk.library.listener.LoadDataListener;
import com.sk.libraryproduct.http.HttpHandler;
import com.sk.libraryproduct.http.HttpManager;

/**
 * Created by sk on 16/3/15.
 */
public class MvpModel implements IMvpModel {

    Context context;
    LoadDataListener<String> loadDataListener;

    public MvpModel(Context context) {
        this.context = context;
    }

    public MvpModel(Context context, LoadDataListener<String> loadDataListener) {
        this.context = context;
        this.loadDataListener = loadDataListener;
    }

    @Override
    public void getData() {
        HttpManager.getInstance(context, new HttpHandler(){

            @Override
            public void onSuccess(String result) {
                loadDataListener.onSuccess(result);
            }

            @Override
            public void onError(ErrorBean errorBean) {
                loadDataListener.onError(errorBean);
            }

            @Override
            public void onFailure(String errorMsg) {
                loadDataListener.onFailure(errorMsg);
            }
        }).getData();
    }

}
