package com.hlxyedu.mhk.ui.ecomposition.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.ecomposition.contract.TxtContract;
import com.hlxyedu.mhk.ui.ecomposition.presenter.TxtPresenter;

/**
 * Created by zhangguihua
 */
public class TxtFragment extends RootFragment<TxtPresenter> implements TxtContract.View {


    public static TxtFragment newInstance() {
        Bundle args = new Bundle();

        TxtFragment fragment = new TxtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_txt;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}