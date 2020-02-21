package com.hlxyedu.mhk.ui.exercise.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.DataVO;
import com.hlxyedu.mhk.ui.exercise.adapter.SelectTxtAdapter;
import com.hlxyedu.mhk.ui.exercise.contract.EChoiceContract;
import com.hlxyedu.mhk.ui.exercise.presenter.EChoicePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class ESelectTxtFragment extends RootFragment<EChoicePresenter> implements EChoiceContract.View {


    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.rcy)
    RecyclerView rcy;

    private SelectTxtAdapter mAdapter;

    private List<DataVO> dataVOList = new ArrayList<>();

    public static ESelectTxtFragment newInstance() {
        Bundle args = new Bundle();

        ESelectTxtFragment fragment = new ESelectTxtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_eselect_txt;
    }

    @Override
    protected void initEventAndData() {
        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());
        dataVOList.add(new DataVO());

        mAdapter = new SelectTxtAdapter(R.layout.item_select_txt, dataVOList, "获取");
        rcy.setLayoutManager(new LinearLayoutManager(mActivity));
        rcy.setAdapter(mAdapter);

    }

    @Override
    public void responeError(String errorMsg) {

    }

}