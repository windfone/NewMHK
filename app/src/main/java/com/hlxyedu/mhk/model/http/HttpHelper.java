package com.hlxyedu.mhk.model.http;

import com.hlxyedu.mhk.model.bean.ExamListVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * 作者：skyworth on 2017/7/11 09:56
 * 邮箱：dqwei@iflytek.com
 */

public interface HttpHelper {

//    Flowable<HttpResponse<UserVO>> getLoginBody(String uname, String pwd);

      Flowable<HttpResponse<UserVO>> postLoginBody(String mobile,String password);

      Flowable<HttpResponse<String>> postModifyPsdBody(String mobile,String password,String idNum);

      Flowable<HttpResponse<ExamListVO>> getExamList(String examType, String id, int pageNum, int perpage, String version);

}
