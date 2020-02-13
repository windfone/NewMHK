package com.hlxyedu.mhk.ui.operation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.operation.contract.OperationSelectContract;
import com.hlxyedu.mhk.ui.operation.presenter.OperationSelectPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class OperationSelectActivity extends RootActivity<OperationSelectPresenter> implements OperationSelectContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.exercise_number_tv)
    TextView exerciseNumberTv;
    @BindView(R.id.positive_cb)
    CheckBox positiveCb;
    @BindView(R.id.positive_cv)
    CardView positiveCv;
    @BindView(R.id.reserve_cb)
    CheckBox reserveCb;
    @BindView(R.id.reserve_cv)
    CardView reserveCv;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.state_tv)
    TextView stateTv;
    @BindView(R.id.completed_cb)
    CheckBox completedCb;
    @BindView(R.id.completed_cv)
    CardView completedCv;
    @BindView(R.id.all_state_cb)
    CheckBox allStateCb;
    @BindView(R.id.all_state_cv)
    CardView allStateCv;
    @BindView(R.id.undone_cb)
    CheckBox undoneCb;
    @BindView(R.id.undone_cv)
    CardView undoneCv;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, OperationSelectActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_operation_select;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick({R.id.positive_cb, R.id.reserve_cb, R.id.completed_cb, R.id.all_state_cb, R.id.undone_cb, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.positive_cb:
                break;
            case R.id.reserve_cb:
                break;
            case R.id.completed_cb:
                break;
            case R.id.all_state_cb:
                break;
            case R.id.undone_cb:
                break;
            case R.id.confirm_btn:
                break;
        }
    }

    @Override
    public void left() {
        finish();
    }

    @Override
    public void right() {

    }

}