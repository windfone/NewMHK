package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.ExerciseVO;
import com.hlxyedu.mhk.model.http.api.ApiConstants;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseActivity;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseSelectActivity;
import com.hlxyedu.mhk.ui.main.adapter.ExerciseAdapter;
import com.hlxyedu.mhk.ui.main.contract.ExerciseContract;
import com.hlxyedu.mhk.ui.main.presenter.ExercisePresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
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
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    private String examType;

    private List<String> examNames = new ArrayList<>();
    private List<Integer> posis = new ArrayList<>();

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
    public void download(int posi,String downloadPath,String examName) {
//        tvDownload.setEnabled(false);
//        iv_book_progress.setVisibility(android.view.View.VISIBLE);
//        Aria.download(this).register();
//        Aria.download(this)
//                .load(downloadPath)     //读取下载地址
//                .setFilePath(AppConstants.FILE_DOWNLOAD_PATH + examName) //设置文件保存的完整路径
//                .start();
        long taskId = Aria.download(this)
                .load(downloadPath)     //读取下载地址
                .setFilePath(AppConstants.FILE_DOWNLOAD_PATH + examName) //设置文件保存的完整路径
                .create();   //创建并启动下载

        examNames.add(examName);
        posis.add(posi);
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        for (int i = 0; i < examNames.size(); i++) {
            if (task.getTaskName().equals(examNames.get(i))){
                Button button = (Button) mAdapter.getViewByPosition(rlv,posis.get(i),R.id.positive_btn);
                button.setEnabled(true);
                button.setText("获取");
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
            }
        }
//        iv_book_progress.setVisibility(android.view.View.GONE);
//        tvDownload.setEnabled(true);
//        tvDownload.setText(R.string.dwonload_already);
//        String path = bookVO.getRname() + ".pdf";
//        startActivity(PDFReaderActivity.newInstance(this, path, bookVO.getRname()));
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