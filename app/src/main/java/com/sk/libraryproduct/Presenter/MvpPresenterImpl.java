package com.sk.libraryproduct.Presenter;

import android.content.Context;

import com.sk.library.Bean.ErrorBean;
import com.sk.library.listener.LoadDataListener;
import com.sk.libraryproduct.Model.IMvpModel;
import com.sk.libraryproduct.Model.MvpModel;
import com.sk.libraryproduct.view.IMvpView;

/**
 * Created by sk on 16/3/15.
 */
public class MvpPresenterImpl implements IMvpPresenter {

    private IMvpModel iMvpModel;
    private IMvpView iMvpView;

    public MvpPresenterImpl(Context context, IMvpView iMvpView) {
        this.iMvpView = iMvpView;
        iMvpModel = new MvpModel(context, new MvpListener());
    }

    @Override
    public void getData() {
        iMvpModel.getData();
    }

    class MvpListener implements LoadDataListener<String> {

        @Override
        public void onSuccess(String data) {
            iMvpView.getData(data);
        }

        @Override
        public void onError(ErrorBean errorBean) {
            iMvpView.onError(errorBean);
        }

        @Override
        public void onFailure(String msg) {
            iMvpView.onFialure(msg);
        }
    }
}
