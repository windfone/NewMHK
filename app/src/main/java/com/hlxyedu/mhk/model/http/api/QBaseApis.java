package com.hlxyedu.mhk.model.http.api;

/**
 * 作者：skyworth on 2017/9/7 09:51
 * 邮箱：dqwei@iflytek.com
 * <p>
 * http://www.jianshu.com/p/73216939806a  Retrofit2 使用说明
 */

public interface QBaseApis {

    /*@GET("json/login")
    Flowable<HttpResponse<UserVO>> getLoginBody(@Query("uname") String uname, @Query("pwd") String pwd);

    @FormUrlEncoded
    @POST("json/login")
    Flowable<HttpResponse<RegisterVO>> postRegisterBody(@Field("uname") String uname, @Field("pwd") String pwd, @Field("tname") String tname);

    //获取教材列表
    @FormUrlEncoded
    @POST("json")
    Flowable<HttpResponse<List<BookVO>>> getBookList(@Field("uid") String uid, @Field("type") String type);

    //获取会员书架
    @GET("json/user")
    Flowable<HttpResponse<List<BookShelfVO>>> getBookShelfList(@Query("uid") String uid);

    *//* 修改个人信息 *//*
    @FormUrlEncoded
    @PUT("json/login")
    Flowable<HttpResponse<UserVO>> putModifyInfo(@Field("id") String id, @Field("pwd") String pwd
            , @Field("tname") String tname, @Field("tel") String tel, @Field("addr") String addr);*/

    // 用户登录
//    @GET("user/login")
//    Flowable<HttpResponse<UserVO>> getLoginBody(@Query("username") String username, @Query("password") String password);

}


