package com.hlxyedu.mhk.ui.main.presenter;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.model.event.SelectEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.main.contract.ExerciseContract;
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
public class ExercisePresenter extends RxPresenter<ExerciseContract.View> implements ExerciseContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public ExercisePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(ExerciseContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        // 选择（列表条件筛选）
        addSubscribe(RxBus.getDefault().toFlowable(SelectEvent.class)
                .compose(RxUtil.<SelectEvent>rxSchedulerHelper())
                .filter(new Predicate<SelectEvent>() {
                    @Override
                    public boolean test(@NonNull SelectEvent selectEvent) throws Exception {
                        return selectEvent.getType().equals(SelectEvent.EXERCISE_SEL);
                    }
                })
                .subscribeWith(new CommonSubscriber<SelectEvent>(mView) {
                    @Override
                    public void onNext(SelectEvent s) {
                        mView.onSelect(s.getQuestionType());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(DownLoadEvent.class)
                        .compose(RxUtil.<DownLoadEvent>rxSchedulerHelper())
                        .filter(new Predicate<DownLoadEvent>() {
                            @Override
                            public boolean test(@NonNull DownLoadEvent downLoadEvent) throws Exception {
                                return downLoadEvent.getType().equals(DownLoadEvent.DOWNLOAD_PAPER_EXERCISE);
                            }
                        })
                        .subscribeWith(new CommonSubscriber<DownLoadEvent>(mView) {
                            @Override
                            public void onNext(DownLoadEvent s) {
//                        mView.download(s.getPos(),s.getDownloadPath(),s.getType(),s.getExamName(),s.getExamId());
                                mView.download(s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        })
        );

    }

    @Override
    public void getExamList(String examType, String id, int pageNum, int perpage, String version) {
        addSubscribe(
                mDataManager.getExamList(examType, id, pageNum, perpage, version)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<ExerciseListVO>(mView) {
                                    @Override
                                    public void onNext(ExerciseListVO exerciseListVO) {
                                        mView.onSuccess(exerciseListVO);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (e.toString().contains("UnknownHostException")) {
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
    public String getID() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(), UserVO.class);
        return userVO.getId();
    }

}