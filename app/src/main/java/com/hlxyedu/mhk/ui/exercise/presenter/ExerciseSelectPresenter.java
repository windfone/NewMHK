package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.ExerciseSelectContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ExerciseSelectPresenter extends RxPresenter<ExerciseSelectContract.View> implements ExerciseSelectContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ExerciseSelectPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ExerciseSelectContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}