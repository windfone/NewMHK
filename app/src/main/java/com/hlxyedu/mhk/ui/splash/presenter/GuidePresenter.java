package com.hlxyedu.mhk.ui.splash.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.splash.contract.GuideContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class GuidePresenter extends RxPresenter<GuideContract.View> implements GuideContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public GuidePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(GuideContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }

    @Override
    public void setIsFirst(boolean b) {
        mDataManager.setIsFrist(b);
    }


}