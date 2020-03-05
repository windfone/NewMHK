package com.hlxyedu.mhk.ui.exercise.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.model.bean.OperationVO;

import java.util.List;

public class SelectTxtAdapter extends BaseQuickAdapter<OperationVO, BaseViewHolder> {


    public SelectTxtAdapter(int layoutResId,
                            @Nullable List<OperationVO> data, String mTitles) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OperationVO item) {

        helper.setBackgroundRes(R.id.option_tv, R.drawable.shape_gray_circle)
                .setTextColor(R.id.option_tv, ContextCompat.getColor(mContext, R.color.gray7C7));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.setBackgroundColor(R.id.option_tv, ContextCompat.getColor(mContext, R.color.blue0F4))
                        .setTextColor(R.id.option_tv, ContextCompat.getColor(mContext, R.color.whitefff));
            }
        });
    }
}
