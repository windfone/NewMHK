package com.hlxyedu.mhk.ui.ebook.presenter;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.ebook.contract.BookContract;
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
public class BookPresenter extends RxPresenter<BookContract.View> implements BookContract.Presenter {
    private DataManager mDataManager;

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

                        if(!mView.isShow()){
                            return;
                        }
                        cimmitAnswer((String) s.getAnswer(),s.getExamId(),s.getHomeworkId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public void cimmitAnswer(String finalAnswer,String paperId,String homeworkId) {
        addSubscribe(
                mDataManager.postExerciseScoreBody(getUserId(), homeworkId,finalAnswer, paperId)
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
    public String getUserId() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(),UserVO.class);
        return userVO.getId();
    }


}