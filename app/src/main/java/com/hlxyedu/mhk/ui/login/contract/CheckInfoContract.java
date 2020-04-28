package com.hlxyedu.mhk.ui.login.contract;

import com.hlxyedu.mhk.model.bean.UserVO;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface CheckInfoContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onSuccessInfo(UserVO userVO);
    }

    interface Presenter extends BasePresenter<View> {

        void getUserInfo();
    }
}