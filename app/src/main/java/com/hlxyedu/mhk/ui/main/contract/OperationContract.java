package com.hlxyedu.mhk.ui.main.contract;

import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

import java.util.List;

/**
 * Created by zhangguihua
 */
public interface OperationContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onSuccess(List<OperationVO> operationListVOS);

        void onSelect(String hws); // 作业完成状态

//        void download(int pos,Object downloadPath,String type,String examName,String examId,String homeworkId);
        void download(DownLoadEvent downLoadEvent);
    }

    interface Presenter extends BasePresenter<View> {

        void getOperationList(String userId,int pageNumber,int pageSize,String hws);

        String getID();

    }
}