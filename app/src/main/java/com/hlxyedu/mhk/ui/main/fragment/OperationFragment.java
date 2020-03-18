package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseActivity;
import com.hlxyedu.mhk.ui.main.adapter.OperationAdapter;
import com.hlxyedu.mhk.ui.main.contract.OperationContract;
import com.hlxyedu.mhk.ui.main.presenter.OperationPresenter;
import com.hlxyedu.mhk.ui.operation.activity.OperationSelectActivity;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.skyworth.rxqwelibrary.app.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class OperationFragment extends RootFragment<OperationPresenter> implements OperationContract.View, XBaseTopBarImp {


    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private OperationAdapter mAdapter;

    private List<OperationVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    private String hws = "A";// 作业完成状态,默认全部

    private List<String> examNames = new ArrayList<>();
    private List<Integer> posis = new ArrayList<>();


    public static OperationFragment newInstance() {
        Bundle args = new Bundle();

        OperationFragment fragment = new OperationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operation;
    }

    @Override
    public void onResume() {
        super.onResume();
        count = 1;
        pageSize = 20;
        mPresenter.getOperationList(mPresenter.getID(), count, pageSize,hws);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        xbaseTopbar.setxBaseTopBarImp(this);
        stateLoading();

        Aria.download(this).register();

        mAdapter = new OperationAdapter(R.layout.item_exercise, dataVOList);
        rlv.setLayoutManager(new LinearLayoutManager(mActivity));
        rlv.setAdapter(mAdapter);

        count = 1;
        if (!dataVOList.isEmpty()) {
            dataVOList.clear();
        }
        mPresenter.getOperationList(mPresenter.getID(), count, pageSize,hws);

        mAdapter.setPreLoadNumber(1);
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.getOperationList(mPresenter.getID(), ++count, pageSize,hws);
        }, rlv);
        mAdapter.setOnItemChildClickListener((adapter, view, position) ->
                startActivity(ExerciseActivity.newInstance(mActivity,"")));

    }

    @Override
    public void onSelect(String state) {
        if (StringUtils.isEmpty(state)){
            return;
        }
        hws = state;
        dataVOList.clear();
        mAdapter.notifyDataSetChanged();
        count = 1;
        mPresenter.getOperationList(mPresenter.getID(), count, pageSize,hws);
    }

    @Override
    public void onSuccess(List<OperationVO> operationListVOS) {
        if (!operationListVOS.isEmpty()) {
            dataVOList.addAll(operationListVOS);
            mAdapter.setNewData(dataVOList);
            if (operationListVOS.size() < pageSize) {
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
        Log.e("=============",p+"");

    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        for (int i = 0; i < examNames.size(); i++) {
            if (task.getTaskName().equals(examNames.get(i))){
                Button button = (Button) mAdapter.getViewByPosition(rlv,posis.get(i),R.id.positive_btn);
                button.setEnabled(true);
                ToastUtils.showShort("下载失败，重试");
                button.setText("做作业");
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
                button.setText("做作业");
//                downloadDialog.dismiss();
            }
        }
    }

    @Override
    public void responeError(String errorMsg) {
//        stateError();
    }

    @Override
    public void left() {

    }

    @Override
    public void right() {
        startActivity(OperationSelectActivity.newInstance(mActivity));
    }
}