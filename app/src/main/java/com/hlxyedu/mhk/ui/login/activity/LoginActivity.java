package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.contract.LoginContract;
import com.hlxyedu.mhk.ui.login.presenter.LoginPresenter;

/**
 * Created by zhangguihua
 */
public class LoginActivity extends RootActivity<LoginPresenter> implements LoginContract.View {

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }
}