package com.hlxyedu.mhk.ui.ecomposition.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.event.EventsConfig;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.ecomposition.contract.TxtContract;
import com.hlxyedu.mhk.ui.ecomposition.presenter.TxtPresenter;
import com.hlxyedu.mhk.utils.CommonUtils;

import java.lang.reflect.Field;

/**
 * Created by zhangguihua
 */
public class TxtFragment extends RootFragment<TxtPresenter> implements TxtContract.View {

    private static final String TAG = TxtFragment.class.getSimpleName();
    private View view;
    //父 的布局
    private LinearLayout base_layout;
    // 结束页面控制显示隐藏
    private TextView waitText;
    private TextView successHintText;
    private Button finishBtn;
    //判断欢迎页面
    private boolean isWelcome = true;
    //数据中心
    private PageModel pageModel;

    //下一个数据
    private static final int NEXT_TRAIN = 0x05;
    //下一页
    private static final int NEXT_PAGE = 0x03;

    private String answer;


    public static TxtFragment newInstance() {
        Bundle args = new Bundle();

        TxtFragment fragment = new TxtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            //开始执行
            startTraining();
        } else {
        }
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public boolean isShow() {
        return isSupportVisible();
    }

    @Override
    public void commitSuccess() {
        waitText.setVisibility(View.GONE);
        successHintText.setVisibility(View.VISIBLE);
        finishBtn.setVisibility(View.VISIBLE);
        successHintText.setText("恭喜您该试卷已经顺利完成～\n" +
                "祝您取得理想的成绩！");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //在此创建但是 不显示
        switch (pageModel.getType())
        {
            //欢迎界面
            case PageModel.huanying:

                view = View.inflate(getActivity(), R.layout.fragment_test_layout,
                        null);
                base_layout = getView(view,R.id.base_layout);
                startTraining();
                break;
            case PageModel.WRITE_zuowen:

                view = View.inflate(getActivity(), R.layout.fragment_test_layout,
                        null);
                base_layout = getView(view,R.id.base_layout);
                break;

            case PageModel.jieshu:
                view = View.inflate(getActivity(), R.layout.fragment_test_finish,
                        null);
                waitText = getView(view, R.id.wait_text);
                successHintText = getView(view, R.id.success_hint_text);
                finishBtn = getView(view, R.id.finish_btn);
                finishBtn.setOnClickListener(v -> mActivity.finish());
                break;
        }
        return view;
    }

    @Override
    public void handleMessage(Message msg) {
        //不显示时 不影响倒计时


        if (!isVisible())
            return;

        super.handleMessage(msg);
        switch (msg.what) {
            case NEXT_PAGE:
                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE,EventsConfig.TEST_NEXT_PAGE,answer));
                break;
            case NEXT_TRAIN:
                startTraining();
                break;
        }
    }

    /**
     * 开始遍历数据
     */
    private void startTraining(){

        if(pageModel.getType().equals(PageModel.jieshu))
        {

            return;
        }

        if(pageModel.getType().equals(PageModel.huanying)&&isWelcome){
            isWelcome = false;
            return;
        }

        for(int i=0;i<pageModel.getBasePageModels().size();i++)
        {
            BasePageModel basePageModel = pageModel.getBasePageModels().get(i);
            if(!basePageModel.isShow())
            {
                //根据不同类型 去执行
                basePageModel.setShow(true);
                doTraining(basePageModel);
                return;
            }
        }

        if(!pageModel.getType().equals(PageModel.jieshu))
        {
            //下一个fragment
            Message message = new Message();
            message.what = NEXT_PAGE;
            getHandler().sendMessage(message);
        }

    }

    /**
     * 根据type去执行
     */
    private void doTraining(BasePageModel basePageModel){
        switch (basePageModel.getType())
        {
            case BasePageModel.TEXT:
                startTraining();
                break;
            case BasePageModel.RICHTEXT:
                if(basePageModel.getSuffix().equals("png"))
                {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_START);
                    LinearLayout.LayoutParams  layoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    base_layout.addView(imageView,layoutParams);
                    Glide.with(getContext())
                            .load(basePageModel.getSrc())
//                            .centerCrop()
//                            .dontTransform()
                            .into(imageView);

                    if (!StringUtils.isEmpty(basePageModel.getQuestionid())){
                        EditText editText = new EditText(getContext());
                        editText.setBackground(getResources().getDrawable(R.drawable.shape_edit));
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        editText.setGravity(Gravity.TOP);
                        editText.setPadding(10,10,10,10);
                        editText.setHeight(500);

                        LinearLayout.LayoutParams  layoutParams2 =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams2.setMargins(0,0,0,800);
                        base_layout.addView(editText,layoutParams2);

                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                                answer = s.toString();
                            }
                        });
                    }
                }

                startTraining();

                break;
            case BasePageModel.WAIT:
                Message message = new Message();
                message.what = NEXT_TRAIN;
                getHandler().sendMessageDelayed(message, CommonUtils.delayTime(basePageModel.getTimeout()));

                BaseEvents baseEvents = new BaseEvents(BaseEvents.NOTICE,EventsConfig.SHOW_DETAL_VIEW);
                baseEvents.setData(CommonUtils.delayTime(basePageModel.getTimeout())/1000);
                RxBus.getDefault().post(baseEvents);

                break;
            case BasePageModel.QUESTION:
                startTraining();
                break;
        }
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
    public void responeError(String errorMsg) {

    }

}