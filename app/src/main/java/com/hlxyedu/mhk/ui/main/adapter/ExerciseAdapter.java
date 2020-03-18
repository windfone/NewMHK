package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ExerciseVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.model.http.api.ApiConstants;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
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

        String[] zipPaths = item.getZip_path().split("/");
        //压缩包名字
        String zipName = zipPaths[zipPaths.length-1];
//        String path = item.getExamname() + ".zip";
        if (FileUtils.isFileExists(AppConstants.FILE_DOWNLOAD_PATH + zipName)) {
            helper.setText(R.id.positive_btn,"开始练习");
        } else {
            helper.setText(R.id.positive_btn,"获取");
        }

        Button button = helper.getView(R.id.positive_btn);
        button.setOnClickListener(v -> {
            if (FileUtils.isFileExists(AppConstants.FILE_DOWNLOAD_PATH + zipName)) {
                if(item.getExamname().contains("听力")){
                    mContext.startActivity(TestListeningActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                }else if (item.getExamname().contains("口语")){
                    //最后一个参数为 item.getId() 指的是examId
                    mContext.startActivity(TestSpeakActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                }
//                mContext.startActivity(ExerciseActivity.newInstance(mContext, path));
            } else {
                button.setText("下载中...");
                button.setEnabled(false);
                RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_EXERCISE,helper.getLayoutPosition(),
                        ApiConstants.HOST + item.getZip_path(),zipName));
            }
        });

    }
}
