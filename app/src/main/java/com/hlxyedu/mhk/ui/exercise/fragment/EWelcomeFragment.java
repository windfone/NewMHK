package com.hlxyedu.mhk.ui.exercise.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.exercise.contract.EWelcomeContract;
import com.hlxyedu.mhk.ui.exercise.presenter.EWelcomePresenter;

/**
 * Created by zhangguihua
 */
public class EWelcomeFragment extends RootFragment<EWelcomePresenter> implements EWelcomeContract.View {


    public static EWelcomeFragment newInstance() {
        Bundle args = new Bundle();

        EWelcomeFragment fragment = new EWelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ewelcome;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}