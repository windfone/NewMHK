package com.hlxyedu.mhk.ui.ecomposition.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.ecomposition.contract.TestTxtContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class TestTxtPresenter extends RxPresenter<TestTxtContract.View> implements TestTxtContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestTxtPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestTxtContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}