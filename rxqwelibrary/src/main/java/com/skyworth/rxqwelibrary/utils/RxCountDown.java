package com.skyworth.rxqwelibrary.utils;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxCountDown {

    private static Disposable mDisposable;

    public static void countdown(long time, final IRxExecute execute) {
        Observable.intervalRange(0, time + 1, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                })
                .doOnNext(aLong -> {
                    //正在倒计时
                    if (execute != null) {
                        execute.doNext(time - aLong);
                    }
                })
                .doOnComplete(() -> {
                    if (execute != null) {
                        execute.doComplete();
                    }
                })
                .doOnError(throwable -> {
                    cancel();
                })
                .subscribe();

    }


    /**
     *    * 取消订阅
     *    
     */
    public static void cancel() {

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            Logger.d("====定时器取消======");
        }
    }


    public interface IRxExecute {
        void doNext(long number);

        void doComplete();
    }

}
