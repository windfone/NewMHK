package com.hlxyedu.mhk.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.di.component.ActivityComponent;
import com.hlxyedu.mhk.di.component.DaggerActivityComponent;
import com.hlxyedu.mhk.di.module.ActivityModule;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;
import com.skyworth.rxqwelibrary.base.SimpleFragmentActivity;

import javax.inject.Inject;

/**
 * Created by codeest on 2016/8/2.
 * MVP activity基类
 */
public abstract class BaseFragmentActivity<T extends BasePresenter> extends SimpleFragmentActivity implements BaseView {

    protected DialogPlus mMaterialDialog;

    @Inject
    protected T mPresenter;

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(AppContext.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
//        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
    }

    @Override
    public void stateError() {

    }

    @Override
    public void stateEmpty(String msg) {

    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateMain() {

    }

    protected abstract void initInject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackHint();
    }

    private void setBackHint() {
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        mMaterialDialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_logout))
                .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                .setContentWidth((int) (display
                        .getWidth() * 0.8))
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(true)//设置不可取消   可以取消
                .setOnClickListener((dialog, view1) -> {
                    switch (view1.getId()) {
                        case R.id.btn_neg:
                            dialog.dismiss();
                            break;
                        case R.id.btn_pos:
                            dialog.dismiss();
                            finish();
                            break;
                    }
                }).create();
        TextView textView = (TextView) mMaterialDialog.findViewById(R.id.txt_msg);
        textView.setText("退出测试将不会保留答案，是否仍要退出？");
    }

}