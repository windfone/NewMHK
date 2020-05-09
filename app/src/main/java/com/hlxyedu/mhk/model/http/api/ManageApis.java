package com.hlxyedu.mhk.model.http.api;

import com.hlxyedu.mhk.model.bean.FileUrlVO;
import com.hlxyedu.mhk.model.bean.VersionVO;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ManageApis {

    /**
     * manager
     * APP_ID
     */
    // 测试环境
    public static final String APP_ID = "00000000000000012001";
    // 生产环境
    public static String HOST = "https://www.skyworthedu.com.cn/api/";



    @POST("common/base-server/project-version/check-new-version")
    Flowable<VersionVO> getNewVersion(@Body RequestBody requestBody);

    @GET("common/cloud-file-server/file/get-file-url")
    Flowable<FileUrlVO> getFileUrl(@Query("fid") String fid);
}
