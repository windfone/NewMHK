package com.hlxyedu.mhk.weight.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.hlxyedu.mhk.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.app.AppConstants;

/**
 * Created by LiXiang on 16/8/8.
 */
public class DownloadView {

    protected TextView loadingProgressTv;
    //下载文件所需要代码
    protected DialogPlus downloadDialog;

    private Context context;

    //下载的压缩包名字（如 AAA.zip）
    private String examName;

    // 下载路径
    private String downloadPath;


    public DownloadView(Context context,String downloadPath,String examName) {
        this.context = context;
        this.examName = examName;
        this.downloadPath = downloadPath;

    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        int p = task.getPercent();	//任务进度百分比
        loadingProgressTv.setText(p);
    }


    //显示弹框
    public void showDownloadDialog() {
        downloadDialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.download_file_dialog))
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
//                .setContentBackgroundResource(R.drawable.toast_bg)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                    }
                })
                .create();
        loadingProgressTv = (TextView) downloadDialog.findViewById(R.id.loading_progress_tv);

        downloadDialog.show();
    }


}
