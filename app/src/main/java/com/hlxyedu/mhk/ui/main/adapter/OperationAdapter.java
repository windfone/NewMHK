package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.model.bean.OperationVO;

import java.util.ArrayList;
import java.util.List;

public class OperationAdapter extends BaseQuickAdapter<OperationVO, BaseViewHolder> {

    private ArrayList<OperationVO> datas;

    public OperationAdapter(int layoutResId,
                            @Nullable List<OperationVO> data) {
        super(layoutResId, data);
        this.datas = (ArrayList<OperationVO>) data;
    }

    @Override
    protected void convert(BaseViewHolder helper, OperationVO item) {
        switch (item.getState()){
            case "1":
                helper.setText(R.id.positive_btn, "做作业").addOnClickListener(R.id.positive_btn);
                break;
            case "2":
                helper.setText(R.id.positive_btn, "未完成").addOnClickListener(R.id.positive_btn);
                break;
            case "3":
            case "4":
                helper.setText(R.id.positive_btn, "已完成").addOnClickListener(R.id.positive_btn);
                break;
        }

        helper.addOnClickListener(R.id.positive_btn)
              .setText(R.id.title_tv,item.getHomeworkName())
              .setText(R.id.author_tv,item.getTeacherName()+"");

        if (StringUtils.equals(item.getExamType(),"KY")){ //口语
            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_oral_listening);
        }else if (StringUtils.equals(item.getExamType(),"YD")){ //阅读

        }else if (StringUtils.equals(item.getExamType(),"SM")){ //书面

        }else if (StringUtils.equals(item.getExamType(),"TL")){ //听力
            helper.setImageResource(R.id.question_type_iv,R.drawable.icon_hearing_2);
        }else if (StringUtils.equals(item.getExamType(),"ZW")){ //作文

        }else if (StringUtils.equals(item.getExamType(),"ZH")){ //全真试题（综合）

        }

    }

}
