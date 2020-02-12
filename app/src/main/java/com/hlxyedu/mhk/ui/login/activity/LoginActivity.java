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
import com.hlxyedu.mhk.ui.login.contract.LoginContract;
import com.hlxyedu.mhk.ui.login.presenter.LoginPresenter;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.weight.dialog.NetErrorDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class LoginActivity extends RootActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.user_name_edit)
    EditText userNameEdit;
    @BindView(R.id.psd_edit)
    EditText psdEdit;
    @BindView(R.id.forget_psd_tv)
    TextView forgetPsdTv;
    @BindView(R.id.vertify_edit)
    EditText vertifyEdit;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.user_name_line)
    View userNameLine;
    @BindView(R.id.psd_line)
    View psdLine;
    @BindView(R.id.vertify_line)
    View vertifyLine;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        NetErrorDialog.getInstance().showNetErrorDialog(this);
        initUIState();
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick({R.id.forget_psd_tv, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_psd_tv:
                startActivity(FoundPsdActivity.newInstance(this));
                break;
            case R.id.login_btn:
                startActivity(MainActivity.newInstance(this));
                break;
        }
    }


    private void initUIState() {
        userNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                    psdLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    vertifyLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                }
            }
        });
        psdEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    psdLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                    vertifyLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                }
            }
        });
        vertifyEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    psdLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    vertifyLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                }
            }
        });

        userNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(psdEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(vertifyEdit.getText().toString())) {
                    loginBtn.setEnabled(true);
                } else {
                    loginBtn.setEnabled(false);
                }
            }
        });

        psdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(vertifyEdit.getText().toString())) {
                    loginBtn.setEnabled(true);
                } else {
                    loginBtn.setEnabled(false);
                }
            }
        });

        vertifyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(psdEdit.getText().toString())) {
                    loginBtn.setEnabled(true);
                } else {
                    loginBtn.setEnabled(false);
                }
            }
        });

    }

}