package com.hlxyedu.mhk.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.login.activity.LoginActivity;
import com.hlxyedu.mhk.ui.main.contract.MineContract;
import com.hlxyedu.mhk.ui.main.presenter.MinePresenter;
import com.hlxyedu.mhk.ui.mine.activity.AboutUsActivity;
import com.hlxyedu.mhk.ui.mine.activity.FeedBackActivity;
import com.hlxyedu.mhk.ui.mine.activity.GradeActivity;
import com.hlxyedu.mhk.ui.mine.activity.ServiceTermsActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class MineFragment extends RootFragment<MinePresenter> implements MineContract.View {

    @BindView(R.id.grade_rl)
    RelativeLayout gradeRl;
    @BindView(R.id.feedback_rl)
    RelativeLayout feedbackRl;
    @BindView(R.id.terms_of_service_rl)
    RelativeLayout termsOfServiceRl;
    @BindView(R.id.about_us_rl)
    RelativeLayout aboutUsRl;
    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.version_rl)
    RelativeLayout versionRl;
    @BindView(R.id.exit_btn)
    Button exit_btn;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEventAndData() {
        versionTv.setText("v" + AppUtils.getAppVersionName());
        if (mPresenter.isLogin()) {
            exit_btn.setVisibility(View.VISIBLE);
        } else {
            exit_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick({R.id.grade_rl, R.id.feedback_rl, R.id.terms_of_service_rl, R.id.about_us_rl, R.id.version_rl, R.id.exit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.grade_rl:
                startActivity(GradeActivity.newInstance(mActivity));
                break;
            case R.id.feedback_rl:
                startActivity(FeedBackActivity.newInstance(mActivity));
                break;
            case R.id.terms_of_service_rl:
                startActivity(ServiceTermsActivity.newInstance(mActivity));
                break;
            case R.id.about_us_rl:
                startActivity(AboutUsActivity.newInstance(mActivity));
                break;
            case R.id.version_rl:
                ToastUtils.showShort("当前已经是最新版本");
                break;
            case R.id.exit_btn:
                WindowManager windowManager = (WindowManager) mActivity
                        .getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();

                DialogPlus logoutDialog = DialogPlus.newDialog(mActivity)
                        .setGravity(Gravity.CENTER)
                        .setContentHolder(new ViewHolder(R.layout.dialog_logout))
                        .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                        .setContentWidth((int) (display
                                .getWidth() * 0.8))
                        .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)//设置不可取消   可以取消
                        .setOnClickListener((dialog, view1) -> {
                            switch (view1.getId()) {
                                case R.id.btn_neg:
                                    dialog.dismiss();
                                    break;
                                case R.id.btn_pos:
                                    mPresenter.clearLoginInfo();
                                    mPresenter.setLoginState(false);
                                    startActivity(LoginActivity.newInstance(mActivity));
                                    mActivity.finish();
                                    break;
                            }
                        }).create();
                logoutDialog.show();
                break;
        }
    }
}