package com.hlxyedu.mhk.model;

import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.FileUrlVO;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.TotalScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.bean.VersionVO;
import com.hlxyedu.mhk.model.http.HttpHelper;
import com.hlxyedu.mhk.model.http.response.HttpResponse;
import com.hlxyedu.mhk.model.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    public void saveOfflineData(String dataVO) {
        mPreferencesHelper.saveOfflineData(dataVO);
    }

    @Override
    public String getOfflineData() {
        return mPreferencesHelper.getOfflineData();
    }

    @Override
    public void saveReExamComposition(String txt) {
        mPreferencesHelper.saveReExamComposition(txt);
    }

    @Override
    public String getReExamComposition() {
        return mPreferencesHelper.getReExamComposition();
    }

    @Override
    public Flowable<HttpResponse<UserVO>> postLoginBody(String mobile, String pwd) {
        return mHttpHelper.postLoginBody(mobile, pwd);
    }

    @Override
    public Flowable<HttpResponse<String>> getNoteInfo() {
        return mHttpHelper.getNoteInfo();
    }

    @Override
    public Flowable<HttpResponse<UserVO>> getUserInfo() {
        return mHttpHelper.getUserInfo();
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
        return mHttpHelper.getOperationList(userId, pageNumber, pageSize, hws);
    }

    @Override
    public Flowable<HttpResponse<List<ExamVO>>> getMockList(String id, int pageNumber, int pageSize) {
        return mHttpHelper.getMockList(id, pageNumber, pageSize);
    }

    @Override
    public Flowable<HttpResponse<ScoreVO>> postExerciseScoreBody(String userId, String homeworkId, String answer, String examId, String testId, String type) {
        return mHttpHelper.postExerciseScoreBody(userId, homeworkId, answer, examId, testId, type);
    }

    @Override
    public Flowable<HttpResponse<TotalScoreVO>> getTotalScore(String userId) {
        return mHttpHelper.getTotalScore(userId);
    }

    @Override
    public Flowable<HttpResponse<String>> saveLog(String userId, String mobileInfo, String exceptionInfo) {
        return mHttpHelper.saveLog(userId,mobileInfo,exceptionInfo);
    }

    @Override
    public Flowable<HttpResponse<String>> uploadRecord(RequestBody userId, RequestBody examId, RequestBody homeworkId, RequestBody testId, RequestBody testType, RequestBody fileName, MultipartBody.Part fileData) {
        return mHttpHelper.uploadRecord(userId, examId, homeworkId, testId, testType, fileName, fileData);
    }

    @Override
    public Flowable<HttpResponse<String>> uploadVideo(RequestBody userId, RequestBody examId, RequestBody testId, RequestBody type, RequestBody fileName, MultipartBody.Part fileData) {
        return mHttpHelper.uploadVideo(userId, examId, testId, type, fileName, fileData);
    }

    @Override
    public Flowable<VersionVO> getNewVersion(RequestBody requestBody) {
        return mHttpHelper.getNewVersion(requestBody);
    }

    @Override
    public Flowable<FileUrlVO> getFileUrl(String fid) {
        return mHttpHelper.getFileUrl(fid);
    }
}
