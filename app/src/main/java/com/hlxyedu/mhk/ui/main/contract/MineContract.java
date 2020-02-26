package com.hlxyedu.mhk.ui.main.contract;

import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface MineContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);
    }

    interface Presenter extends BasePresenter<View> {

        boolean isLogin();

        void clearLoginInfo();

        void setLoginState(boolean login);

    }

}