package com.hlxyedu.mhk.model;

import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.HttpHelper;
import com.hlxyedu.mhk.model.http.response.HttpResponse;
import com.hlxyedu.mhk.model.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 作者：skyworth on 2017/7/11 09:55
 * 邮箱：dqwei@iflytek.com
 */

public class DataManager implements HttpHelper, PreferencesHelper {

    HttpHelper mHttpHelper;

    PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper, PreferencesHelper mPreferencesHelper) {
        mHttpHelper = httpHelper;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferencesHelper.getLoginStatus();
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferencesHelper.setLoginStatus(isLogin);
    }

    @Override
    public boolean getNightModeState() {
        return false;
    }

    @Override
    public void setNightModeState(boolean state) {

    }

    @Override
    public boolean getIsFrist() {
        return mPreferencesHelper.getIsFrist();
    }

    @Override
    public void setIsFrist(boolean isFrist) {
        mPreferencesHelper.setIsFrist(isFrist);
    }

    @Override
    public void setUid(String uid) {
        mPreferencesHelper.setUid(uid);
    }

    @Override
    public String getSpUserInfo() {
        return mPreferencesHelper.getSpUserInfo();
    }

    @Override
    public void setUserInfo(String userInfo) {
        mPreferencesHelper.setUserInfo(userInfo);
    }

    @Override
    public void clearLoginInfo() {
        mPreferencesHelper.clearLoginInfo();
    }

    @Override
    public Flowable<HttpResponse<UserVO>> postLoginBody(String mobile, String pwd) {
        return mHttpHelper.postLoginBody(mobile, pwd);
    }

    @Override
    public Flowable<HttpResponse<String>> postModifyPsdBody(String mobile, String password, String idNum) {
        return mHttpHelper.postModifyPsdBody(mobile, password, idNum);
    }

    @Override
    public Flowable<HttpResponse<ExerciseListVO>> getExamList(String examType, String id, int pageNum, int perpage, String version) {
        return mHttpHelper.getExamList(examType, id, pageNum, perpage, version);
    }

    @Override
    public Flowable<HttpResponse<List<OperationVO>>> getOperationList(String userId, int pageNumber, int pageSize, String hws) {
        return mHttpHelper.getOperationList(userId,pageNumber,pageSize,hws);
    }

    @Override
    public Flowable<HttpResponse<ScoreVO>> postExerciseScoreBody(String id, String homeworkId, String answer, String examId) {
        return mHttpHelper.postExerciseScoreBody(id,homeworkId,answer,examId);
    }

}
