package com.hlxyedu.mhk.ui.mine.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.mine.contract.GradeContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class GradePresenter extends RxPresenter<GradeContract.View> implements GradeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public GradePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(GradeContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}