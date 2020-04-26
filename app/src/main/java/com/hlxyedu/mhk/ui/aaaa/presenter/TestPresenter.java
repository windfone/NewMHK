package com.hlxyedu.mhk.ui.aaaa.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.aaaa.contract.TestContract;

import javax.inject.Inject;

/**
 * Created by ceshi
 */
public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}