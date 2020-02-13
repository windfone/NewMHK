package com.hlxyedu.mhk.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.mine.contract.FeedBackContract;
import com.hlxyedu.mhk.ui.mine.presenter.FeedBackPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class FeedBackActivity extends RootActivity<FeedBackPresenter> implements FeedBackContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.title_edit)
    EditText titleEdit;
    @BindView(R.id.title_number_tv)
    TextView titleNumberTv;
    @BindView(R.id.detail_edit)
    EditText detailEdit;
    @BindView(R.id.detail_number_tv)
    TextView detailNumberTv;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
        initEditState();
    }

    private void initEditState() {
        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 16) {
                    titleEdit.setText(s.toString().substring(0, 16));
                    titleEdit.setSelection(16);
                    ToastUtils.showShort("字数已超出上限");
                    return;
                } else {
                    titleNumberTv.setText(s.length() + "/16");
                }
            }
        });

        detailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 200) {
                    titleEdit.setText(s.toString().substring(0, 200));
                    titleEdit.setSelection(200);
                    ToastUtils.showShort("最多可输入200字");
                    return;
                } else {
                    detailNumberTv.setText(s.length() + "/200");
                }
            }
        });

    }

    @Override
    public void responeError(String errorMsg) {

    }


    @OnClick(R.id.confirm_btn)
    public void onViewClicked() {

    }


    @Override
    public void left() {
        finish();
    }

    @Override
    public void right() {

    }
}