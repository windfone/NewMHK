package com.hlxyedu.mhk.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.model.bean.OperationVO;
import com.hlxyedu.mhk.ui.exam.adapter.TestScoreAdapter;
import com.hlxyedu.mhk.ui.mine.contract.GradeContract;
import com.hlxyedu.mhk.ui.mine.presenter.GradePresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class GradeActivity extends RootActivity<GradePresenter> implements GradeContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    private TextView finishPagerTv;

    private TestScoreAdapter mAdapter;

    private List<OperationVO> OperationVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, GradeActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_grade;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
        OperationVOList.add(new OperationVO());
        OperationVOList.add(new OperationVO());
        OperationVOList.add(new OperationVO());
        OperationVOList.add(new OperationVO());
        OperationVOList.add(new OperationVO());

        View view = LayoutInflater.from(this).inflate(R.layout.grade_header_view, null);
        finishPagerTv = view.findViewById(R.id.finished_pager_tv);
        mAdapter = new TestScoreAdapter(R.layout.item_test_score, OperationVOList, "获取");
        mAdapter.addHeaderView(view);
        rlv.setLayoutManager(new LinearLayoutManager(this));
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