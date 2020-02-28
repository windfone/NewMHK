package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.DataVO;
import com.hlxyedu.mhk.model.bean.ExamListVO;
import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.event.LoginEvent;
import com.hlxyedu.mhk.ui.exam.activity.TestScoreActivity;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends BaseQuickAdapter<ExamVO, BaseViewHolder> {

    private String title;
    private ArrayList<ExamVO> datas;

    public ExerciseAdapter(int layoutResId,
                           @Nullable List<ExamVO> data, String mTitles) {
        super(layoutResId, data);
        this.datas = (ArrayList<ExamVO>) data;
        this.title = mTitles;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExamVO item) {
        helper.setText(R.id.positive_btn, title).addOnClickListener(R.id.positive_btn)
              .setText(R.id.title_tv,item.getExamname())
              .setText(R.id.exercise_number_tv,item.getTimes()+"");

    }

}
