package com.hlxyedu.mhk.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.CommonUtil;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.skyworth.rxqwelibrary.app.AppConstants;

import static android.view.Window.FEATURE_NO_TITLE;

public class DownLoadDialog extends Dialog {

    private String DOWNLOAD_URL;

    private Context context;
    private DownLoadEvent s;
    private ProgressBar mProgressBar;
    private TextView mSize;
    private TextView mPercent;
    private long mTaskId = -1;

    public DownLoadDialog(@NonNull Context context, DownLoadEvent s) {
        super(context);
        this.context = context;
        this.s = s;

        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_download);

        initData();
        init();
    }

    private void initData() {
        DOWNLOAD_URL = (String) s.getDownloadPath();
    }

    private void init() {
        this.setCancelable(false);
        WindowManager.LayoutParams dialogParams = getWindow().getAttributes();
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.1);
        dialogParams.width = width;
        dialogParams.height = height;

        Aria.download(this).register();

        mPercent = findViewById(R.id.precent_tv);
        mSize = findViewById(R.id.size_tv);
        mProgressBar = findViewById(R.id.progressBar);

        download();

    }

    private void download() {
        mTaskId = Aria.download(this)
                .load(DOWNLOAD_URL)
                .setFilePath(AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), true)
                .create();
    }

    @Download.onTaskPre
    public void onTaskPre(DownloadTask task) {
        mSize.setText(CommonUtil.formatFileSize(task.getFileSize()));
//        setBtState(false);
    }

    /*@Download.onTaskStop
    public void onTaskStop(DownloadTask task) {
        setBtState(true);
        mSpeed.setText(task.getConvertSpeed());
    }*/

    /*@Download.onTaskCancel
    public void onTaskCancel(DownloadTask task) {
        setBtState(true);
        mPb.setProgress(0);
        mSpeed.setText(task.getConvertSpeed());
    }*/

    @Download.onTaskFail
    public void onTaskFail(DownloadTask task) {
        ToastUtils.showShort("加载失败！");
        this.dismiss();
    }

    @Download.onTaskRunning
    public void onTaskRunning(DownloadTask task) {
        if (task.getKey().equals(DOWNLOAD_URL)) {
            mPercent.setText(task.getPercent() + "%");
            mProgressBar.setProgress(task.getPercent());
//            mSpeed.setText(task.getConvertSpeed());
        }
    }

    @Download.onTaskComplete
    public void onTaskComplete(DownloadTask task) {
        if (task.getKey().equals(DOWNLOAD_URL)) {
            mProgressBar.setProgress(100);
//            mSpeed.setText(0+"");
        }

        /**
         *   从练习页面跳转
         */
        if (s.getType().equals(DownLoadEvent.DOWNLOAD_PAPER_EXERCISE)) {

            if (s.getExamType().contains("听力")) {
                context.startActivity(TestListeningActivity.newInstance(context, "练习", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId()));
            } else if (s.getExamType().contains("口语") || s.getExamType().contains("朗读")) {
                //最后一个参数为 s.getExamId() 指的是examId
                context.startActivity(TestSpeakActivity.newInstance(context, "练习", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId()));
            } else if (s.getExamType().contains("阅读")) {
                //最后一个参数为 s.getExamId() 指的是examId
                context.startActivity(TestReadActivity.newInstance(context, "练习", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId()));
            } else if (s.getExamType().contains("书面")) {
                //最后一个参数为 s.getExamId() 指的是examId
                context.startActivity(TestBookActivity.newInstance(context, "练习", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId()));
            } else if (s.getExamType().contains("作文")) {
                //最后一个参数为 s.getExamId() 指的是examId
                context.startActivity(TestTxtActivity.newInstance(context, "练习", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId()));
            }

        }
        /**
         *   从作业页面跳转
         */
        else if (s.getType().equals(DownLoadEvent.DOWNLOAD_PAPER_OPERATION)) {

            if (StringUtils.equals(s.getExamType(), "TL")) {
                context.startActivity(TestListeningActivity.newInstance(context, "作业", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId(), s.getHomeworkId(), "homeWork"));
            } else if (StringUtils.equals(s.getExamType(), "KY")) {
                //最后一个参数为 s.getExamId() 指的是homeworkId;   s.getExamId()是试卷id ;    type = homeWork
                context.startActivity(TestSpeakActivity.newInstance(context, "作业", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId(), s.getHomeworkId(), "homeWork"));
            } else if (StringUtils.equals(s.getExamType(), "YD")) {
                //最后一个参数为 s.getExamId() 指的是homeworkId;   s.getExamId()是试卷id ;
                context.startActivity(TestReadActivity.newInstance(context, "作业", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId(), s.getHomeworkId(), "homeWork"));
            } else if (StringUtils.equals(s.getExamType(), "SM")) {
                //最后一个参数为 s.getExamId() 指的是homeworkId;   s.getExamId()是试卷id ;
                context.startActivity(TestBookActivity.newInstance(context, "作业", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId(), s.getHomeworkId(), "homeWork"));
            } else if (StringUtils.equals(s.getExamType(), "ZW")) {
                //最后一个参数为 s.getExamId() 指的是homeworkId;   s.getExamId()是试卷id ;
                context.startActivity(TestTxtActivity.newInstance(context, "作业", AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), s.getExamName(), s.getExamId(), s.getHomeworkId(), "homeWork"));
            }

        }

        this.dismiss();
    }


}
