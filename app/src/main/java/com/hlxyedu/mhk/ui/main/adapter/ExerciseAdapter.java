package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ExerciseVO;
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

        if (item.getExamname().contains("口语")) { //口语
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_speak);
        } else if (item.getExamname().contains("阅读")) { //阅读
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_read);
        } else if (item.getExamname().contains("书面")) { //书面
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_book);
        } else if (item.getExamname().contains("听力")) { //听力
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_listening);
        } else if (item.getExamname().contains("作文")) { //作文
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_txt);
        } else if (item.getExamname().contains("综合")) { //全真试题（综合）
            helper.setImageResource(R.id.question_type_iv, R.drawable.icon_zh);
        }

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
                if(item.getExamname().contains("听力"))
                {
                    mContext.startActivity(TestListeningActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                }else if (item.getExamname().contains("口语"))
                {
                    //最后一个参数为 item.getId() 指的是examId
                    mContext.startActivity(TestSpeakActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                }else if (item.getExamname().contains("阅读"))
                {
                    //最后一个参数为 item.getId() 指的是examId
                    mContext.startActivity(TestReadActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                } else if (item.getExamname().contains("书面"))
                {
                    //最后一个参数为 item.getId() 指的是examId
                    mContext.startActivity(TestBookActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                } else if (item.getExamname().contains("作文"))
                {
                    //最后一个参数为 item.getId() 指的是examId
                    mContext.startActivity(TestTxtActivity.newInstance(mContext, "练习",AppConstants.FILE_DOWNLOAD_PATH + zipName,zipName,item.getId()));
                }
            } else {
                button.setText("下载中...");
                button.setEnabled(false);
                RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_EXERCISE,helper.getLayoutPosition(),
                        ApiConstants.HOST + item.getZip_path(),zipName));
            }
        });

    }
}
