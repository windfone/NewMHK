package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.model.http.api.ApiConstants;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.skyworth.rxqwelibrary.app.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class OperationAdapter extends BaseQuickAdapter<OperationVO, BaseViewHolder> {

    private ArrayList<OperationVO> datas;

    public OperationAdapter(int layoutResId,
                            @Nullable List<OperationVO> data) {
        super(layoutResId, data);
        this.datas = (ArrayList<OperationVO>) data;
    }

    @Override
    protected void convert(BaseViewHolder helper, OperationVO item) {
        switch (item.getState()) {
            case "1":
                // 规定做作业时间之前仍然可以做
                helper.setText(R.id.positive_btn, "做作业").addOnClickListener(R.id.positive_btn);
                break;
            case "2":
                // 布置的作业到时间之后仍未完成(不能点击)
                helper.setText(R.id.positive_btn, "未完成").addOnClickListener(R.id.positive_btn);
                break;
            case "3":
            case "4":
                // 已完成的作业(不能点击)
                helper.setText(R.id.positive_btn, "已完成").addOnClickListener(R.id.positive_btn);
                break;
        }

        helper.addOnClickListener(R.id.positive_btn)
                .setText(R.id.title_tv, item.getHomeworkName())
                .setText(R.id.author_tv, item.getTeacherName() + "");

        if (StringUtils.equals(item.getExamType(), "KY")) { //口语
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_speak);
        } else if (StringUtils.equals(item.getExamType(), "YD")) { //阅读
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_read);
        } else if (StringUtils.equals(item.getExamType(), "SM")) { //书面
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_book);
        } else if (StringUtils.equals(item.getExamType(), "TL")) { //听力
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_listening);
        } else if (StringUtils.equals(item.getExamType(), "ZW")) { //作文
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_txt);
        } else if (StringUtils.equals(item.getExamType(), "ZH")) { //全真试题（综合）
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_zh);
        }

        String[] zipPaths = item.getZipPath().split("/");
        //压缩包名字
        String zipName = zipPaths[zipPaths.length - 1];
//        String path = item.getExamname() + ".zip";
        // 可以做作业的状态

        Button button = helper.getView(R.id.positive_btn);
        if (StringUtils.equals(item.getState(), "1")) {
            button.setEnabled(true);
            button.setOnClickListener(v -> {
                if (FileUtils.isFileExists(AppConstants.FILE_DOWNLOAD_PATH + zipName)) {
                    if (StringUtils.equals(item.getExamType(), "TL")) {
                        mContext.startActivity(TestListeningActivity.newInstance(mContext, "作业", AppConstants.FILE_DOWNLOAD_PATH + zipName, zipName, item.getExamId(), item.getId()));
                    } else if (StringUtils.equals(item.getExamType(), "KY")) {
                        //最后一个参数为 item.getId() 指的是homeworkId;   item.getExamId()是试卷id ;    type = homeWork
                        mContext.startActivity(TestSpeakActivity.newInstance(mContext, "作业", AppConstants.FILE_DOWNLOAD_PATH + zipName, zipName, item.getExamId(), item.getId(), "homeWork"));
                    } else if (StringUtils.equals(item.getExamType(), "YD")) {
                        //最后一个参数为 item.getId() 指的是homeworkId;   item.getExamId()是试卷id ;
                        mContext.startActivity(TestReadActivity.newInstance(mContext, "作业", AppConstants.FILE_DOWNLOAD_PATH + zipName, zipName, item.getExamId(), item.getId()));
                    } else if (StringUtils.equals(item.getExamType(), "SM")) {
                        //最后一个参数为 item.getId() 指的是homeworkId;   item.getExamId()是试卷id ;
                        mContext.startActivity(TestBookActivity.newInstance(mContext, "作业", AppConstants.FILE_DOWNLOAD_PATH + zipName, zipName, item.getExamId(), item.getId()));
                    } else if (StringUtils.equals(item.getExamType(), "ZW")) {
                        //最后一个参数为 item.getId() 指的是homeworkId;   item.getExamId()是试卷id ;
                        mContext.startActivity(TestTxtActivity.newInstance(mContext, "作业", AppConstants.FILE_DOWNLOAD_PATH + zipName, zipName, item.getExamId(), item.getId()));
                    }
//                mContext.startActivity(ExerciseActivity.newInstance(mContext, path));
                } else {
                    button.setText("下载中...");
                    button.setEnabled(false);
                    RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_OPERATION, helper.getLayoutPosition(),
                            ApiConstants.HOST + item.getZipPath(), zipName));
                }
            });

        } else {
            button.setEnabled(false);
        }

    }

}
