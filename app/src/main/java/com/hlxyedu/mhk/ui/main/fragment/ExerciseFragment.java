package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.ExerciseListVO;
import com.hlxyedu.mhk.model.bean.ExerciseVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.ui.main.adapter.ExerciseAdapter;
import com.hlxyedu.mhk.ui.main.contract.ExerciseContract;
import com.hlxyedu.mhk.ui.main.presenter.ExercisePresenter;
import com.hlxyedu.mhk.ui.select.activity.ExerciseSelectActivity;
import com.hlxyedu.mhk.weight.MyLinearLayoutManager;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.hlxyedu.mhk.weight.dialog.DownLoadDialog;
import com.hlxyedu.mhk.weight.listener.DoubleClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class ExerciseFragment extends RootFragment<ExercisePresenter> implements ExerciseContract.View, XBaseTopBarImp {


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private ExerciseAdapter mAdapter;

    private List<ExerciseVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    private String examType;

    private int clickPos;

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
    public void getClickPos(int pos) {
        clickPos = pos;
    }

    @Override
    public void examFinish() {
        int times = dataVOList.get(clickPos).getTimes();
        dataVOList.get(clickPos).setTimes(++times);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelect(String questionType) {
        if (StringUtils.isEmpty(questionType)) {
            return;
        }
        // 选择了全部
        if (questionType.equals("1")) {
            questionType = "";
        }
        examType = questionType;
        dataVOList.clear();
        mAdapter.notifyDataSetChanged();
        count = 1;
        mPresenter.getExamList(examType, mPresenter.getID(), count, pageSize, AppUtils.getAppVersionName());
    }

/*    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        dataVOList.clear();
        count = 1; // 当前页数;
        pageSize = 20;
        mPresenter.getExamList(examType, mPresenter.getID(), count, pageSize, AppUtils.getAppVersionName());
    }*/

    @Override
    public void download(DownLoadEvent s) {
        DownLoadDialog downloadDialog = new DownLoadDialog(mActivity, s);
        downloadDialog.show();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        xbaseTopbar.setxBaseTopBarImp(this);
        stateLoading();

        mAdapter = new ExerciseAdapter(R.layout.item_exercise, dataVOList);
        rlv.setLayoutManager(new MyLinearLayoutManager(mActivity));
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

        // 双击标题栏 返回列表顶部
        xbaseTopbar.setOnClickListener(new DoubleClickListener() {
            @Override
            protected void onDoubleClick(View v) {
                rlv.smoothScrollToPosition(0);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                count = 1;
                dataVOList.clear();
                mPresenter.getExamList(examType, mPresenter.getID(), count, pageSize, AppUtils.getAppVersionName());
            }
        });

    }

    @Override
    public void onSuccess(ExerciseListVO exerciseListVO) {
        refreshLayout.finishRefresh();

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
    public void left() {

    }

    @Override
    public void right() {
        startActivity(ExerciseSelectActivity.newInstance(mActivity, examType));
    }

    @Override
    public void responeError(String errorMsg) {
        stateError();
    }

}