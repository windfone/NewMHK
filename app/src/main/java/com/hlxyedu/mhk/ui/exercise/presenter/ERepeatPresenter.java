package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.ERepeatContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ERepeatPresenter extends RxPresenter<ERepeatContract.View> implements ERepeatContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ERepeatPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ERepeatContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}