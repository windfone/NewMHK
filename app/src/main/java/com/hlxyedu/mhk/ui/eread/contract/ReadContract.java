package com.hlxyedu.mhk.ui.eread.contract;

import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface ReadContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void commitSuccess(ScoreVO scoreVO);

        boolean isShow();
    }

    interface Presenter extends BasePresenter<View> {

        String getUserId();
    }
}