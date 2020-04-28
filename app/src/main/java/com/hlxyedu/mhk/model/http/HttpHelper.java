package com.hlxyedu.mhk.model.http;

import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.TotalScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：skyworth on 2017/7/11 09:56
 * 邮箱：dqwei@iflytek.com
 */

public interface HttpHelper {

//    Flowable<HttpResponse<UserVO>> getLoginBody(String uname, String pwd);

    Flowable<HttpResponse<UserVO>> postLoginBody(String mobile, String password);

    Flowable<HttpResponse<String>> getNoteInfo();

    Flowable<HttpResponse<UserVO>> getUserInfo();

    Flowable<HttpResponse<String>> postModifyPsdBody(String mobile, String password, String idNum);

    Flowable<HttpResponse<ExerciseListVO>> getExamList(String examType, String id, int pageNum, int perpage, String version);

    Flowable<HttpResponse<List<OperationVO>>> getOperationList(String userId, int pageNumber, int pageSize, String hws);

    Flowable<HttpResponse<List<ExamVO>>> getMockList(String id, int pageNumber, int pageSize);

    Flowable<HttpResponse<ScoreVO>> postExerciseScoreBody(String id, String homeworkId, String answer, String examId, String testId, String type);

    Flowable<HttpResponse<TotalScoreVO>> getTotalScore(String userId);

    Flowable<HttpResponse<String>> uploadRecord(RequestBody userId,
                                                RequestBody examId,
                                                RequestBody homeworkId,
                                                RequestBody testId,
                                                RequestBody testType,
                                                RequestBody fileName,
                                                MultipartBody.Part fileData);

    Flowable<HttpResponse<String>> uploadVideo(RequestBody userId,
                                               RequestBody examId,
                                               RequestBody testId,
                                               RequestBody type,
                                               RequestBody fileName,
                                               MultipartBody.Part fileData);
}
