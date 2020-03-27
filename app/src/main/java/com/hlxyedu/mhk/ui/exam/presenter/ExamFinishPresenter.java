package com.hlxyedu.mhk.ui.exam.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exam.contract.ExamFinishContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ExamFinishPresenter extends RxPresenter<ExamFinishContract.View> implements ExamFinishContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ExamFinishPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ExamFinishContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}