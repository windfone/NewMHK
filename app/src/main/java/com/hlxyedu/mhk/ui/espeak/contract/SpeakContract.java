package com.hlxyedu.mhk.ui.espeak.contract;

import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface SpeakContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void commitSuccess();

        boolean isShow();
    }

    interface Presenter extends BasePresenter<View> {

        String getUserId();

    }
}