package com.hlxyedu.mhk.ui.ecomposition.contract;

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

        void getExitAnswer();

        void onFinish(String str);

        void reUploadAnswer(String s);

        void exitReUploadAnswer(String s);

    }

    interface Presenter extends BasePresenter<View> {


        String getUserId();

        void saveReExamCompositon(String txt);

        String getReExamComposition();

        void toExitAnswer(String exitAnswer);

        void cimmitAnswer();

        void exitCommitAnswer(String exitAnswer);

    }

}