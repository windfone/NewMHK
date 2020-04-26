package com.hlxyedu.mhk.ui.exam.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.RefreshEvent;
import com.hlxyedu.mhk.ui.exam.contract.ExamFinishContract;
import com.hlxyedu.mhk.ui.exam.presenter.ExamFinishPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class ExamFinishActivity extends RootActivity<ExamFinishPresenter> implements ExamFinishContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.finish_btn)
    Button finishBtn;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ExamFinishActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_exam_finish;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick(R.id.finish_btn)
    public void onViewClicked() {
        RxBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH_EVENT));
        finish();
    }

    @Override
    public void left() {
        RxBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH_EVENT));
        finish();
    }

    @Override
    public void onBackPressed() {
        RxBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH_EVENT));
        super.onBackPressed();
    }

    @Override
    public void right() {

    }
}