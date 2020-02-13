package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.DataVO;
import com.hlxyedu.mhk.model.event.LoginEvent;
import com.hlxyedu.mhk.ui.exam.activity.TestScoreActivity;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends BaseQuickAdapter<DataVO, BaseViewHolder> {

    private String title;
    private ArrayList<DataVO> datas;

    public ExerciseAdapter(int layoutResId,
                           @Nullable List<DataVO> data, String mTitles) {
        super(layoutResId, data);
        this.datas = (ArrayList<DataVO>) data;
        this.title = mTitles;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataVO item) {
//        String position = (helper.getLayoutPosition() + 1) > 9 ?
//                (helper.getLayoutPosition() + 1) + "" : "0" + (helper.getLayoutPosition() + 1);
//
////        String name = item.getName().substring(0, item.getName().lastIndexOf("."));
////        String txtStr = "【" + position + "】  " + "《" + name + "》示范朗读";
        helper.setText(R.id.positive_btn, title).addOnClickListener(R.id.positive_btn);
//        helper.setText(R.id.title, "【" + position + "】  " + item.getConTitle());
//
//        RelativeLayout relativeLayout = (RelativeLayout) helper.itemView;
//        relativeLayout.setOnClickListener(view ->
//                RxBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN,helper.getLayoutPosition(), (ArrayList<DataVO>) datas, title, item.getConTitle())));
    }

}
