package com.hlxyedu.mhk.ui.splash.contract;

import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface GuideContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);
    }

    interface Presenter extends BasePresenter<View> {

        void setIsFirst(boolean b);

    }
}