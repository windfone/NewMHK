package com.hlxyedu.mhk.ui.splash.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.splash.contract.SplashContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class SplashPresenter extends RxPresenter<SplashContract.View> implements SplashContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SplashPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    @Override
    public boolean isLogin() {
        return mDataManager.getLoginStatus();
    }

    @Override
    public boolean isFirst() {
        boolean b = mDataManager.getIsFrist();
        return b;
    }

    private void registerEvent() {

    }

}