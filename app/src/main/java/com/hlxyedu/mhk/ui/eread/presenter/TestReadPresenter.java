package com.hlxyedu.mhk.ui.eread.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.eread.contract.TestReadContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class TestReadPresenter extends RxPresenter<TestReadContract.View> implements TestReadContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestReadPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestReadContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}