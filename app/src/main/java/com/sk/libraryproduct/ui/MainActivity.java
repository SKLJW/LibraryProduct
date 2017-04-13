package com.sk.libraryproduct.ui;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.library.Bean.ErrorBean;
import com.sk.library.base.BaseActivity;
import com.sk.library.base.BaseMessageEvent;
import com.sk.library.http.HttpFactory;
import com.sk.library.http.IHttpHelper;
import com.sk.libraryproduct.Model.IMvpModel;
import com.sk.libraryproduct.Presenter.IMvpPresenter;
import com.sk.libraryproduct.Presenter.MvpPresenterImpl;
import com.sk.libraryproduct.R;
import com.sk.libraryproduct.view.IMvpView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {


    @Override
    protected int contentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initialize() {

    }

    @OnClick(R.id.btnJump)
    void jumpSecond() {
        startIntent(SencondActivity.class);
    }

    @Override
    @Subscribe
    public void onEvent(BaseMessageEvent baseMessageEvent) {
        super.onEvent(baseMessageEvent);
        Toast.makeText(MainActivity.this, baseMessageEvent.getBean().toString(), Toast.LENGTH_SHORT).show();
    }

}
