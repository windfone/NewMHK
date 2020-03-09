package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arialyy.aria.core.Aria;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ExerciseVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.model.http.api.ApiConstants;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseActivity;
import com.skyworth.rxqwelibrary.app.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends BaseQuickAdapter<ExerciseVO, BaseViewHolder> {

    private ArrayList<ExerciseVO> datas;

    public ExerciseAdapter(int layoutResId,
                           @Nullable List<ExerciseVO> data) {
        super(layoutResId, data);
        this.datas = (ArrayList<ExerciseVO>) data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExerciseVO item) {
        helper.setText(R.id.title_tv,item.getExamname())
              .setText(R.id.exercise_number_tv,item.getTimes()+"");

//        if (StringUtils.equals(item.getExamType(),"KY")){ //口语
//            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_oral_listening);
//        }else if (StringUtils.equals(item.getExamType(),"YD")){ //阅读
//
//        }else if (StringUtils.equals(item.getExamType(),"SM")){ //？
//
//        }else if (StringUtils.equals(item.getExamType(),"TL")){ //听力
//            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_hearing_2);
//        }else if (StringUtils.equals(item.getExamType(),"ZW")){ //作文
//
//        }

        String path = item.getExamname() + ".zip";
        if (FileUtils.isFileExists(AppConstants.FILE_DOWNLOAD_PATH + path)) {
            helper.setText(R.id.positive_btn,"开始练习");
        } else {
            helper.setText(R.id.positive_btn,"获取");
        }

        Button button = helper.getView(R.id.positive_btn);
        button.setOnClickListener(v -> {
            if (FileUtils.isFileExists(AppConstants.FILE_DOWNLOAD_PATH + path)) {
                mContext.startActivity(ExerciseActivity.newInstance(mContext, path));
            } else {
                button.setText("下载中...");
                button.setEnabled(false);
                RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER,helper.getLayoutPosition(),
                        ApiConstants.HOST + item.getZip_path(),path));
            }
        });

    }
}
