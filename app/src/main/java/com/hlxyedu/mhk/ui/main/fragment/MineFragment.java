package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.exam.activity.TestScoreActivity;
import com.hlxyedu.mhk.ui.main.contract.MineContract;
import com.hlxyedu.mhk.ui.main.presenter.MinePresenter;
import com.hlxyedu.mhk.ui.mine.activity.FeedBackActivity;
import com.hlxyedu.mhk.ui.mine.activity.GradeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
                break;
            case R.id.about_us_rl:
                break;
            case R.id.version_rl:
                break;
            case R.id.exit_btn:
                mPresenter.clearLoginInfo();
                mPresenter.setLoginState(false);
                break;
        }
    }
}