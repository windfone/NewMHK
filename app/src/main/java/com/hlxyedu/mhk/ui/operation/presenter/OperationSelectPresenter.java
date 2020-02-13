package com.hlxyedu.mhk.ui.operation.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.operation.contract.OperationSelectContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class OperationSelectPresenter extends RxPresenter<OperationSelectContract.View> implements OperationSelectContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public OperationSelectPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(OperationSelectContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}