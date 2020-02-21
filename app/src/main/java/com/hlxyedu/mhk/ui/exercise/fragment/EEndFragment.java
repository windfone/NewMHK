package com.hlxyedu.mhk.ui.exercise.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.exercise.contract.EEndContract;
import com.hlxyedu.mhk.ui.exercise.presenter.EEndPresenter;

/**
 * Created by zhangguihua
 */
public class EEndFragment extends RootFragment<EEndPresenter> implements EEndContract.View {


    public static EEndFragment newInstance() {
        Bundle args = new Bundle();

        EEndFragment fragment = new EEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_eend;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}