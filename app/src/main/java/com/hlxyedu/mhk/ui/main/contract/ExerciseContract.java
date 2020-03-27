package com.hlxyedu.mhk.ui.main.contract;

import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.base.BaseView;

/**
 * Created by zhangguihua
 */
public interface ExerciseContract {

    interface View extends BaseView {
        //返回登陆结果
        void responeError(String errorMsg);

        void onSuccess(ExerciseListVO exerciseListVO);

        void onSelect(String questionType);

//        void download(int pos,Object downloadPath,String type,String examName,String examId);
        void download(DownLoadEvent downLoadEvent);

    }

    interface Presenter extends BasePresenter<View> {

        void getExamList(String examType, String id, int pageNum, int perpage, String version);

        String getID();

    }

}