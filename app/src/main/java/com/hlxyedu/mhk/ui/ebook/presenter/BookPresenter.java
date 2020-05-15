package com.hlxyedu.mhk.ui.ebook.presenter;

import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.event.ExitCommitEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.ebook.contract.BookContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;
import com.orhanobut.logger.Logger;
import com.skyworth.rxqwelibrary.app.AppConstants;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class BookPresenter extends RxPresenter<BookContract.View> implements BookContract.Presenter {
    private DataManager mDataManager;

    private CommitEvent commitEvent;

    private ExitCommitEvent exitCommitEvent;

    @Inject
    public BookPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(BookContract.View view) {
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
                        Logger.d("收到书面上传答案RxBus");
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
        String zip =  commitEvent.getZip();
        String unzip = commitEvent.getUnzip();
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
                                        Logger.d("书面答案上传成功");
                                        // 删除zip 包
                                        FileUtils.deleteFile(zip);
                                        // 删除解压出来的文件
                                        FileUtils.deleteDir(unzip);
                                        mView.commitSuccess(scoreVO);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.d("书面答案上传失败" + e.toString());
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
        String zip =  exitCommitEvent.getZip();
        String unzip = exitCommitEvent.getUnzip();
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
                                        Logger.d("书面提前交卷答案上传成功");
                                        // 删除zip 包
                                        FileUtils.deleteFile(zip);
                                        // 删除解压出来的文件
                                        FileUtils.deleteDir(unzip);
                                        mView.onFinish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.d("书面提前交卷答案上传失败" + e.toString());
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