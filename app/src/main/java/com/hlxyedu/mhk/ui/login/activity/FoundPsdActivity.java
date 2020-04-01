package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.contract.FoundPsdContract;
import com.hlxyedu.mhk.ui.login.presenter.FoundPsdPresenter;
import com.hlxyedu.mhk.utils.ForResultUtils;
import com.skyworth.rxqwelibrary.widget.NetErrorDialog;

import butterknife.BindView;
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

    private String mobile, psd, newPsd, idCard;

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
        initUIState();

    }

    @Override
    public void responeError(String errorMsg) {

    }

    @Override
    public void foundPsdSuccess() {
        ToastUtils.showShort("密码重置成功");
        Intent i = new Intent();
        i.putExtra("mobile", mobile);
        i.putExtra("password", psd);
        setResult(ForResultUtils.RESULT_CODE, i);
        finish();
    }

    @OnClick(R.id.confirm_btn)
    public void onViewClicked() {
        // 先验证网络
        if (!NetworkUtils.isConnected()) {
            NetErrorDialog.getInstance().showNetErrorDialog(this);
            return;
        }

       /* // 身份证号不是18位 不合格
        if (IDCardEdit.getText().toString().trim().length() != 18) {
            IDCardErrTv.setVisibility(View.VISIBLE);
            return;
        } else {
            IDCardErrTv.setVisibility(View.INVISIBLE);
        }*/

        mobile = userNameEdit.getText().toString().trim();
        psd = newPsdEdit.getText().toString().trim();
        newPsd = newPsdAgainEdit.getText().toString().trim();
//      if  idCard = IDCardEdit.getText().toString().trim();

        if (psd.length() < 6 || psd.length() > 16){
            return;
        }

        if (StringUtils.equals(psd, newPsd)) {
            psdDifferentTv.setVisibility(View.INVISIBLE);
        } else {
            psdDifferentTv.setVisibility(View.VISIBLE);
            return;
        }
        mPresenter.foundPsd(mobile, psd, idCard);
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
      /*  IDCardEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    idCardLine.setBackgroundColor(getResources().getColor(R.color.blueED4));
                    newPsdLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                    newPsdAgainLine.setBackgroundColor(getResources().getColor(R.color.gray9D9));
                }
            }
        });*/
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
//                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(IDCardEdit.getText().toString())
                if (s.toString().trim().length() > 0
                        && !StringUtils.isTrimEmpty(newPsdEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdAgainEdit.getText().toString())) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }
            }
        });

        /*IDCardEdit.addTextChangedListener(new TextWatcher() {
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
        });*/

        newPsdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() >= 6 && s.toString().trim().length() <= 16) {
                    requirementsTv.setVisibility(View.INVISIBLE);

                } else {
                    requirementsTv.setVisibility(View.VISIBLE);
                }

                if (s.toString().trim().length() > 0 && !StringUtils.isTrimEmpty(userNameEdit.getText().toString())
//                        && !StringUtils.isTrimEmpty(IDCardEdit.getText().toString())
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
//                        && !StringUtils.isTrimEmpty(IDCardEdit.getText().toString())
                        && !StringUtils.isTrimEmpty(newPsdEdit.getText().toString())) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }
            }
        });

    }

}