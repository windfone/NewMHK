package com.hlxyedu.mhk.ui.mine.activity;

import android.content.Context;
import android.content.Intent;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.mine.contract.AboutUsContract;
import com.hlxyedu.mhk.ui.mine.presenter.AboutUsPresenter;

/**
 * Created by zhangguihua
 */
public class AboutUsActivity extends RootActivity<AboutUsPresenter> implements AboutUsContract.View {

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }
}