package com.hlxyedu.mhk.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.ui.login.contract.CheckInfoContract;
import com.hlxyedu.mhk.ui.login.presenter.CheckInfoPresenter;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangguihua
 */
public class CheckInfoActivity extends RootActivity<CheckInfoPresenter> implements CheckInfoContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.nation_tv)
    TextView nationTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.id_card_tv)
    TextView idCardTv;
    @BindView(R.id.btn_neg)
    Button btnNeg;
    @BindView(R.id.btn_pos)
    Button btnPos;
    private UserVO userVO;

    public static Intent newInstance(Context context, UserVO userVO) {
        Intent intent = new Intent(context, CheckInfoActivity.class);
        intent.putExtra("userVO", (Parcelable) userVO);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_check_info;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
//        mPresenter.getUserInfo();
        userVO = getIntent().getParcelableExtra("userVO");

        nameTv.setText(userVO.getUserName());
        nationTv.setText(userVO.getNationName());
        phoneTv.setText(userVO.getMobile());
        idCardTv.setText(userVO.getIdCard());
    }

    @Override
    public void onSuccessInfo(UserVO userVO) {

    }

    @Override
    public void responeError(String errorMsg) {

    }

    @OnClick({R.id.btn_neg, R.id.btn_pos})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_neg:
                startActivity(LoginActivity.newInstance(this));
                break;
            case R.id.btn_pos:
                startActivity(MainActivity.newInstance(this));
                break;
        }
    }

    @Override
    public void left() {
        finish();
    }

    @Override
    public void right() {

    }
}