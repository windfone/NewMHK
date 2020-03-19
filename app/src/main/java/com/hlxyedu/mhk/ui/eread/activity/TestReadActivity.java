package com.hlxyedu.mhk.ui.eread.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragmentActivity;
import com.hlxyedu.mhk.ui.eread.contract.TestReadContract;
import com.hlxyedu.mhk.ui.eread.presenter.TestReadPresenter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.viewpager.NoTouchViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangguihua
 * 阅读理解（和书面表达 是一样的）
 */
public class TestReadActivity extends RootFragmentActivity<TestReadPresenter> implements TestReadContract.View {

    private static final String TAG = TestReadActivity.class.getSimpleName();

    private static final int SUCEESS = 0x01;

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.question_type_tv)
    TextView questionTypeTv;
    @BindView(R.id.notouch_vp)
    NoTouchViewPager viewPager;
    @BindView(R.id.countdown_tv)
    TextView countdownTv;
    @BindView(R.id.countdown_rl)
    RelativeLayout countdownRl;

//    //解析到的数据中心
//    private List<PageModel> pageModels;
//    //不能滑动的viewpager
//    private NoTouchViewPager viewPager;
//    //fragment 数组
//    private List<TestReadFragment> testReadFragments;
//
//    private TimerProgressBar timerProgressBar;

    private int currentItem = 0;

    public String answer = "";


    private String zipPath;// 压缩包路径
    private String fileName;// (压缩包名字 TLXXX.zip)也是解压后的文件夹名字 TLXXX.zip
    private String examId; // 试卷id
    private String homeworkId; // 作业id
    // 倒计时
    private int TIMER;
    private String from;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId) {
        Intent intent = new Intent(context, TestReadActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test_read;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}