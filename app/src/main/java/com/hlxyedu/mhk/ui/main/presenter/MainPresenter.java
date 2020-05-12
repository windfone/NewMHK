package com.hlxyedu.mhk.ui.main.presenter;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.api.Constants;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.FileUrlVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.bean.VersionVO;
import com.hlxyedu.mhk.model.http.api.ManageApis;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.main.contract.MainContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }

    @Override
    public void checkNewVersion() {
        JSONObject root = new JSONObject();
        try {
            root.put("projectId", ManageApis.APP_ID);
            root.put("releaseNum",  AppUtils.getAppVersionCode());
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody
                .create(MediaType.parse(Constants.SYK_DATA_FORMAT), root.toString());

        addSubscribe(mDataManager.getNewVersion(requestBody)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<VersionVO>(mView) {
                            @Override
                            public void onNext(VersionVO versionVO) {
                                getFileUrl(versionVO);
                            }

                            @Override
                            public void onError(Throwable e) {
                                //当数据返回为null时 做特殊处理
                                if (e instanceof HttpException) {
                                    HttpResponseCode httpResponseCode = RegUtils
                                            .onError((HttpException) e);
                                }
                                mView.responeError("数据请求失败，请检查网络！");
                            }
                        }
                )
        );
    }

    private void getFileUrl(final VersionVO versionVO){
        addSubscribe(mDataManager.getFileUrl(versionVO.getFileCode())
                        .compose(RxUtil.rxSchedulerHelper())
                        .subscribeWith(
                                new CommonSubscriber<FileUrlVO>(mView) {
                                    @Override
                                    public void onNext(FileUrlVO fileUrlVO) {
//                                mView.initExamRegistrations(examRegistrationVOS);
                                        mView.versionSuccess(versionVO,fileUrlVO.getUrl());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        //当数据返回为null时 做特殊处理
                                        if (e instanceof HttpException) {
                                            HttpResponseCode httpResponseCode = RegUtils
                                                    .onError((HttpException) e);
                                        }
                                        mView.responeError("数据请求失败，请检查网络！");
                                    }

                                }
                        )
        );
    }

    @Override
    public void uploadLogFileBatch(List<File> files) {
        if (files.isEmpty()) {
            Logger.d("本地无log日志");
            return;
        }
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), getUserId());

        List<RequestBody> requestBodies = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            requestBodies.add(requestBody);
        }

        List<MultipartBody.Part> parts2 = RegUtils
                .filesToMultipartBodyParts("logData", MediaType.parse("multipart/form-data"), files);
        addSubscribe(
                mDataManager.uploadLogFileBatch(userIdBody, requestBodies,parts2)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleTestResult())
                        .subscribeWith(
                                new CommonSubscriber<List<String>>(mView) {
                                    @Override
                                    public void onNext(List<String> data) {


                                    }

                                    @Override
                                    public void onError(Throwable e) {


                                    }

                                    @Override
                                    public void onComplete() {
                                        super.onComplete();
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