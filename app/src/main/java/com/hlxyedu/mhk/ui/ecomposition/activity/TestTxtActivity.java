package com.hlxyedu.mhk.ui.ecomposition.activity;

import android.content.Context;
import android.content.Intent;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.ecomposition.contract.TestTxtContract;
import com.hlxyedu.mhk.ui.ecomposition.presenter.TestTxtPresenter;

/**
 * Created by zhangguihua
 * 作文 练习
 */
public class TestTxtActivity extends RootActivity<TestTxtPresenter> implements TestTxtContract.View {

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TestTxtActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test_txt;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }
}