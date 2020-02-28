package com.hlxyedu.mhk.model.http;


import com.hlxyedu.mhk.model.bean.ExamListVO;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.http.api.ManageApis;
import com.hlxyedu.mhk.model.http.api.QBaseApis;
import com.hlxyedu.mhk.model.http.response.HttpResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;

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
    public Flowable<HttpResponse<ExamListVO>> getExamList(String examType, String id, int pageNum, int perpage, String version) {
        return qBaseApis.getExamList(examType,id,pageNum,perpage,version);
    }
}
