package com.hlxyedu.mhk.ui.exam.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.ui.exam.adapter.TestScoreAdapter;
import com.hlxyedu.mhk.ui.exam.contract.TestScoreContract;
import com.hlxyedu.mhk.ui.exam.presenter.TestScorePresenter;
import com.hlxyedu.mhk.weight.MyLinearLayoutManager;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class TestScoreActivity extends RootActivity<TestScorePresenter> implements TestScoreContract.View, XBaseTopBarImp {

    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private TestScoreAdapter mAdapter;

    private List<OperationVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TestScoreActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test_score;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
        dataVOList.add(new OperationVO());
        dataVOList.add(new OperationVO());
        dataVOList.add(new OperationVO());
        dataVOList.add(new OperationVO());
        dataVOList.add(new OperationVO());

        mAdapter = new TestScoreAdapter(R.layout.item_test_score, dataVOList, "获取");
        rlv.setLayoutManager(new MyLinearLayoutManager(this));
        rlv.setAdapter(mAdapter);
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @Override
    public void left() {
        finish();
    }

    @Override
    public void right() {

    }
}