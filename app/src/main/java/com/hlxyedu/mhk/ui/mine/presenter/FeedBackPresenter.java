package com.hlxyedu.mhk.ui.mine.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.mine.contract.FeedBackContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class FeedBackPresenter extends RxPresenter<FeedBackContract.View> implements FeedBackContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public FeedBackPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(FeedBackContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}