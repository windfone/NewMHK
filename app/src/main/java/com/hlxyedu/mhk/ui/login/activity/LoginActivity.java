package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.contract.LoginContract;
import com.hlxyedu.mhk.ui.login.presenter.LoginPresenter;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

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
        showNetErrorDialog();
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick({R.id.forget_psd_tv, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_psd_tv:
                break;
            case R.id.login_btn:
                startActivity(MainActivity.newInstance(this));
                break;
        }
    }

    public void showNetErrorDialog() {

        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_net))
                .setContentWidth((int) (display
                        .getWidth() * 0.8))
                .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(true)
                .setOnClickListener((dialog, view1) -> {
                    switch (view1.getId()) {
                        case R.id.confirm_btn:
                            dialog.dismiss();
                            break;
                    }
                }).create();
        dialogPlus.show();
    }

}