package com.hlxyedu.mhk.ui.mine.contract;

import com.hlxyedu.mhk.model.bean.TotalScoreVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface GradeContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void success(TotalScoreVO totalScoreVO);
    }

    interface Presenter extends BasePresenter<View> {

        void getTotalScore();
    }
}