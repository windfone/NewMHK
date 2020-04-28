package com.skyworth.rxqwelibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.skyworth.rxqwelibrary.managers.AppManagers;
import com.skyworth.rxqwelibrary.widget.NetErrorDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：skyworth on 2017/7/10 11:11
 * 邮箱：dqwei@iflytek.com
 */

public abstract class SimpleFragmentActivity extends SupportAutoActivity {

    protected Activity mContext;
    //退出事件
    private long exitTime = 0;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        onViewCreated();
        AppManagers.getInstance().addActivity(this);
        initEventAndData();

        // 沉浸式
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .transparentStatusBar().init();

        if (!NetworkUtils.isConnected()) {
            NetErrorDialog.getInstance().showNetErrorDialog(this);
        }

        // 禁止截屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
    }

    protected void onViewCreated() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManagers.getInstance().killActivity(this);
        mUnBinder.unbind();
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();

    //-------------------------------------------------------------------//
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManagers.getInstance().exit(this);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
