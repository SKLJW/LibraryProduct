package com.sk.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by sk on 16/2/16.
 */
public class BaseAbsActivity extends Activity {

    private Intent mIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getExtraEvent(extras);
        }

        EventBus.getDefault().register(this);
    }


    /**
     * 获取页面传值
     */
    protected void getExtraEvent(Bundle extras) {
    }

    /**
     * 获取EventBus发送的信息
     *
     * @param baseMessageEvent
     */
    @Subscribe
    public void onEvent(BaseMessageEvent baseMessageEvent) {
    }


    /**
     * 启动页面跳转
     *
     * @param cls
     */
    public void startIntent(Class<?> cls) {
        this.startIntent(this, cls);
    }

    /**
     * 启动页面跳转
     *
     * @param cls
     */
    public void startIntent(Class<?> cls, Bundle bundle) {
        this.startIntent(this, cls, bundle);
    }

    /**
     * 启动页面跳转
     *
     * @param context
     * @param cls
     */
    public void startIntent(Context context, Class<?> cls) {
        this.startIntent(context, cls, null);
    }

    /**
     * 启动页面跳转
     *
     * @param context
     * @param cls
     * @param bundle
     */
    public void startIntent(Context context, Class<?> cls, Bundle bundle) {
        mIntent = new Intent(context, cls);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        context.startActivity(mIntent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
