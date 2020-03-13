package com.hlxyedu.mhk.ui.exercise.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.ui.exercise.contract.TestFinishContract;
import com.hlxyedu.mhk.ui.exercise.presenter.TestFinishPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhangugihuaq
 */
public class TestFinishFragment extends RootFragment<TestFinishPresenter> implements TestFinishContract.View {

    @BindView(R.id.success_hint_text)
    TextView successHintText;

    public static TestFinishFragment newInstance() {
        Bundle args = new Bundle();

        TestFinishFragment fragment = new TestFinishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_finish;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}