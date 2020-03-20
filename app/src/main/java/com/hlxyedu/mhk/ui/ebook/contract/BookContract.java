package com.hlxyedu.mhk.ui.ebook.contract;

import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.ui.eread.contract.ReadContract;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface BookContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void commitSuccess(ScoreVO scoreVO);

        boolean isShow();
    }

    interface Presenter extends BasePresenter<View> {
        void cimmitAnswer(String finalAnswer,String paperId,String homeworkId);

        String getUserId();
    }
}