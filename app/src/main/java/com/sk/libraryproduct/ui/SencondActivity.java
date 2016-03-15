package com.sk.libraryproduct.ui;

import android.util.Log;

import com.sk.library.base.BaseActivity;
import com.sk.library.base.BaseMessageEvent;
import com.sk.libraryproduct.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class SencondActivity extends BaseActivity {

    @Override
    protected int contentViewID() {
        return R.layout.activity_sencond;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initialize() {

    }

    @OnClick(R.id.btnSend)
    void postEventMsg() {
        Log.d("tag", "Hello,send is success!");
        EventBus.getDefault().post(new BaseMessageEvent("Hello,send is success!"));
    }


}
