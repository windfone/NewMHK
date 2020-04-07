package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.ui.main.adapter.ExamAdapter;
import com.hlxyedu.mhk.ui.main.contract.ExamContract;
import com.hlxyedu.mhk.ui.main.presenter.ExamPresenter;
import com.hlxyedu.mhk.weight.MyLinearLayoutManager;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.dialog.MoreTaskDLDialog;
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
public class ExamFragment extends RootFragment<ExamPresenter> implements ExamContract.View {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private ExamAdapter mAdapter;

    private List<ExamVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;


    public static ExamFragment newInstance() {
        Bundle args = new Bundle();

        ExamFragment fragment = new ExamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exam;
    }

/*    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        dataVOList.clear();
        count = 1;
        pageSize = 20;
        mPresenter.getMockList(mPresenter.getID(), count, pageSize);
    }*/

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        stateLoading();

        mAdapter = new ExamAdapter(R.layout.item_exercise, dataVOList);
        rlv.setLayoutManager(new MyLinearLayoutManager(mActivity));
        rlv.setAdapter(mAdapter);

        count = 1;
        if (!dataVOList.isEmpty()) {
            dataVOList.clear();
        }
        mPresenter.getMockList(mPresenter.getID(), count, pageSize);

        mAdapter.setPreLoadNumber(1);
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.getMockList(mPresenter.getID(), ++count, pageSize);
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
                mPresenter.getMockList(mPresenter.getID(), count, pageSize);
            }
        });

    }

    @Override
    public void onSuccess(List<ExamVO> examVOS) {
        refreshLayout.finishRefresh();

        if (!examVOS.isEmpty()) {
            dataVOList.addAll(examVOS);
            mAdapter.setNewData(dataVOList);
            if (examVOS.size() < pageSize) {
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
    public void download() {

        MoreTaskDLDialog downloadDialog = new MoreTaskDLDialog(mActivity);
        downloadDialog.show();

    }

    @Override
    public void responeError(String errorMsg) {
        stateError();
    }

}