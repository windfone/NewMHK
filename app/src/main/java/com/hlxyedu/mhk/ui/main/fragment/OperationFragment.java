package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.DataVO;
import com.hlxyedu.mhk.ui.main.adapter.ExerciseAdapter;
import com.hlxyedu.mhk.ui.main.contract.OperationContract;
import com.hlxyedu.mhk.ui.main.presenter.OperationPresenter;
import com.hlxyedu.mhk.ui.operation.activity.OperationSelectActivity;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

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

    private ExerciseAdapter mAdapter;

    private List<DataVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

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
    protected void initEventAndData() {
        super.initEventAndData();
        xbaseTopbar.setxBaseTopBarImp(this);
//        stateLoading();
        stateMain();

        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());

        mAdapter = new ExerciseAdapter(R.layout.item_exercise, dataVOList, "做作业");
        rlv.setLayoutManager(new LinearLayoutManager(mActivity));
        rlv.setAdapter(mAdapter);

//        count = 1;
//        if (!dataVOList.isEmpty()) {
//            dataVOList.clear();
//        }
////        mPresenter.getLearningList(getArguments().getInt("typeId"),pageSize,count);
//
//        mAdapter.setPreLoadNumber(1);
//        mAdapter.setOnLoadMoreListener(() -> {
//            count++;
////            mPresenter.getLearningList(getArguments().getInt("typeId"),pageSize,count);
//        }, rlv);

    }

//    @Override
//    public void onSuccess(List<DataVO> essayVOS) {
//        if (!essayVOS.isEmpty()) {
//            dataVOList.addAll(essayVOS);
//            mAdapter.setNewData(dataVOList);
//            if (essayVOS.size() < pageSize) {
//                mAdapter.loadMoreEnd();
//            } else {
//                mAdapter.loadMoreComplete();
//            }
//            stateMain();
//        } else {
//            if (count == 1) {
//                stateEmpty("暂无内容");
//            } else {
//                mAdapter.loadMoreEnd();
//            }
//        }
//    }

    @Override
    public void responeError(String errorMsg) {
        stateError();
    }

    @Override
    public void left() {

    }

    @Override
    public void right() {
        startActivity(OperationSelectActivity.newInstance(mActivity));
    }
}