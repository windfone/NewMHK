package com.hlxyedu.mhk.ui.elistening.contract;

import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface ListeningContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void commitSuccess(ScoreVO scoreVO);

        void onFinish();

        boolean isShow();

        void reUploadAnswer(String s);

        void exitReUploadAnswer(String s);
    }

    interface Presenter extends BasePresenter<View> {

        String getUserId();

        void cimmitAnswer();

        void exitCommitAnswer();

    }
}