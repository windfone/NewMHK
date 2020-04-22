package com.hlxyedu.mhk.ui.eread.presenter;

import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.event.ExitCommitEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.eread.contract.ReadContract;
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
public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter {
    private DataManager mDataManager;

    private CommitEvent commitEvent;

    private ExitCommitEvent exitCommitEvent;

    @Inject
    public ReadPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ReadContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CommitEvent.class)
                .compose(RxUtil.<CommitEvent>rxSchedulerHelper())
                .filter(new Predicate<CommitEvent>() {
                    @Override
                    public boolean test(@NonNull CommitEvent commitEvent) throws Exception {
                        return commitEvent.getType().equals(CommitEvent.COMMIT);
                    }
                })
                .subscribeWith(new CommonSubscriber<CommitEvent>(mView) {
                    @Override
                    public void onNext(CommitEvent s) {

                        if (!mView.isShow()) {
                            return;
                        }
//                        cimmitAnswer((String) s.getAnswer(), s.getExamId(), s.getHomeworkId(), s.getTestId(), s.getTestType());

                        commitEvent = s;
                        cimmitAnswer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(ExitCommitEvent.class)
                .compose(RxUtil.<ExitCommitEvent>rxSchedulerHelper())
                .filter(new Predicate<ExitCommitEvent>() {
                    @Override
                    public boolean test(@NonNull ExitCommitEvent exitCommitEvent) throws Exception {
                        return exitCommitEvent.getType().equals(ExitCommitEvent.EXIT_COMMIT);
                    }
                })
                .subscribeWith(new CommonSubscriber<ExitCommitEvent>(mView) {
                    @Override
                    public void onNext(ExitCommitEvent s) {

                        if (!mView.isShow()) {
                            return;
                        }
//                        exitCommitAnswer((String) s.getAnswer(), s.getExamId(), s.getHomeworkId(), s.getTestId(), s.getTestType());

                        exitCommitEvent = s;
                        exitCommitAnswer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public void cimmitAnswer() {
        if (!NetworkUtils.isConnected()) {
            mView.reUploadAnswer("未检测到网络，请连网后重新上传");
            return;
        }
        String finalAnswer = (String) commitEvent.getAnswer();
        String paperId = commitEvent.getExamId();
        String homeworkId = commitEvent.getHomeworkId();
        String testId = commitEvent.getTestId();
        String type = commitEvent.getTestType();

        addSubscribe(
                mDataManager.postExerciseScoreBody(getUserId(), homeworkId, finalAnswer, paperId, testId, type)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<ScoreVO>(mView) {
                                    @Override
                                    public void onNext(ScoreVO scoreVO) {
                                        mView.commitSuccess(scoreVO);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mView.reUploadAnswer("答案上传失败，请重新上传答案");
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
    public void exitCommitAnswer() {
        if (!NetworkUtils.isConnected()) {
            mView.exitReUploadAnswer("未检测到网络，请连网后重新上传");
            return;
        }
        String finalAnswer = (String) exitCommitEvent.getAnswer();
        String paperId = exitCommitEvent.getExamId();
        String homeworkId = exitCommitEvent.getHomeworkId();
        String testId = exitCommitEvent.getTestId();
        String type = exitCommitEvent.getTestType();

        addSubscribe(
                mDataManager.postExerciseScoreBody(getUserId(), homeworkId, finalAnswer, paperId, testId, type)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<ScoreVO>(mView) {
                                    @Override
                                    public void onNext(ScoreVO scoreVO) {
                                        mView.onFinish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mView.exitReUploadAnswer("答案上传失败，请重新上传答案");
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
    public String getUserId() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(), UserVO.class);
        return userVO.getId();
    }

}