package com.sk.library.base;

import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by sk on 16/2/15.
 */
public abstract class BaseActivity extends BaseAbsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.contentViewID());
        ButterKnife.bind(this);
        initView();
        initialize();
    }

    /**
     * Activity 关联布局文件
     *
     * @return
     */
    protected abstract int contentViewID();

    /**
     * 初始化所有view
     */
    protected abstract void initView();

    /**
     * 对象初始化，方法调用
     */
    protected abstract void initialize();
}
