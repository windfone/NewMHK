package com.hlxyedu.mhk.ui.main.presenter;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.model.event.ReExamEvent;
import com.hlxyedu.mhk.model.event.RestartEvent;
import com.hlxyedu.mhk.model.event.SaveLogEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.main.contract.ExamContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class ExamPresenter extends RxPresenter<ExamContract.View> implements ExamContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ExamPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ExamContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        //下载指令
        addSubscribe(RxBus.getDefault().toFlowable(DownLoadEvent.class)
                .compose(RxUtil.<DownLoadEvent>rxSchedulerHelper())
                .filter(new Predicate<DownLoadEvent>() {
                    @Override
                    public boolean test(@NonNull DownLoadEvent downLoadEvent) throws Exception {
                        return downLoadEvent.getType().equals(DownLoadEvent.DOWNLOAD_PAPER_EXAM);
                    }
                })
                .subscribeWith(new CommonSubscriber<DownLoadEvent>(mView) {
                    @Override
                    public void onNext(DownLoadEvent s) {
                        mView.download();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        // 续考
        addSubscribe(RxBus.getDefault().toFlowable(ReExamEvent.class)
                .compose(RxUtil.<ReExamEvent>rxSchedulerHelper())
                .filter(new Predicate<ReExamEvent>() {
                    @Override
                    public boolean test(@NonNull ReExamEvent reExamEvent) throws Exception {
                        return reExamEvent.getType().equals(ReExamEvent.RE_EXAM);
                    }
                })
                .subscribeWith(new CommonSubscriber<ReExamEvent>(mView) {
                    @Override
                    public void onNext(ReExamEvent s) {
                       mView.reExamination(s.getQuestionType());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        // 锁屏续考
        addSubscribe(RxBus.getDefault().toFlowable(RestartEvent.class)
                .compose(RxUtil.<RestartEvent>rxSchedulerHelper())
                .filter(new Predicate<RestartEvent>() {
                    @Override
                    public boolean test(@NonNull RestartEvent restartEvent) throws Exception {
                        return restartEvent.getType().equals(RestartEvent.RESTART);
                    }
                })
                .subscribeWith(new CommonSubscriber<RestartEvent>(mView) {
                    @Override
                    public void onNext(RestartEvent s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        // 接收输入框内容，准备上传到服务器
        addSubscribe(RxBus.getDefault().toFlowable(SaveLogEvent.class)
                .compose(RxUtil.<SaveLogEvent>rxSchedulerHelper())
                .filter(new Predicate<SaveLogEvent>() {
                    @Override
                    public boolean test(@NonNull SaveLogEvent saveLogEvent) throws Exception {
                        return saveLogEvent.getType().equals(SaveLogEvent.SAVE_LOG);
                    }
                })
                .subscribeWith(new CommonSubscriber<SaveLogEvent>(mView) {
                    @Override
                    public void onNext(SaveLogEvent s) {
                        mView.upLoadErrorEditStr(s.getLog());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

    }

    @Override
    public void getMockList(String id, int pageNumber, int pageSize) {
        addSubscribe(
                mDataManager.getMockList(id, pageNumber, pageSize)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<List<ExamVO>>(mView) {
                                    @Override
                                    public void onNext(List<ExamVO> examVOS) {
                                        mView.onSuccess(examVOS);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (e.toString().contains("UnknownHostException") || e.toString().contains("ConnectException")) {
                                            mView.responeError("数据请求失败，请检查网络！");
                                            return;
                                        }
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


    @Override
    public void saveLog(String userId, String mobileInfo, String exceptionInfo) {
        addSubscribe(
                mDataManager.saveLog(userId, mobileInfo, exceptionInfo)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<String>(mView) {
                                    @Override
                                    public void onNext(String s) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                }
                        )
        );
    }

    @Override
    public String getID() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(), UserVO.class);
        return userVO.getId();
    }
}