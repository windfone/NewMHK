package com.hlxyedu.mhk.ui.main.contract;

import com.hlxyedu.mhk.model.bean.ExamListVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface OperationContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onSuccess(ExamListVO examListVO);

    }

    interface Presenter extends BasePresenter<View> {

        void getExamList(String examType, String id, int pageNum, int perpage, String version);

        String getID();

    }
}