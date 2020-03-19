package com.hlxyedu.mhk.ui.eread.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.eread.contract.ReadContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ReadPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ReadContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }
}