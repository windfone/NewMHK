package com.hlxyedu.mhk.weight.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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
import com.hlxyedu.mhk.utils.PermissionSettingUtil;
import com.skyworth.rxqwelibrary.app.AppConstants;
import com.tbruyelle.rxpermissions2.RxPermissions;

import static android.view.Window.FEATURE_NO_TITLE;

public class DownLoadDialog extends Dialog {

    private String DOWNLOAD_URL;

    private Context context;
    private DownLoadEvent s;
    private ProgressBar mProgressBar;
    private TextView mSize;
    private TextView mPercent;
    private long mTaskId = -1;

    // 权限相关
    private boolean READ_EXTERNAL_STORAGE;
    private boolean WRITE_EXTERNAL_STORAGE;
    private boolean RECORD_AUDIO;
    private boolean READ_PHONE_STATE;

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
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams dialogParams = getWindow().getAttributes();
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.17);
        dialogParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogParams.height = height;

        Aria.download(this).register();

        mPercent = findViewById(R.id.precent_tv);
        mSize = findViewById(R.id.size_tv);
        mProgressBar = findViewById(R.id.progressBar);

        // 下载之前先检测权限，如果没有存储权限则下载不了
        checkPermissions();

    }

    @Download.onTaskPre
    public void onTaskPre(DownloadTask task) {
        mSize.setText(CommonUtil.formatFileSize(task.getFileSize()));
    }


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
                                    .load(DOWNLOAD_URL)
                                    .setFilePath(AppConstants.FILE_DOWNLOAD_PATH + s.getExamName(), true)
                                    .create();
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
                        builder.setMessage("需要同意录音、存储、获取手机状态信息权限才能正常使用哦");
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

}
