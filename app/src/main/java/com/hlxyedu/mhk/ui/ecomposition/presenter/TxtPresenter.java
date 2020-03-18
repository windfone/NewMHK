package com.hlxyedu.mhk.ui.ecomposition.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.ecomposition.contract.TxtContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class TxtPresenter extends RxPresenter<TxtContract.View> implements TxtContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TxtPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TxtContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}