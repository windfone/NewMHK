package com.hlxyedu.mhk.ui.eread.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ScoreVO;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.event.EventsConfig;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.eread.contract.ReadContract;
import com.hlxyedu.mhk.ui.eread.presenter.ReadPresenter;
import com.hlxyedu.mhk.utils.CommonUtils;
import com.hlxyedu.mhk.utils.StringUtils;
import com.hlxyedu.mhk.weight.view.ListenQuestionItemView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by zhangguihua
 */
public class ReadFragment extends RootFragment<ReadPresenter> implements ReadContract.View, ListenQuestionItemView.onAnswerClickListener {

    private static final String TAG = ReadFragment.class.getSimpleName();
    //下一页
    private static final int NEXT_PAGE = 0x03;
    //下一个数据
    private static final int NEXT_TRAIN = 0x05;
    //整套试卷的 听力部分已经播放完毕，跳转 activity
    private static final int NEXT_PART = 0x07;
    private View view;
    //数据中心
    private PageModel pageModel;
    //父 的布局
    private LinearLayout base_layout;
    // 结束页面控制显示隐藏
    private TextView waitText;
    private TextView successHintText;
    private Button finishBtn;
    //判断欢迎页面
    private boolean isWelcome = true;

    private ArrayList<String> answers = new ArrayList<String>();

    private ArrayList<String> questions = new ArrayList<String>();

    //数组长度
    private int op_long = 0;

    private String type;

    public static ReadFragment newInstance() {
        Bundle args = new Bundle();

        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReadFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            //开始执行
            startTraining();
        }
    }

    @Override
    public boolean isShow() {
        return isSupportVisible();
    }

    @Override
    public void commitSuccess(ScoreVO scoreVO) {
        if (com.blankj.utilcode.util.StringUtils.equals(type, "考试")) {
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.TEST_NEXT_ACTIVITY));
        } else {
            waitText.setVisibility(View.GONE);
            successHintText.setVisibility(View.VISIBLE);
            finishBtn.setVisibility(View.VISIBLE);
            successHintText.setText("恭喜您该试卷已经顺利完成～\n" +
                    "您在该测试中，答对" + scoreVO.getRightcount() + "题，答错" + scoreVO.getWrongcount() + "题。\n" +
                    "答对的题号有第 " + scoreVO.getRightTitle() + " 题，" + "\n答错的题号有第 " + scoreVO.getWrongTitle() + " 题。");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = getArguments().getString("type");

        switch (pageModel.getType()) {
            //欢迎界面
            case PageModel.huanying:
                view = View.inflate(getActivity(), R.layout.fragment_test_layout,
                        null);
                base_layout = getView(view, R.id.base_layout);
                startTraining();
                break;
            case PageModel.READ_yuedulijie:
                view = View.inflate(getActivity(), R.layout.fragment_test_layout,
                        null);
                base_layout = getView(view, R.id.base_layout);
                break;

            case PageModel.jieshu:
                if (com.blankj.utilcode.util.StringUtils.equals(type, "考试")) {
                    view = View.inflate(getActivity(), R.layout.fragment_exam_finish, null);
                } else {
                    view = View.inflate(getActivity(), R.layout.fragment_test_finish, null);
                    waitText = getView(view, R.id.wait_text);
                    successHintText = getView(view, R.id.success_hint_text);
                    finishBtn = getView(view, R.id.finish_btn);
                    finishBtn.setOnClickListener(v -> {
                        mActivity.finish();
                    });
                }
                break;
        }
        return view;
    }

    /**
     * 开始遍历数据
     */
    private void startTraining() {
        // 第一次 2
        // 第二次 3
        //如果是结束页面 则直接返回
        if (pageModel.getType().equals(PageModel.jieshu)) {
            return;
        }

        if (pageModel.getType().equals(PageModel.huanying) && isWelcome) {
            isWelcome = false;
            return;
        }

        if (pageModel.getBasePageModels() != null) {
            for (int i = 0; i < pageModel.getBasePageModels().size(); i++) {
                BasePageModel basePageModel = pageModel.getBasePageModels().get(i);
                if (!basePageModel.isShow()) {
                    //根据不同类型 去执行
                    basePageModel.setShow(true);

                    doTraining(basePageModel);

                    return;
                }
            }
        }
        if (!pageModel.getType().equals(PageModel.jieshu)) {
//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                if (AppContext.getInstance().getCurrentItem() < AppContext.getInstance().getAllItem()-1){
//                    //下一个fragment
//                    Message message = new Message();
//                    message.what = NEXT_PAGE;
//                    getHandler().sendMessage(message);
//                }else {
//                    //下一个fragment
//                    Message message = new Message();
//                    message.what = NEXT_PART;
//                    getHandler().sendMessage(message);
//                }
//            }else {
            //下一个fragment
            Message message = new Message();
            message.what = NEXT_PAGE;
            getHandler().sendMessage(message);
//            }
        }

    }

    /**
     * 根据type去执行
     */
    private void doTraining(BasePageModel basePageModel) {
        switch (basePageModel.getType()) {
            case BasePageModel.TEXT:

                if (!StringUtils.isEmpty(basePageModel.getContent())) {
                    TextView textView = new TextView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    base_layout.addView(textView, layoutParams);
                    // textView.setPadding(80,0,80,0);
                    String title = null;

                    try {
                        title = basePageModel.getQuestionid().split("_")[1] + "." + "  " + basePageModel.getContent();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        title = basePageModel.getContent();
                    }
                    textView.setText(title);
                }

                startTraining();
                break;
            case BasePageModel.RICHTEXT:
                if (basePageModel.getSuffix().equals("png")) {
                    ImageView imageView = new ImageView(getContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    base_layout.addView(imageView, layoutParams);
                    Glide.with(getContext())
                            .load(basePageModel.getSrc())
                            .into(imageView);

                } else {
                    if (!StringUtils.isEmpty(basePageModel.getSrc())) {
                        TextView textView = new TextView(getContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        base_layout.addView(textView, layoutParams);
                        //textView.setPadding(80,40,80,0);
                        try {
                            FileInputStream fileInputStream = new FileInputStream(basePageModel.getSrc());
                            String title = StringUtils.toConvertString(fileInputStream);
                            textView.setText(title);
                        } catch (FileNotFoundException ex) {
                            LogUtils.d(TAG, "没找到文件" + basePageModel.getSrc());
                        } catch (UnsupportedEncodingException ex) {
                            LogUtils.d(TAG, "编码错误" + basePageModel.getSrc());
                        }
                    }
                }

                startTraining();
                break;
            case BasePageModel.SELECT:
                ListenQuestionItemView questionItemView = new ListenQuestionItemView(getContext());
                questionItemView.setData(basePageModel);
                questionItemView.setId(op_long);
                answers.add(op_long, "");
                questions.add(basePageModel.getQuestionid().substring(basePageModel.getQuestionid().length() - 2, basePageModel.getQuestionid().length())); // 将题号存起来
                op_long++;
                questionItemView.setAnswerClickListener(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                base_layout.addView(questionItemView, layoutParams);
                getView(questionItemView, R.id.question_title).setVisibility(View.GONE);
                //questionItemView.setPadding(80,0,80,80);
                startTraining();
                break;
            case BasePageModel.WAIT:
                Message message = new Message();
                message.what = NEXT_TRAIN;
//                getHandler().sendMessageDelayed(message, CommonUtils.delayTime(basePageModel.getTimeout()));
                getHandler().sendMessageDelayed(message, CommonUtils.delayTime("00:00:15"));

                BaseEvents baseEvents = new BaseEvents(BaseEvents.NOTICE, EventsConfig.SHOW_DETAL_VIEW);
//                baseEvents.setData(CommonUtils.delayTime(basePageModel.getTimeout()) / 1000);
                baseEvents.setData(CommonUtils.delayTime("00:00:15") / 1000);
                RxBus.getDefault().post(baseEvents);

                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        //不显示时 不影响倒计时
        if (!isVisible())
            return;

        super.handleMessage(msg);
        switch (msg.what) {
            case NEXT_PAGE:
                String data = "";
                if (answers != null) {
                    for (int i = 0; i < answers.size(); i++) {
                        data += questions.get(i) + "=" + answers.get(i) + "|";
                    }
                }
                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.TEST_NEXT_PAGE, data));
                break;
            case NEXT_TRAIN:
                startTraining();
                break;
            case NEXT_PART:
                String data1 = "";
                if (answers != null) {
                    for (int i = 0; i < answers.size(); i++) {
                        data1 += questions.get(i) + "=" + answers.get(i) + "|";
                    }
                }
                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.TEST_NEXT_PART, data1));
                break;
        }
    }

    @Override
    public void setAnswer(View view, String answer) {
        answers.set(view.getId(), answer);
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}