package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.EEndContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class EEndPresenter extends RxPresenter<EEndContract.View> implements EEndContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public EEndPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(EEndContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}