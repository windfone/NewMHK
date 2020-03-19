package com.hlxyedu.mhk.ui.eread.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.eread.contract.ReadContract;
import com.hlxyedu.mhk.ui.eread.presenter.ReadPresenter;

/**
 * Created by zhangguihua
 */
public class ReadFragment extends RootFragment<ReadPresenter> implements ReadContract.View {


    public static ReadFragment newInstance() {
        Bundle args = new Bundle();

        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}