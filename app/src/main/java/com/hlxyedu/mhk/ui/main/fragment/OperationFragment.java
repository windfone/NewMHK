package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.main.contract.OperationContract;
import com.hlxyedu.mhk.ui.main.presenter.OperationPresenter;

/**
 * Created by zhangguihua
 */
public class OperationFragment extends RootFragment<OperationPresenter> implements OperationContract.View {


    public static OperationFragment newInstance() {
        Bundle args = new Bundle();

        OperationFragment fragment = new OperationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operation;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}