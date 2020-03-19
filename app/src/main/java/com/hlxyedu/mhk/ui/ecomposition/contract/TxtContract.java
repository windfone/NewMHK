package com.hlxyedu.mhk.ui.ecomposition.contract;

import com.hlxyedu.mhk.model.event.BaseEvents;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface TxtContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void commitSuccess();

        boolean isShow();
    }

    interface Presenter extends BasePresenter<View> {

        void cimmitAnswer(String finalAnswer,String paperId,String homeworkId);

        String getUserId();

    }

}