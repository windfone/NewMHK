package com.hlxyedu.mhk.ui.exercise.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.exercise.contract.ETxtContract;
import com.hlxyedu.mhk.ui.exercise.presenter.ETxtPresenter;

/**
 * Created by zhangguihua
 */
public class ETxtFragment extends RootFragment<ETxtPresenter> implements ETxtContract.View {


    public static ETxtFragment newInstance() {
        Bundle args = new Bundle();

        ETxtFragment fragment = new ETxtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_etxt;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}