package com.hlxyedu.mhk.ui.select.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.SelectEvent;
import com.hlxyedu.mhk.ui.select.contract.OperationSelectContract;
import com.hlxyedu.mhk.ui.select.presenter.OperationSelectPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class OperationSelectActivity extends RootActivity<OperationSelectPresenter> implements OperationSelectContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
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

    private String hws;

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

    @OnClick({ R.id.completed_cb, R.id.all_state_cb, R.id.undone_cb, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.completed_cb:
                hws = "D";
                break;
            case R.id.all_state_cb:
                hws = "A";
                break;
            case R.id.undone_cb:
                hws = "U";
                break;
            case R.id.confirm_btn:
                RxBus.getDefault().post(new SelectEvent(SelectEvent.OPERATION_SEL, hws));
                finish();
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