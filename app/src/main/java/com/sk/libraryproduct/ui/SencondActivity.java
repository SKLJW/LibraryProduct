package com.sk.libraryproduct.ui;

import android.util.Log;
import android.widget.TextView;

import com.sk.library.Bean.ErrorBean;
import com.sk.library.base.BaseActivity;
import com.sk.library.base.BaseMessageEvent;
import com.sk.libraryproduct.Presenter.IMvpPresenter;
import com.sk.libraryproduct.Presenter.MvpPresenterImpl;
import com.sk.libraryproduct.R;
import com.sk.libraryproduct.view.IMvpView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class SencondActivity extends BaseActivity  implements IMvpView {

    IMvpPresenter iMvpPresenter;
    @Bind(R.id.tv_content)
    TextView textView;

    @Override
    protected int contentViewID() {
        return R.layout.activity_sencond;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initialize() {
        iMvpPresenter = new MvpPresenterImpl(this, this);
        iMvpPresenter.getData();
    }

    @OnClick(R.id.btnSend)
    void postEventMsg() {
        Log.d("tag", "Hello,send is success!");
        EventBus.getDefault().post(new BaseMessageEvent<String>("Hello,send is success!"));
    }


    @Override
    public void getData(String data) {

    }

    @Override
    public void onError(ErrorBean errorBean) {
        textView.setText(errorBean.getMsg());
    }

    @Override
    public void onFialure(String errorMsg) {

    }
}
