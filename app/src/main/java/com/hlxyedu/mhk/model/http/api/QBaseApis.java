package com.hlxyedu.mhk.model.http.api;

import com.hlxyedu.mhk.model.bean.ExamListVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    // 获取习题列表
    @GET("phone/getExamList.do")
    Flowable<HttpResponse<ExamListVO>> getExamList(@Query("examType") String examType, @Query("id") String id, @Query("pageNum") int pageNum, @Query("perpage") int perpage, @Query("version")String version);

}


