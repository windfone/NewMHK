package com.hlxyedu.mhk.model.http;

import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;

/**
 * 作者：skyworth on 2017/7/11 09:56
 * 邮箱：dqwei@iflytek.com
 */

public interface HttpHelper {

//    Flowable<HttpResponse<UserVO>> getLoginBody(String uname, String pwd);

      Flowable<HttpResponse<UserVO>> postLoginBody(String mobile,String password);

      Flowable<HttpResponse<String>> postModifyPsdBody(String mobile,String password,String idNum);

}
