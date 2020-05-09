package com.hlxyedu.mhk.ui.main.contract;

import com.hlxyedu.mhk.model.bean.VersionVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface MainContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void versionSuccess(VersionVO versionVO, String apkUrl);
    }

    interface Presenter extends BasePresenter<View> {
        void checkNewVersion();
    }
}