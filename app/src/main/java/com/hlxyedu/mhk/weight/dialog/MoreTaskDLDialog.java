package com.hlxyedu.mhk.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.annotations.DownloadGroup;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.processor.IHttpFileLenAdapter;
import com.arialyy.aria.core.task.DownloadGroupTask;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;
import com.arialyy.aria.util.CommonUtil;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.model.bean.ExamProgressVO;
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
import java.util.Map;

import static android.view.Window.FEATURE_NO_TITLE;

public class MoreTaskDLDialog extends Dialog {

    private String DOWNLOAD_URL;

    private Context context;
    private ProgressBar mProgressBar;
    private TextView mSize;
    private TextView mPercent;
    private long mTaskId = -1;

    // 下载 url 地址集合
    private List<String> downUrlLists = new ArrayList<>();

    // 保存到本地文件名字集合
    private List<String> examNameLists = new ArrayList<>();

    public MoreTaskDLDialog(@NonNull Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_download);

        initData();
        init();
    }

    private void initData() {
        List<ExamProgressVO> examProgressVOS = AppContext.getInstance().getExamProgressVOS();
        for (int i = 0; i < examProgressVOS.size(); i++) {
            String downloadPath = examProgressVOS.get(i).getZipPath();
            downUrlLists.add(ApiConstants.HOST + downloadPath);
        }

        for (int i = 0; i < downUrlLists.size(); i++) {
            String[] zipPaths = downUrlLists.get(i).split("/");
            //压缩包名字
            String zipName = zipPaths[zipPaths.length - 1];
            examNameLists.add(zipName);
        }

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

        mTaskId = Aria.download(this)
                .loadGroup(downUrlLists)
                .setDirPath(AppConstants.FILE_DOWNLOAD_PATH)
                .setSubFileName(examNameLists)
                .unknownSize()
                .option(getHttpOption())
                .ignoreFilePathOccupy()
                .create();

    }

    @DownloadGroup.onTaskPre
    public void onTaskPre(DownloadGroupTask task) {
        mSize.setText(CommonUtil.formatFileSize(task.getFileSize()));
    }

    @DownloadGroup.onTaskFail
    public void onTaskFail(DownloadGroupTask task) {
        ToastUtils.showShort("加载失败！");
        this.dismiss();
    }

    @DownloadGroup.onTaskRunning
    public void onTaskRunning(DownloadGroupTask task) {
       /* Log.e("hahaha======", "group running, p = "
                + task.getPercent()
                + ", speed = "
                + task.getConvertSpeed()
                + ", current_p = "
                + task.getCurrentProgress());*/

        mPercent.setText(task.getPercent() + "%");
        mProgressBar.setProgress(task.getPercent());
    }

    @DownloadGroup.onTaskComplete
    public void onTaskComplete(DownloadGroupTask task) {

        this.dismiss();

        AppContext.getInstance().setCurrentPos(0);
        if (examNameLists.get(0).contains("TL")) {
            context.startActivity(TestListeningActivity.newInstance(context, "考试"));
        } else if (examNameLists.get(0).contains("KY") || examNameLists.get(0).contains("LD")) {
            context.startActivity(TestSpeakActivity.newInstance(context, "考试"));
        } else if (examNameLists.get(0).contains("YD")) {
            context.startActivity(TestReadActivity.newInstance(context, "考试"));
        } else if (examNameLists.get(0).contains("SM")) {
            context.startActivity(TestBookActivity.newInstance(context, "考试"));
        } else if (examNameLists.get(0).contains("ZW")) {
            context.startActivity(TestTxtActivity.newInstance(context, "考试"));
        }
    }

    private HttpOption getHttpOption() {
        HttpOption option = new HttpOption();
        option.setFileLenAdapter(new HttpFileLenAdapter());
        return option;
    }

    static class HttpFileLenAdapter implements IHttpFileLenAdapter {
        @Override public long handleFileLen(Map<String, List<String>> headers) {

            List<String> sLength = headers.get("Content-Length");
            if (sLength == null || sLength.isEmpty()) {
                return -1;
            }
            String temp = sLength.get(0);

            return Long.parseLong(temp);
        }
    }

}
