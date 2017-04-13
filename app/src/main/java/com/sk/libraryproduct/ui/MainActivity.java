package com.sk.libraryproduct.ui;

import android.widget.Toast;

import com.sk.library.base.BaseActivity;
import com.sk.library.base.BaseMessageEvent;
import com.sk.libraryproduct.R;

import org.greenrobot.eventbus.Subscribe;

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
