package com.hlxyedu.mhk.ui.espeak.contract;

import com.hlxyedu.mhk.model.event.BaseEvents;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface TestSpeakContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onMainEvent(BaseEvents event);
    }

    interface Presenter extends BasePresenter<View> {
    }
}