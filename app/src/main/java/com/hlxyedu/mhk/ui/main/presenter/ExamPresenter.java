package com.hlxyedu.mhk.ui.main.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.main.contract.ExamContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ExamPresenter extends RxPresenter<ExamContract.View> implements ExamContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ExamPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ExamContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}