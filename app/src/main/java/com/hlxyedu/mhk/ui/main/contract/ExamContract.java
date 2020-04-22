package com.hlxyedu.mhk.ui.main.contract;

import com.hlxyedu.mhk.model.bean.ExamVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

import java.util.List;

/**
 * Created by zhangguihua
 */
public interface ExamContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onSuccess(List<ExamVO> examVOS);

        void download();

        void reExamination(String questionType);
    }

    interface Presenter extends BasePresenter<View> {

        //useId
        void getMockList(String id, int pageNumber, int pageSize);

        String getID();

    }
}