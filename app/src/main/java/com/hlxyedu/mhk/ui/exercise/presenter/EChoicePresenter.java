package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.exercise.contract.EChoiceContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class EChoicePresenter extends RxPresenter<EChoiceContract.View> implements EChoiceContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public EChoicePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(EChoiceContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}