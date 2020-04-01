package com.hlxyedu.mhk.ui.main.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ExamProgressVO;
import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.weight.listener.NoDoubleClickListener;
import com.skyworth.rxqwelibrary.widget.NetErrorDialog;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends BaseQuickAdapter<ExamVO, BaseViewHolder> {

    private ArrayList<ExamVO> datas;

    public ExamAdapter(int layoutResId,
                       @Nullable List<ExamVO> data) {
        super(layoutResId, data);
        this.datas = (ArrayList<ExamVO>) data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExamVO item) {
        helper.setText(R.id.title_tv, item.getTestName())
                .setText(R.id.author_tv, item.getTeacherName() + "老师")
                .setText(R.id.positive_btn, "去考试")
//                .setText(R.id.end_time_tv, TimeUtils.getMinTime(item.getTestEndTime()))
                .setImageResource(R.id.question_type_iv, R.drawable.icon_zh);
//                .setText(R.id.exercise_number_tv, item.getTotalPoints() + "次考试");

        Button button = helper.getView(R.id.positive_btn);
        button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (!NetworkUtils.isConnected()) {
                    NetErrorDialog.getInstance().showNetErrorDialog(mContext);
                    return;
                }

                if (item.getExamZips() == null) {
                    return;
                }
                List<ExamProgressVO> examProgressVOS = new ArrayList<>();
                for (int i = 0; i < item.getExamZips().size(); i++) {
                    ExamProgressVO examProgressVO = new ExamProgressVO();
                    examProgressVO.setId(item.getId());
                    examProgressVO.setExamId(item.getExamZips().get(i).getExamId());
                    examProgressVO.setZipPath(item.getExamZips().get(i).getZipPath());
                    examProgressVO.setType("exam");
                    examProgressVOS.add(examProgressVO);
                }
                AppContext.getInstance().setExamProgressVOS(examProgressVOS);
                RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_EXAM));
            }
        });

    }
}
