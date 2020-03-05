package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.model.bean.ExerciseVO;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends BaseQuickAdapter<ExerciseVO, BaseViewHolder> {

    private String title;
    private ArrayList<ExerciseVO> datas;

    public ExerciseAdapter(int layoutResId,
                           @Nullable List<ExerciseVO> data, String mTitles) {
        super(layoutResId, data);
        this.datas = (ArrayList<ExerciseVO>) data;
        this.title = mTitles;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExerciseVO item) {
        helper.setText(R.id.positive_btn, title).addOnClickListener(R.id.positive_btn)
              .setText(R.id.title_tv,item.getExamname())
              .setText(R.id.exercise_number_tv,item.getTimes()+"");

//        if (StringUtils.equals(item.getExamType(),"KY")){ //口语
//            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_oral_listening);
//        }else if (StringUtils.equals(item.getExamType(),"YD")){ //阅读
//
//        }else if (StringUtils.equals(item.getExamType(),"SM")){ //？
//
//        }else if (StringUtils.equals(item.getExamType(),"TL")){ //听力
//            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_hearing_2);
//        }else if (StringUtils.equals(item.getExamType(),"ZW")){ //作文
//
//        }

    }

}
