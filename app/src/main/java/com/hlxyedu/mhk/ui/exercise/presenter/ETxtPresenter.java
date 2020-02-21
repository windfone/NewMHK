package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.ETxtContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ETxtPresenter extends RxPresenter<ETxtContract.View> implements ETxtContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ETxtPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ETxtContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}