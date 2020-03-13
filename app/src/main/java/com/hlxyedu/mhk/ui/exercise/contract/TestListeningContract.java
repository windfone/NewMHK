package com.hlxyedu.mhk.ui.exercise.contract;

import com.hlxyedu.mhk.model.event.BaseEvents;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface TestListeningContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onMainEvent(BaseEvents event);

        void buttonVisible(boolean isVisible);
    }

    interface Presenter extends BasePresenter<View> {
    }
}