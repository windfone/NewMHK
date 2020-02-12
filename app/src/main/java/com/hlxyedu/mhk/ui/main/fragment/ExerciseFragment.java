package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.DataVO;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseSelectActivity;
import com.hlxyedu.mhk.ui.main.adapter.ExerciseAdapter;
import com.hlxyedu.mhk.ui.main.contract.ExerciseContract;
import com.hlxyedu.mhk.ui.main.presenter.ExercisePresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhangguihua
 */
public class ExerciseFragment extends RootFragment<ExercisePresenter> implements ExerciseContract.View, XBaseTopBarImp {


    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private ExerciseAdapter mAdapter;

    private List<DataVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

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

        mAdapter = new ExerciseAdapter(R.layout.item_exercise, dataVOList, "获取");
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
        startActivity(ExerciseSelectActivity.newInstance(mActivity));
    }
}