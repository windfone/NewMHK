package com.hlxyedu.mhk.ui.exercise.contract;

import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface ExerciseContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void recordSecond();

        // 下一页
        void nextPage();

        // 开始倒计时
        void xTopBarSecond(int second,String problemTitle);

    }

    interface Presenter extends BasePresenter<View> {
    }
}