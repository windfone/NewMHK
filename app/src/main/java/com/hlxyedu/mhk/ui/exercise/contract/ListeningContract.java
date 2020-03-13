package com.hlxyedu.mhk.ui.exercise.contract;

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
    }

    interface Presenter extends BasePresenter<View> {
        void cimmitAnswer(String finalAnswer,String paperId,String homeworkId);

        String getUserId();
    }
}