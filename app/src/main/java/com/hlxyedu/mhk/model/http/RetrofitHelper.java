package com.hlxyedu.mhk.model.http;


import com.hlxyedu.mhk.model.http.api.ManageApis;
import com.hlxyedu.mhk.model.http.api.QBaseApis;

import javax.inject.Inject;

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


}
