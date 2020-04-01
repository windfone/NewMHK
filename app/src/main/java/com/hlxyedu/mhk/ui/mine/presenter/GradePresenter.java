package com.hlxyedu.mhk.ui.mine.presenter;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.TotalScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.mine.contract.GradeContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import javax.inject.Inject;

import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class GradePresenter extends RxPresenter<GradeContract.View> implements GradeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public GradePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(GradeContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }

    @Override
    public void getTotalScore() {
        addSubscribe(
                mDataManager.getTotalScore(getUserId())
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<TotalScoreVO>(mView) {
                                    @Override
                                    public void onNext(TotalScoreVO s) {
                                        mView.success(s);
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

    public String getUserId() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(), UserVO.class);
        return userVO.getId();
    }

}