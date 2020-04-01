package com.hlxyedu.mhk.ui.mine.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.mine.contract.ServiceTermsContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ServiceTermsPresenter extends RxPresenter<ServiceTermsContract.View> implements ServiceTermsContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ServiceTermsPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ServiceTermsContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}