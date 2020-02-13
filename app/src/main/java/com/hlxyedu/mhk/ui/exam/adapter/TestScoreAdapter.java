package com.hlxyedu.mhk.ui.exam.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.model.bean.DataVO;

import java.util.ArrayList;
import java.util.List;

public class TestScoreAdapter extends BaseQuickAdapter<DataVO, BaseViewHolder> {

    private String title;
    private ArrayList<DataVO> datas;

    public TestScoreAdapter(int layoutResId,
                            @Nullable List<DataVO> data, String mTitles) {
        super(layoutResId, data);
        this.datas = (ArrayList<DataVO>) data;
        this.title = mTitles;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataVO item) {
        String start = "听力理解答对题数：";
        String end = "81题";
        String str;
        str = start + end;
        final SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#EA6651")), start.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.correct_number_tv,sb)
                .setText(R.id.all_number_tv,sb);

//        String position = (helper.getLayoutPosition() + 1) > 9 ?
//                (helper.getLayoutPosition() + 1) + "" : "0" + (helper.getLayoutPosition() + 1);
//
////        String name = item.getName().substring(0, item.getName().lastIndexOf("."));
////        String txtStr = "【" + position + "】  " + "《" + name + "》示范朗读";
//        helper.setText(R.id.positive_btn, title);
//        helper.setText(R.id.title, "【" + position + "】  " + item.getConTitle());
//
//        RelativeLayout relativeLayout = (RelativeLayout) helper.itemView;
//        relativeLayout.setOnClickListener(view ->
//                RxBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN,helper.getLayoutPosition(), (ArrayList<DataVO>) datas, title, item.getConTitle())));
    }
}
