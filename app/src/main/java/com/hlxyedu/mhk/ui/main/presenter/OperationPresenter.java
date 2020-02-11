package com.hlxyedu.mhk.ui.main.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.main.contract.OperationContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class OperationPresenter extends RxPresenter<OperationContract.View> implements OperationContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public OperationPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(OperationContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}