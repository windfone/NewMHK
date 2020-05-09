package com.hlxyedu.mhk.ui.main.presenter;

import com.blankj.utilcode.util.AppUtils;
import com.hlxyedu.mhk.api.Constants;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.FileUrlVO;
import com.hlxyedu.mhk.model.bean.VersionVO;
import com.hlxyedu.mhk.model.http.api.ManageApis;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.main.contract.MainContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.MediaType;
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
}