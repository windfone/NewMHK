package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.main.contract.ExamContract;
import com.hlxyedu.mhk.ui.main.presenter.ExamPresenter;

/**
 * Created by zhangguihua
 */
public class ExamFragment extends RootFragment<ExamPresenter> implements ExamContract.View {


    public static ExamFragment newInstance() {
        Bundle args = new Bundle();

        ExamFragment fragment = new ExamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exam;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}