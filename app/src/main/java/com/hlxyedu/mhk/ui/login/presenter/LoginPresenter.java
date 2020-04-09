package com.hlxyedu.mhk.ui.login.presenter;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.LoginEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.login.contract.LoginContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public LoginPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(LoginContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .compose(RxUtil.<LoginEvent>rxSchedulerHelper())
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(@NonNull LoginEvent loginEvent) throws Exception {
                        return loginEvent.getType().equals(LoginEvent.LOGIN);
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginEvent>(mView) {
                    @Override
                    public void onNext(LoginEvent s) {
                        mView.closeLogin();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public void login(String mobile, String psd) {
        addSubscribe(
                mDataManager.postLoginBody(mobile, psd)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<UserVO>(mView) {
                                    @Override
                                    public void onNext(UserVO userVO) {
                                        mDataManager.setLoginStatus(true);
                                        String s = GsonUtils.toJson(userVO);
                                        mDataManager.setUserInfo(s);
                                        mView.loginSuccess();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.showShort(e.getMessage());
                                        //当数据返回为null时 做特殊处理
                                        if (e instanceof HttpException) {
                                            HttpResponseCode httpResponseCode = RegUtils
                                                    .onError((HttpException) e);
//                                            ToastUtils.showShort(httpResponseCode.getMsg());
                                        }
                                        mView.responeError("数据请求失败，请检查网络！");
                                    }
                                }
                        )
        );
    }

}