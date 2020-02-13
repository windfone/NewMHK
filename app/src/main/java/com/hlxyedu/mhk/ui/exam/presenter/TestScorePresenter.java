package com.hlxyedu.mhk.ui.exam.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exam.contract.TestScoreContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class TestScorePresenter extends RxPresenter<TestScoreContract.View> implements TestScoreContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestScorePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestScoreContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}