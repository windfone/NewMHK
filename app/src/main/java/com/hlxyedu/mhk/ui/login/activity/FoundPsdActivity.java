package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.contract.FoundPsdContract;
import com.hlxyedu.mhk.ui.login.presenter.FoundPsdPresenter;
import com.hlxyedu.mhk.weight.dialog.NetErrorDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class FoundPsdActivity extends RootActivity<FoundPsdPresenter> implements FoundPsdContract.View {

    @BindView(R.id.user_name_edit)
    EditText userNameEdit;
    @BindView(R.id.user_unregister_tv)
    TextView userUnregisterTv;
    @BindView(R.id.ID_card_edit)
    EditText IDCardEdit;
    @BindView(R.id.ID_card_err_tv)
    TextView IDCardErrTv;
    @BindView(R.id.new_psd_edit)
    EditText newPsdEdit;
    @BindView(R.id.requirements_tv)
    TextView requirementsTv;
    @BindView(R.id.new_psd_again_edit)
    EditText newPsdAgainEdit;
    @BindView(R.id.psd_different_tv)
    TextView psdDifferentTv;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.user_name_line)
    View userNameLine;
    @BindView(R.id.id_card_line)
    View idCardLine;
    @BindView(R.id.new_psd_line)
    View newPsdLine;
    @BindView(R.id.new_psd_again_line)
    View newPsdAgainLine;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FoundPsdActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_found_psd;
    }

    @Override
    protected void initEventAndData() {
        NetErrorDialog.getInstance().showNetErrorDialog(this);
        initUIState();

    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick(R.id.confirm_btn)
    public void onViewClicked() {
    }

    private void initUIState() {
        userNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                    idCardLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdAgainLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                }
            }
        });
        IDCardEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    idCardLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                    newPsdLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdAgainLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                }
            }
        });
        newPsdEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    idCardLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                    newPsdAgainLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                }
            }
        });
        newPsdAgainEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    idCardLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdAgainLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                }
            }
        });

        //设置登陆按钮状态
        userNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(IDCardEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdAgainEdit.getText().toString())) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }
            }
        });

        IDCardEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdAgainEdit.getText().toString())) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }
            }
        });

        newPsdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(IDCardEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdAgainEdit.getText().toString())) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }
            }
        });

        newPsdAgainEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(IDCardEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdEdit.getText().toString())) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }
            }
        });

    }

}