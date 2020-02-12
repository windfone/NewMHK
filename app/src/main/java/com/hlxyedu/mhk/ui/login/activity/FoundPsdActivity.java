package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.contract.FoundPsdContract;
import com.hlxyedu.mhk.ui.login.presenter.FoundPsdPresenter;

/**
 * Created by zhangguihua
 */
public class FoundPsdActivity extends RootActivity<FoundPsdPresenter> implements FoundPsdContract.View {

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FoundPsdActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_found_psd;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }
}