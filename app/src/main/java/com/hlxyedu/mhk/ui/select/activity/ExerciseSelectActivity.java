package com.hlxyedu.mhk.ui.select.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.SelectEvent;
import com.hlxyedu.mhk.ui.select.contract.ExerciseSelectContract;
import com.hlxyedu.mhk.ui.select.presenter.ExerciseSelectPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class ExerciseSelectActivity extends RootActivity<ExerciseSelectPresenter> implements ExerciseSelectContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.composition_cb)
    CheckBox compositionCb;
    @BindView(R.id.listening_cb)
    CheckBox listeningCb;
    @BindView(R.id.reading_cb)
    CheckBox readingCb;
    @BindView(R.id.write_cb)
    CheckBox writeCb;
    @BindView(R.id.oral_cb)
    CheckBox oralCb;
    @BindView(R.id.complex_cb)
    CheckBox complexCb;

    private String questionType;

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

    @OnClick(R.id.confirm_btn)
    public void onViewClicked() {
        RxBus.getDefault().post(new SelectEvent(SelectEvent.EXERCISE_SEL, questionType));
        finish();
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

    @OnClick({R.id.composition_cb, R.id.listening_cb, R.id.reading_cb, R.id.write_cb, R.id.oral_cb, R.id.complex_cb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.listening_cb:
                // 听力理解
                if (listeningCb.isChecked()){
                    questionType = "TL";
                    setOtherCBState(true,false,false,false,false,false);
                }else {
                    questionType = "";
                    setOtherCBState(false,false,false,false,false,false);
                }
                break;

            case R.id.oral_cb:
                // 口语听力
                if (oralCb.isChecked()){
                    questionType = "KY";
                    setOtherCBState(false,true,false,false,false,false);
                }else {
                    questionType = "";
                    setOtherCBState(false,false,false,false,false,false);
                }
                break;

            case R.id.reading_cb:
                // 阅读理解
                if (readingCb.isChecked()){
                    questionType = "YD";
                    setOtherCBState(false,false,true,false,false,false);
                }else {
                    questionType = "";
                    setOtherCBState(false,false,false,false,false,false);
                }
                break;

            case R.id.write_cb:
                // 书面表达
                if (writeCb.isChecked()){
                    questionType = "SM";
                    setOtherCBState(false,false,false,true,false,false);
                }else {
                    questionType = "";
                    setOtherCBState(false,false,false,false,false,false);
                }
                break;

            case R.id.composition_cb:
                // 作文
                if (compositionCb.isChecked()){
                    questionType = "ZW";
                    setOtherCBState(false,false,false,false,true,false);
                }else {
                    questionType = "";
                    setOtherCBState(false,false,false,false,false,false);
                }
                break;

            case R.id.complex_cb:
                // 全部
                if (complexCb.isChecked()){
                    // 自己设定的，到 activity中再判断
                    questionType = "1";
                    setOtherCBState(false,false,false,false,false,true);
                }else {
                    questionType = "";
                    setOtherCBState(false,false,false,false,false,false);
                }
                break;
        }
    }

    private void setOtherCBState(boolean TL,boolean KY, boolean YD, boolean SM, boolean ZW, boolean QB){
        listeningCb.setChecked(TL);
        oralCb.setChecked(KY);
        readingCb.setChecked(YD);
        writeCb.setChecked(SM);
        compositionCb.setChecked(ZW);
        complexCb.setChecked(QB);
    }

}