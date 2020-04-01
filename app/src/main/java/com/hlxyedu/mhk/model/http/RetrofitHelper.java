package com.hlxyedu.mhk.model.http;


import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.TotalScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.api.ManageApis;
import com.hlxyedu.mhk.model.http.api.QBaseApis;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 *
 */
public class RetrofitHelper implements HttpHelper {

    private QBaseApis qBaseApis;

    private ManageApis manageApis;

    @Inject
    public RetrofitHelper(QBaseApis qBaseApis, ManageApis manageApis) {
        this.qBaseApis = qBaseApis;
        this.manageApis = manageApis;
    }


    @Override
    public Flowable<HttpResponse<UserVO>> postLoginBody(String mobile, String pwd) {
        return qBaseApis.postLoginBody(mobile,pwd);
    }

    @Override
    public Flowable<HttpResponse<String>> postModifyPsdBody(String mobile, String password, String idNum) {
        return qBaseApis.postModifyPsdBody(mobile,password,idNum);
    }

    @Override
    public Flowable<HttpResponse<ExerciseListVO>> getExamList(String examType, String id, int pageNum, int perpage, String version) {
        return qBaseApis.getExamList(examType,id,pageNum,perpage,version);
    }

    @Override
    public Flowable<HttpResponse<List<OperationVO>>> getOperationList(String userId, int pageNumber, int pageSize, String hws) {
        return qBaseApis.getOperationList(userId,pageNumber,pageSize,hws);
    }

    @Override
    public Flowable<HttpResponse<List<ExamVO>>> getMockList(String id, int pageNumber, int pageSize) {
        return qBaseApis.getMockList(id,pageNumber,pageSize);
    }

    @Override
    public Flowable<HttpResponse<ScoreVO>> postExerciseScoreBody(String userId, String homeworkId, String answer, String examId,String testId,String type) {
        return qBaseApis.postExerciseScoreBody(userId,homeworkId,answer,examId,testId,type);
    }

    @Override
    public Flowable<HttpResponse<TotalScoreVO>> getTotalScore(String userId) {
        return qBaseApis.getTotalScore(userId);
    }

    @Override
    public Flowable<HttpResponse<String>> uploadRecord(RequestBody userId, RequestBody examId, RequestBody homeworkId, RequestBody testId, RequestBody testType, RequestBody fileName, MultipartBody.Part fileData) {
        return qBaseApis.uploadRecord(userId,examId,homeworkId,testId,testType,fileName,fileData);
    }

}
