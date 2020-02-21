package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.EWelcomeContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class EWelcomePresenter extends RxPresenter<EWelcomeContract.View> implements EWelcomeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public EWelcomePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(EWelcomeContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}