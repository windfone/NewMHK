package com.hlxyedu.mhk.ui.exercise.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.exercise.contract.ERepeatContract;
import com.hlxyedu.mhk.ui.exercise.presenter.ERepeatPresenter;

/**
 * Created by zhangguihua
 */
public class ERepeatFragment extends RootFragment<ERepeatPresenter> implements ERepeatContract.View {


    public static ERepeatFragment newInstance() {
        Bundle args = new Bundle();

        ERepeatFragment fragment = new ERepeatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_erepeat;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}