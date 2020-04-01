package com.hlxyedu.mhk.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.mine.contract.ServiceTermsContract;
import com.hlxyedu.mhk.ui.mine.presenter.ServiceTermsPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class ServiceTermsActivity extends RootActivity<ServiceTermsPresenter> implements ServiceTermsContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.terms_detail_tv)
    TextView termsDetailTv;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ServiceTermsActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_terms;
    }

    @Override
    protected void initEventAndData() {
        xbaseTopbar.setxBaseTopBarImp(this);
        String[] details = getResources().getStringArray(R.array.terms_details);
        String strings = "";
        for (int i = 0; i < details.length; i++) {
            strings += details[i] + "\n\n";
        }
        termsDetailTv.setText(strings);
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
}