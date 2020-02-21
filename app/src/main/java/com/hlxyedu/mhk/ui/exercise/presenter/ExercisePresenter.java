package com.hlxyedu.mhk.ui.exercise.presenter;

import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.event.ActionEvent;
import com.hlxyedu.mhk.model.event.RecordEvent;
import com.hlxyedu.mhk.ui.exercise.contract.ExerciseContract;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

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

        // 倒计时
        addSubscribe(RxBus.getDefault().toFlowable(ActionEvent.class)
                .compose(RxUtil.<ActionEvent>rxSchedulerHelper())
                .filter(new Predicate<ActionEvent>() {
                    @Override
                    public boolean test(@NonNull ActionEvent actionEvent) throws Exception {
                        return actionEvent.getType().equals(ActionEvent.COUNTDOWN);
                    }
                })
                .subscribeWith(new CommonSubscriber<ActionEvent>(mView) {
                    @Override
                    public void onNext(ActionEvent s) {
                        mView.xTopBarSecond(s.getSecond(),s.getProblemTitle());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        //下一页
        addSubscribe(RxBus.getDefault().toFlowable(ActionEvent.class)
                .compose(RxUtil.<ActionEvent>rxSchedulerHelper())
                .filter(new Predicate<ActionEvent>() {
                    @Override
                    public boolean test(@NonNull ActionEvent actionEvent) throws Exception {
                        return actionEvent.getType().equals(ActionEvent.NEXT);
                    }
                })
                .subscribeWith(new CommonSubscriber<ActionEvent>(mView) {
                    @Override
                    public void onNext(ActionEvent s) {
                        mView.nextPage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

        // 录音
        addSubscribe(RxBus.getDefault().toFlowable(RecordEvent.class)
                .compose(RxUtil.<RecordEvent>rxSchedulerHelper())
                .filter(new Predicate<RecordEvent>() {
                    @Override
                    public boolean test(@NonNull RecordEvent recordEvent) throws Exception {
                        return recordEvent.getType().equals(RecordEvent.START_RECORD);
                    }
                })
                .subscribeWith(new CommonSubscriber<RecordEvent>(mView) {
                    @Override
                    public void onNext(RecordEvent s) {
                        mView.recordSecond();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }
}