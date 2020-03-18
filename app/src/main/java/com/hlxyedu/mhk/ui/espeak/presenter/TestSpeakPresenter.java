package com.hlxyedu.mhk.ui.espeak.presenter;

import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.ui.espeak.contract.TestSpeakContract;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by zhangguihua
 */
public class TestSpeakPresenter extends RxPresenter<TestSpeakContract.View> implements TestSpeakContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestSpeakPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestSpeakContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(BaseEvents.class)
                .compose(RxUtil.<BaseEvents>rxSchedulerHelper())
                .filter(new Predicate<BaseEvents>() {
                    @Override
                    public boolean test(@NonNull BaseEvents baseEvents) throws Exception {
                        return baseEvents.getType().equals(BaseEvents.NOTICE);
                    }
                })
                .subscribeWith(new CommonSubscriber<BaseEvents>(mView) {
                    @Override
                    public void onNext(BaseEvents s) {
                        mView.onMainEvent(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

}