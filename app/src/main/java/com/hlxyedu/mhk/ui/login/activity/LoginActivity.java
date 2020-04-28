package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.ui.login.contract.LoginContract;
import com.hlxyedu.mhk.ui.login.presenter.LoginPresenter;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.utils.CodeUtils;
import com.hlxyedu.mhk.utils.ForResultUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.skyworth.rxqwelibrary.widget.NetErrorDialog;

import butterknife.BindView;
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
    @BindView(R.id.code_iv)
    ImageView codeIv;
    @BindView(R.id.user_error_prompt_tv)
    TextView userErrorPromptTv;
    @BindView(R.id.psd_error_prompt_tv)
    TextView psdErrorPromptTv;
    @BindView(R.id.code_error_prompt_rl)
    RelativeLayout codeErrorPromptRl;
    @BindView(R.id.code_error_prompt_tv)
    TextView codeErrorPromptTv;

    private boolean codeVisible; // 是否显示验证码
    private int loginErrorNum; //登录失败(密码错误)次数,超过两次就显示验证码
    private Bitmap bitmap;
    private String code;

    private QMUITipDialog tipDialog;

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
        initUIState();
        initCode();

        mPresenter.getNoteInfo();
    }

    @Override
    public void responeError(String errorMsg) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
    }

    @Override
    public void noteInfoSuccess(String note) {
        psdEdit.setHint(note);
    }

    @Override
    public void loginSuccess(UserVO userVO) {
        tipDialog.dismiss();
//        startActivity(MainActivity.newInstance(this));
        startActivity(CheckInfoActivity.newInstance(this,userVO));
        KeyboardUtils.hideSoftInput(this);
        finish();
    }

    @Override
    public void closeLogin() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ForResultUtils.REQUEST_CODE && resultCode == ForResultUtils.RESULT_CODE) {
            String mobile = data.getStringExtra("mobile");
            String password = data.getStringExtra("password");
            userNameEdit.setText(mobile);
            psdEdit.setText(password);
            userNameEdit.setSelection(mobile.length());
        }
    }

    @OnClick({R.id.forget_psd_tv, R.id.login_btn, R.id.code_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_psd_tv:
                startActivityForResult(FoundPsdActivity.newInstance(this), ForResultUtils.REQUEST_CODE);
                break;
            case R.id.login_btn:
                if (!NetworkUtils.isConnected()) {
                    NetErrorDialog.getInstance().showNetErrorDialog(this);
                    return;
                }

                tipDialog = new QMUITipDialog.Builder(LoginActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在登录")
                        .create();
                tipDialog.show();

                String mobile = userNameEdit.getText().toString().trim();
                String password = psdEdit.getText().toString().trim();
                mPresenter.login(mobile, password);
                break;
            case R.id.code_iv:
                bitmap = CodeUtils.getInstance().createBitmap();
                code = CodeUtils.getInstance().getCode();
                codeIv.setImageBitmap(bitmap);
                ToastUtils.showShort(code);
                break;
        }
    }

    private void initCode() {
        //获取需要展示图片验证码的ImageView
        //获取工具类生成的图片验证码对象
        bitmap = CodeUtils.getInstance().createBitmap();
        //获取当前图片验证码的对应内容用于校验
        code = CodeUtils.getInstance().getCode();
        codeIv.setImageBitmap(bitmap);
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
                if (codeVisible) {
                    if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(psdEdit.getText().toString())
                            && !StringUtils.isTrimEmpty(vertifyEdit.getText().toString())) {
                        loginBtn.setEnabled(true);
                    } else {
                        loginBtn.setEnabled(false);
                    }
                } else {
                    if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(psdEdit.getText().toString())) {
                        loginBtn.setEnabled(true);
                    } else {
                        loginBtn.setEnabled(false);
                    }
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
                if (codeVisible) {
                    if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
                            && !StringUtils.isTrimEmpty(vertifyEdit.getText().toString())) {
                        loginBtn.setEnabled(true);
                    } else {
                        loginBtn.setEnabled(false);
                    }
                } else {
                    if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())) {
                        loginBtn.setEnabled(true);
                    } else {
                        loginBtn.setEnabled(false);
                    }
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