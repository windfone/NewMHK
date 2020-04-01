package com.hlxyedu.mhk.ui.mine.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.mine.contract.AboutUsContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class AboutUsPresenter extends RxPresenter<AboutUsContract.View> implements AboutUsContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public AboutUsPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(AboutUsContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}