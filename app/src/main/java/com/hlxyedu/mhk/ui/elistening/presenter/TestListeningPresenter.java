package com.hlxyedu.mhk.ui.elistening.presenter;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.elistening.contract.TestListeningContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;
import com.skyworth.rxqwelibrary.app.AppConstants;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class TestListeningPresenter extends RxPresenter<TestListeningContract.View> implements TestListeningContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public TestListeningPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(TestListeningContract.View view) {
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

    @Override
    public String getUserId() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(), UserVO.class);
        return userVO.getId();
    }

    @Override
    public void uploadVideo(File file, String examId, String testId, String testType) {
        String fileName = "";
        fileName = file.getName();
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), getUserId());
        RequestBody examIdBody = RequestBody.create(MediaType.parse("text/plain"), examId);
        if (testId == null) {
            testId = "";
        }
        if (testType == null) {
            testType = "";
        }
        RequestBody testIdBody = RequestBody.create(MediaType.parse("text/plain"), testId);
        RequestBody testTypeBody = RequestBody.create(MediaType.parse("text/plain"), testType);
        RequestBody fileNameBody = RequestBody.create(MediaType.parse("text/plain"), fileName);

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("videoData", file.getName(), fileRequestBody);

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("audioData", file.getName(),
//                RequestBody.create(MediaType.parse("image/*"), file));
        addSubscribe(mDataManager.uploadVideo(userIdBody, examIdBody, testIdBody, testTypeBody, fileNameBody, fileData)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String s) {
                            }

                            @Override
                            public void onError(Throwable e) {
                                //当数据返回为null时 做特殊处理
                                if (e instanceof HttpException) {
                                    HttpResponseCode httpResponseCode = RegUtils
                                            .onError((HttpException) e);
                                    ToastUtils.showShort(httpResponseCode.getMsg());
                                }
                                mView.responeError("数据请求失败，请检查网络！");
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
//                                FileUtils.deleteDir(AppConstants.VIDEO_RECORDING_PATH);
                            }
                        }
                )
        );
    }

}