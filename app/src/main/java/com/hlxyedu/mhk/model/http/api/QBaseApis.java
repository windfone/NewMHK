package com.hlxyedu.mhk.model.http.api;

import com.hlxyedu.mhk.model.bean.ExamVO;
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
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 作者：skyworth on 2017/9/7 09:51
 * 邮箱：dqwei@iflytek.com
 * <p>
 * http://www.jianshu.com/p/73216939806a  Retrofit2 使用说明
 */

public interface QBaseApis {

    /*//* 修改个人信息 *//*
    @FormUrlEncoded
    @PUT("json/login")
    Flowable<HttpResponse<UserVO>> putModifyInfo(@Field("id") String id, @Field("pwd") String pwd
            , @Field("tname") String tname, @Field("tel") String tel, @Field("addr") String addr);*/

    // 用户登录
//    @GET("user/login")
//    Flowable<HttpResponse<UserVO>> getLoginBody(@Query("username") String username, @Query("password") String password);

    // 用户登录
    @FormUrlEncoded
    @POST("login/loginm.do")
    Flowable<HttpResponse<UserVO>> postLoginBody(@Field("mobile") String mobile, @Field("password") String password);

    // 找回密码（重置密码）
    @FormUrlEncoded
    @POST("login/forgotPwordm.do")
    Flowable<HttpResponse<String>> postModifyPsdBody(@Field("mobile") String mobile, @Field("password") String password, @Field("idNum") String idNum);

    // 获取练习列表
    @GET("phone/getExamList.do")
    Flowable<HttpResponse<ExerciseListVO>> getExamList(@Query("examType") String examType, @Query("id") String id, @Query("pageNum") int pageNum, @Query("perpage") int perpage, @Query("version") String version);

    // 获取作业列表
    @GET("phone/appAssig.do")
    Flowable<HttpResponse<List<OperationVO>>> getOperationList(@Query("userId") String userId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize, @Query("hws") String hws);

    // 获取考试列表
    @GET("phone/getMockList.do")
    Flowable<HttpResponse<List<ExamVO>>> getMockList(@Query("id") String id, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    // 提交答案获取成绩
    @FormUrlEncoded
    @POST("phone/calExamScore.do")      //id 为userId，此处比较混乱，特注明
    Flowable<HttpResponse<ScoreVO>> postExerciseScoreBody(@Field("id") String userId, @Field("homeworkId") String homeworkId, @Field("answer") String answer, @Field("examId") String examId, @Field("testId") String testId, @Field("type") String type);


    @Multipart
    @POST("phone/uploadRec.do")
    Flowable<HttpResponse<String>> uploadRecord(@Part("id") RequestBody userId,
                                              @Part("examId") RequestBody examId,
                                              @Part("homeworkId") RequestBody homeworkId,
                                              @Part("testId") RequestBody testId,
                                              @Part("type") RequestBody type,
                                              @Part("fileName") RequestBody fileName,
                                              @Part MultipartBody.Part fileData);
}


