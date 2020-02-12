package com.hlxyedu.mhk.ui.exercise.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.exercise.contract.ExerciseSelectContract;
import com.hlxyedu.mhk.ui.exercise.presenter.ExerciseSelectPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class ExerciseSelectActivity extends RootActivity<ExerciseSelectPresenter> implements ExerciseSelectContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.completed_cb)
    CheckBox completedCb;
    @BindView(R.id.all_state_cb)
    CheckBox allStateCb;
    @BindView(R.id.undone_cb)
    CheckBox undoneCb;
    @BindView(R.id.listening_type_1_cb)
    CheckBox listeningType1Cb;
    @BindView(R.id.listening_type_2_cb)
    CheckBox listeningType2Cb;
    @BindView(R.id.reading_comprehension_cb)
    CheckBox readingComprehensionCb;
    @BindView(R.id.write_reading_cb)
    CheckBox writeReadingCb;
    @BindView(R.id.oral_listening_cb)
    CheckBox oralListeningCb;
    @BindView(R.id.whole_truth_test_cb)
    CheckBox wholeTruthTestCb;
    @BindView(R.id.low_top_cb)
    CheckBox lowTopCb;
    @BindView(R.id.top_low_cb)
    CheckBox topLowCb;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ExerciseSelectActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_exercise_select;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
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