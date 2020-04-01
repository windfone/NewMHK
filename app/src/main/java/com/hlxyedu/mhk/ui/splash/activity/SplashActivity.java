package com.hlxyedu.mhk.ui.splash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.activity.LoginActivity;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.ui.splash.contract.SplashContract;
import com.hlxyedu.mhk.ui.splash.presenter.SplashPresenter;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by zhangguihua
 */
public class SplashActivity extends RootActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.splash_iv)
    ImageView splash_iv;

    @BindView(R.id.banner_guide)
    RelativeLayout banner_guide;

    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;

    @BindView(R.id.banner_guide_foreground)
    BGABanner mForegroundBanner;


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

        //第二次进入跳转
        splash_iv.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.app_start_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画完成
            @Override
            public void onAnimationEnd(Animation animation) {
                initApp(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_iv.setAnimation(animation);

//        new Handler().postDelayed(() -> {
////            startActivity(LoginActivity.newInstance(getBaseContext()));
//            if (mPresenter.isLogin()) {
//                startActivity(MainActivity.newInstance(getBaseContext()));
//            } else {
//                startActivity(LoginActivity.newInstance(getBaseContext()));
//            }
//            finish();
//
//        }, 1000);

    }

    private void initApp(Animation animation) {

//        startActivity(MainActivity.newInstance(getApplicationContext()));
//        startActivity(LoginActivity.newInstance(getApplicationContext()));

        if (mPresenter.isLogin()) {
            startActivity(MainActivity.newInstance(getApplicationContext()));
        } else {
            startActivity(LoginActivity.newInstance(getApplicationContext()));
        }
        finish();
    }

    @Override
    public void responeError(String errorMsg) {

    }
}