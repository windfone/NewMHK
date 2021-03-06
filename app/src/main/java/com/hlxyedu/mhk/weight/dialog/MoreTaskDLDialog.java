package com.hlxyedu.mhk.weight.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.annotations.DownloadGroup;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.download.DownloadGroupEntity;
import com.arialyy.aria.core.processor.IHttpFileLenAdapter;
import com.arialyy.aria.core.task.DownloadGroupTask;
import com.arialyy.aria.util.CommonUtil;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ExamProgressVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.model.http.api.ApiConstants;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.hlxyedu.mhk.utils.PermissionSettingUtil;
import com.orhanobut.logger.Logger;
import com.skyworth.rxqwelibrary.app.AppConstants;
import com.tbruyelle.rxpermissions2.RxPermissions;

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

    // 权限相关
    private boolean READ_EXTERNAL_STORAGE;
    private boolean WRITE_EXTERNAL_STORAGE;
    private boolean RECORD_AUDIO;
    private boolean READ_PHONE_STATE;

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
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams dialogParams = getWindow().getAttributes();
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.22);
        dialogParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogParams.height = height;

        Aria.download(this).register();

        mPercent = findViewById(R.id.precent_tv);
        mSize = findViewById(R.id.size_tv);
        mProgressBar = findViewById(R.id.progressBar);

        findViewById(R.id.reload_tv).setOnClickListener(v -> {
//            int state = Aria.download(this).getDownloadEntity(mTaskId).getState();
            // Aria.download(this).getDownloadEntity(mTaskId).getState()
            // 任务存在
//            boolean b = Aria.download(this).taskExists(downUrlLists);
//            Aria.download(this).loadGroup(mTaskId).stop(); // 走下载完成方法，实际未完成
//            Aria.download(this).loadGroup(mTaskId).reTry();// 会走stop方法， 暂停住
//            Aria.download(this).loadGroup(mTaskId).reStart();// 没效果,不走方法
//            Aria.download(this).loadGroup(mTaskId).resume();
            Aria.download(this).loadGroup(mTaskId).cancel(true);
        });

        // 下载之前先检测权限，如果没有存储权限则下载不了
        checkPermissions();

    }

    @DownloadGroup.onTaskPre
    public void onTaskPre(DownloadGroupTask task) {
        Log.e("=============","onTaskPre");
        Logger.d("获取到文件大小" + CommonUtil.formatFileSize(task.getFileSize()));
        mSize.setText(CommonUtil.formatFileSize(task.getFileSize()));
    }

    @DownloadGroup.onTaskStart
    public void onTaskStart(DownloadGroupTask task) {
        Log.e("=============","onTaskStart");
        Logger.d("下载开始");
    }

    @DownloadGroup.onTaskCancel
    public void onTaskCancel(DownloadGroupTask task) {
        Aria.download(this).unRegister();
        Logger.d("点击了重新下载");
        dismiss();
        RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_EXAM));
    }

    @DownloadGroup.onTaskStop
    public void onTaskStop(DownloadGroupTask task) {
        Log.e("=============","onTaskStop");
        Logger.d("下载暂停");
        Aria.download(this).loadGroup(mTaskId).cancel(true);
        this.dismiss();
    }

    @DownloadGroup.onTaskFail
    public void onTaskFail(DownloadGroupTask task) {
        Log.e("=============","onTaskFail");
        ToastUtils.showShort("加载失败！请重试");
        Logger.d("文件下载失败！");
        Aria.download(this).loadGroup(mTaskId).cancel(true);
        Aria.download(this).unRegister();
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
        Logger.d("文件下载完成！");
        Aria.download(this).loadGroup(mTaskId).cancel();
        Aria.download(this).unRegister();
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

    @SuppressLint("CheckResult")
    public void checkPermissions() {
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        if (permission.name.equals("android.permission.READ_EXTERNAL_STORAGE")) {
                            READ_EXTERNAL_STORAGE = true;
                        } else if (permission.name.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                            WRITE_EXTERNAL_STORAGE = true;
                        } else if (permission.name.equals("android.permission.RECORD_AUDIO")) {
                            RECORD_AUDIO = true;
                        } else if (permission.name.equals("android.permission.READ_PHONE_STATE")) {
                            READ_PHONE_STATE = true;
                        }
                        // 权限同意,而且是权限全部同意才下载，这样做 防止只同意存储权限可以下载，但是不能录音，到口语 题型的时候不能录音还得再次申请
                        if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE && RECORD_AUDIO && READ_PHONE_STATE) {

                            mTaskId = Aria.download(this)
//                                    .setMaxSpeed(0) // 0表示不限速
                                    .loadGroup(downUrlLists)
                                    .setDirPath(AppConstants.FILE_DOWNLOAD_PATH)
                                    .setSubFileName(examNameLists)
                                    .unknownSize()
                                    .option(getHttpOption())
                                    .ignoreFilePathOccupy()
                                    .create();
                            Log.e("=============",mTaskId+"");
                        }
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        // 禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                        ToastUtils.showShort("下载失败,需同意下载权限");
                        dismiss();
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        // 禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        // 需要到 设置里面 手动打开
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("权限申请");
                        builder.setMessage("需要同意相机、录音、存储、获取手机状态信息权限才能正常使用哦");
                        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionSettingUtil.gotoPermission(context);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                });
    }

    static class HttpFileLenAdapter implements IHttpFileLenAdapter {
        @Override
        public long handleFileLen(Map<String, List<String>> headers) {

            List<String> sLength = headers.get("Content-Length");
            if (sLength == null || sLength.isEmpty()) {
                return -1;
            }
            String temp = sLength.get(0);

            return Long.parseLong(temp);
        }
    }

}
