package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.ExerciseVO;
import com.hlxyedu.mhk.model.http.api.ApiConstants;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseActivity;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseSelectActivity;
import com.hlxyedu.mhk.ui.main.adapter.ExerciseAdapter;
import com.hlxyedu.mhk.ui.main.contract.ExerciseContract;
import com.hlxyedu.mhk.ui.main.presenter.ExercisePresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.app.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.skyworth.rxqwelibrary.app.AppConstants.DOWNLOAD_PATH;

/**
 * Created by zhangguihua
 */
public class ExerciseFragment extends RootFragment<ExercisePresenter> implements ExerciseContract.View, XBaseTopBarImp {


    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private ExerciseAdapter mAdapter;

    private List<ExerciseVO> dataVOList = new ArrayList<>();
    private int pageSize = 100;
    private int count = 1; // 当前页数;

    private String examType;

    private List<String> examNames = new ArrayList<>();
    private List<Integer> posis = new ArrayList<>();

    private DialogPlus downloadDialog;
    private TextView loadingProgressTv;

    public static ExerciseFragment newInstance() {
        Bundle args = new Bundle();

        ExerciseFragment fragment = new ExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exercise;
    }

    @Override
    public void onSelect(String questionType) {
        if (StringUtils.isEmpty(questionType)) {
            return;
        }
        examType = questionType;
        dataVOList.clear();
        mAdapter.notifyDataSetChanged();
        count = 1;
        mPresenter.getExamList(examType, mPresenter.getID(), count, pageSize, AppUtils.getAppVersionName());
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        count = 1; // 当前页数;
        pageSize = 20;
        mPresenter.getExamList(examType, mPresenter.getID(), count, pageSize, AppUtils.getAppVersionName());
    }

    @Override
    public void download(int posi,String downloadPath,String examName) {
//        showDownloadDialog();
        long taskId = Aria.download(this)
                .load(downloadPath)     //读取下载地址
                .setFilePath(AppConstants.FILE_DOWNLOAD_PATH + examName,true) //设置文件保存的完整路径
                .create();   //创建并启动下载

        examNames.add(examName);
        posis.add(posi);
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        int p = task.getPercent();	//任务进度百分比

    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        for (int i = 0; i < examNames.size(); i++) {
            if (task.getTaskName().equals(examNames.get(i))){
                Button button = (Button) mAdapter.getViewByPosition(rlv,posis.get(i),R.id.positive_btn);
                button.setEnabled(true);
                ToastUtils.showShort("下载失败，重试");
                button.setText("获取");
//                downloadDialog.dismiss();
            }
        }
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        for (int i = 0; i < examNames.size(); i++) {
            if (task.getTaskName().equals(examNames.get(i))){
                Button button = (Button) mAdapter.getViewByPosition(rlv,posis.get(i),R.id.positive_btn);
                button.setEnabled(true);
                button.setText("开始练习");
//                downloadDialog.dismiss();
            }
        }
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        xbaseTopbar.setxBaseTopBarImp(this);
        stateLoading();

        Aria.download(this).register();

        mAdapter = new ExerciseAdapter(R.layout.item_exercise, dataVOList);
        rlv.setLayoutManager(new LinearLayoutManager(mActivity));
        rlv.setAdapter(mAdapter);

        count = 1;
        if (!dataVOList.isEmpty()) {
            dataVOList.clear();
        }
        mPresenter.getExamList(examType, mPresenter.getID(), count, pageSize, AppUtils.getAppVersionName());

        mAdapter.setPreLoadNumber(1);
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.getExamList(examType, mPresenter.getID(), ++count, pageSize, AppUtils.getAppVersionName());
        }, rlv);
    }

    @Override
    public void onSuccess(ExerciseListVO exerciseListVO) {
        if (!exerciseListVO.getExam().isEmpty()) {
//            // 为了好测试做些筛选，之后去掉
//            for (int i = 0; i < exerciseListVO.getExam().size(); i++) {
//                if (exerciseListVO.getExam().get(i).getExamname().contains("书面")){
//                    dataVOList.add(exerciseListVO.getExam().get(i));
//                }
//            }
//            //
            dataVOList.addAll(exerciseListVO.getExam());
            mAdapter.setNewData(dataVOList);
            if (exerciseListVO.getExam().size() < pageSize) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
            stateMain();
        } else {
            if (count == 1) {
                stateEmpty("暂无内容");
            } else {
                mAdapter.loadMoreEnd();
            }
        }
    }

    /*//显示弹框
    public void showDownloadDialog() {
        downloadDialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.download_file_dialog))
                .setGravity(Gravity.CENTER)
                .setContentWidth(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
//                .setContentBackgroundResource(R.drawable.toast_bg)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                    }
                })
                .create();
        loadingProgressTv = (TextView) downloadDialog.findViewById(R.id.loading_progress_tv);
        loadingProgressTv.setText("6666");
        downloadDialog.show();
    }*/

    @Override
    public void responeError(String errorMsg) {
        stateError();
    }


    @Override
    public void left() {

    }

    @Override
    public void right() {
        startActivity(ExerciseSelectActivity.newInstance(mActivity));
    }
}