package com.hlxyedu.mhk.model.http;

import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.Part;

/**
 * 作者：skyworth on 2017/7/11 09:56
 * 邮箱：dqwei@iflytek.com
 */

public interface HttpHelper {

//    Flowable<HttpResponse<UserVO>> getLoginBody(String uname, String pwd);

      Flowable<HttpResponse<UserVO>> postLoginBody(String mobile,String password);

      Flowable<HttpResponse<String>> postModifyPsdBody(String mobile,String password,String idNum);

      Flowable<HttpResponse<ExerciseListVO>> getExamList(String examType, String id, int pageNum, int perpage, String version);

      Flowable<HttpResponse<List<OperationVO>>> getOperationList(String userId, int pageNumber, int pageSize, String hws);

      Flowable<HttpResponse<ScoreVO>> postExerciseScoreBody(String id, String homeworkId, String answer, String examId);

      Flowable<HttpResponse<String>> uploadRecord(RequestBody userId,
                                                  RequestBody examId,
                                                  RequestBody homeworkId,
                                                  RequestBody testId,
                                                  RequestBody testType,
                                                  RequestBody fileName,
                                                  MultipartBody.Part fileData);
}
