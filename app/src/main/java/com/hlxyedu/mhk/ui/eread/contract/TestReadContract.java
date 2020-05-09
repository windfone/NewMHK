package com.hlxyedu.mhk.ui.eread.contract;

import com.hlxyedu.mhk.model.event.BaseEvents;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

import java.io.File;

/**
 * Created by zhangguihua
 */
public interface TestReadContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onMainEvent(BaseEvents event);
    }

    interface Presenter extends BasePresenter<View> {

        String getUserId();

        void saveLog(String userId, String mobileInfo, String exceptionInfo);

        void uploadVideo(File file, String examId, String testId, String testType);

    }
}