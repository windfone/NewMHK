package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.ui.main.adapter.OperationAdapter;
import com.hlxyedu.mhk.ui.main.contract.OperationContract;
import com.hlxyedu.mhk.ui.main.presenter.OperationPresenter;
import com.hlxyedu.mhk.ui.select.activity.OperationSelectActivity;
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
public class OperationFragment extends RootFragment<OperationPresenter> implements OperationContract.View, XBaseTopBarImp {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private OperationAdapter mAdapter;

    private List<OperationVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    private String hws = "A";// 作业完成状态,默认全部


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

    /**
     * 放弃使用此方法的原因是 筛选，返回页面后会重复请求两次接口
     */
    /*@Override
    public void onSupportVisible() {
        super.onSupportVisible();
        dataVOList.clear();
        count = 1;
        pageSize = 20;
        mPresenter.getOperationList(mPresenter.getID(), count, pageSize, hws);
    }*/
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        xbaseTopbar.setxBaseTopBarImp(this);
        stateLoading();

        mAdapter = new OperationAdapter(R.layout.item_exercise, dataVOList);
        rlv.setLayoutManager(new MyLinearLayoutManager(mActivity));
        rlv.setAdapter(mAdapter);

        count = 1;
        if (!dataVOList.isEmpty()) {
            dataVOList.clear();
        }
        mPresenter.getOperationList(mPresenter.getID(), count, pageSize, hws);

        mAdapter.setPreLoadNumber(1);
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.getOperationList(mPresenter.getID(), ++count, pageSize, hws);
        }, rlv);

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
                mPresenter.getOperationList(mPresenter.getID(), count, pageSize, hws);
            }
        });
    }

    @Override
    public void onSelect(String state) {
        if (StringUtils.isEmpty(state)) {
            return;
        }
        hws = state;
        dataVOList.clear();
        mAdapter.notifyDataSetChanged();
        count = 1;
        mPresenter.getOperationList(mPresenter.getID(), count, pageSize, hws);
    }

    @Override
    public void onSuccess(List<OperationVO> operationListVOS) {
        refreshLayout.finishRefresh();

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
    public void download(DownLoadEvent s) {
        DownLoadDialog downloadDialog = new DownLoadDialog(mActivity, s);
        downloadDialog.show();
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
        startActivity(OperationSelectActivity.newInstance(mActivity, hws));
    }
}