package com.hlxyedu.mhk.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.model.bean.TotalScoreVO;
import com.hlxyedu.mhk.ui.mine.contract.GradeContract;
import com.hlxyedu.mhk.ui.mine.presenter.GradePresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangguihua
 */
public class GradeActivity extends RootActivity<GradePresenter> implements GradeContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.listening_tv)
    TextView listeningTv;
    @BindView(R.id.read_tv)
    TextView readTv;
    @BindView(R.id.book_tv)
    TextView bookTv;
    @BindView(R.id.speak_tv)
    TextView speakTv;
    @BindView(R.id.write_tv)
    TextView writeTv;
    @BindView(R.id.view_main)
    RelativeLayout viewMain;
    @BindView(R.id.listening_all_tv)
    TextView listeningAllTv;
    @BindView(R.id.read_all_tv)
    TextView readAllTv;
    @BindView(R.id.book_all_tv)
    TextView bookAllTv;
    @BindView(R.id.finished_pager_tv)
    TextView finishedPagerTv;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, GradeActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_grade;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        xbaseTopbar.setxBaseTopBarImp(this);
        stateLoading();
        mPresenter.getTotalScore();
    }

    /*String start = "听力理解答对题数：";
        String end = "81题";
        String str;
        str = start + end;
        final SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#EA6651")), start.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

    @Override
    public void responeError(String errorMsg) {
        stateError();
    }

    @Override
    public void success(TotalScoreVO totalScoreVO) {
        stateMain();
        finishedPagerTv.setText("已做试卷：" + totalScoreVO.getExamCount() + "套");

        listeningTv.setText(totalScoreVO.getTlRightCount() + "题");
        listeningAllTv.setText(totalScoreVO.getTlCount() + "题");

        readTv.setText(totalScoreVO.getYdRightCount() + "题");
        readAllTv.setText(totalScoreVO.getYdCount() + "题");

        bookTv.setText(totalScoreVO.getSmRightCount() + "题");
        bookAllTv.setText(totalScoreVO.getSmCount() + "题");

        speakTv.setText(totalScoreVO.getKyCount() + "题");

        writeTv.setText(totalScoreVO.getZwCount() + "题");
    }

    @Override
    public void left() {
        finish();
    }

    @Override
    public void right() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}