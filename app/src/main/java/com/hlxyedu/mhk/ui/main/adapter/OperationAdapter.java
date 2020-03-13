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
import com.hlxyedu.mhk.ui.exercise.activity.TestListeningActivity;
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
        switch (item.getState()){
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
              .setText(R.id.title_tv,item.getHomeworkName())
              .setText(R.id.author_tv,item.getTeacherName()+"");

        if (StringUtils.equals(item.getExamType(),"KY")){ //口语
            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_oral_listening);
        }else if (StringUtils.equals(item.getExamType(),"YD")){ //阅读

        }else if (StringUtils.equals(item.getExamType(),"SM")){ //书面

        }else if (StringUtils.equals(item.getExamType(),"TL")){ //听力
            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_hearing_2);
        }else if (StringUtils.equals(item.getExamType(),"ZW")){ //作文

        }else if (StringUtils.equals(item.getExamType(),"ZH")){ //全真试题（综合）

        }

        String[] zipPaths = item.getZipPath().split("/");
        //压缩包名字
        String zipName = zipPaths[zipPaths.length-1];
//        String path = item.getExamname() + ".zip";
        // 可以做作业的状态

        Button button = helper.getView(R.id.positive_btn);
        if (StringUtils.equals(item.getState(),"1")){
            button.setEnabled(true);
            button.setOnClickListener(v -> {
                if (FileUtils.isFileExists(AppConstants.FILE_DOWNLOAD_PATH + zipName)) {
                    if(StringUtils.equals(item.getExamType(),"TL")){
                        mContext.startActivity(TestListeningActivity.newInstance(mContext, AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getExamId(),item.getId()));
                    }
//                mContext.startActivity(ExerciseActivity.newInstance(mContext, path));
                } else {
                    button.setText("下载中...");
                    button.setEnabled(false);
                    RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_OPERATION,helper.getLayoutPosition(),
                            ApiConstants.HOST + item.getZipPath(),zipName));
                }
            });

        }else {
            button.setEnabled(false);
        }

    }

}
