package com.hlxyedu.mhk.ui.login.contract;

import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface FoundPsdContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void foundPsdSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void foundPsd(String mobile,String password,String idNum);
    }

}