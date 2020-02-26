package com.hlxyedu.mhk.ui.main.presenter;

import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.ui.main.contract.MineContract;

import javax.inject.Inject;

/**
 * Created by zhangguihua
 */
public class MinePresenter extends RxPresenter<MineContract.View> implements MineContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MinePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(MineContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }

    @Override
    public boolean isLogin() {
        return mDataManager.getLoginStatus();
    }

    @Override
    public void clearLoginInfo() {
        mDataManager.clearLoginInfo();
    }

    @Override
    public void setLoginState(boolean login) {
        mDataManager.setLoginStatus(login);
    }

}