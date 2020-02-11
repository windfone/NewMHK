package com.hlxyedu.mhk.ui.main.fragment;

import android.os.Bundle;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.main.contract.MineContract;
import com.hlxyedu.mhk.ui.main.presenter.MinePresenter;

/**
 * Created by zhangguihua
 */
public class MineFragment extends RootFragment<MinePresenter> implements MineContract.View {


    public static MineFragment newInstance() {
        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}