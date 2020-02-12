package com.hlxyedu.mhk.ui.login.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.login.contract.FoundPsdContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class FoundPsdPresenter extends RxPresenter<FoundPsdContract.View> implements FoundPsdContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public FoundPsdPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(FoundPsdContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}