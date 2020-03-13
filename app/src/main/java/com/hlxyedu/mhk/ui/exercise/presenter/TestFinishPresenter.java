package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.TestFinishContract;

import javax.inject.Inject;

/**
 * Created by zhangugihuaq
 */
public class TestFinishPresenter extends RxPresenter<TestFinishContract.View> implements TestFinishContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestFinishPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestFinishContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}