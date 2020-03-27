package com.hlxyedu.mhk.ui.splash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;


import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.activity.LoginActivity;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.ui.splash.contract.SplashContract;
import com.hlxyedu.mhk.ui.splash.presenter.SplashPresenter;

/**
 * Created by zhangguihua
 */
public class SplashActivity extends RootActivity<SplashPresenter> implements SplashContract.View {

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //将window的背景图设置为空
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initEventAndData() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        new Handler().postDelayed(() -> {
//            startActivity(LoginActivity.newInstance(getBaseContext()));
            if (mPresenter.isLogin()) {
                startActivity(MainActivity.newInstance(getBaseContext()));
            } else {
                startActivity(LoginActivity.newInstance(getBaseContext()));
            }
            finish();

        },1000);

        /*findViewById(R.id.jump_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.newInstance(SplashActivity.this));
                finish();
            }
        });*/
    }

    @Override
    public void responeError(String errorMsg) {

    }
}