package com.hlxyedu.mhk.ui.espeak.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.fifedu.record.media.record.AudioPlayManager;
import com.fifedu.record.media.record.AudioplayInterface;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.event.EventsConfig;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.espeak.contract.SpeakContract;
import com.hlxyedu.mhk.ui.espeak.presenter.SpeakPresenter;
import com.hlxyedu.mhk.utils.CommonUtils;
import com.hlxyedu.mhk.utils.FileUtil;
import com.hlxyedu.mhk.utils.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhangguihua
 */
public class SpeakFragment extends RootFragment<SpeakPresenter> implements SpeakContract.View {

    //等待完毕
    private static final int WAIT_FINISH = 0x01;
    //录音完毕
    private static final int RECORD_SUCCESS = 0x02;

    //下一页
    private static final int NEXT_PAGE = 0x03;

    //播放下一个音频
    private static final int PLAY_NEXT = 0x04;

    //下一个数据
    private static final int NEXT_TRAIN = 0x05;

    //整套试卷的 听力部分已经播放完毕，跳转 activity
    private static final int NEXT_PART = 0x07;
    private static final String TAG = SpeakFragment.class.getSimpleName();
    //父 的布局
    private LinearLayout base_layout;
    // 结束页面控制显示隐藏
    private TextView waitText;
    private TextView successHintText;
    private Button finishBtn;
    //private TextView doc_text_question;
    private Button compete_btn;
    private View view;
    //数据中心
    private PageModel pageModel;

    //是否有要播放的音频
    private Boolean hasplayaudio = false;
    // 音频播放管理
    private AudioPlayManager _audioManager;
    //第一次遍历数组
    private boolean isfirst = true;
    //判断欢迎页面
    private boolean isWelcome = true;

    private String type;

    private AudioplayInterface audioPlayListen = new AudioplayInterface() {

        @Override
        public void onerror(String str) {
            Toast.makeText(getContext(), "音频播放错误", Toast.LENGTH_LONG).show();
        }

        @Override
        public void complete() {
            startTraining();
        }

        @Override
        public void interupt() {
        }

    };

    public static SpeakFragment newInstance() {
        Bundle args = new Bundle();

        SpeakFragment fragment = new SpeakFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SpeakFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type",type);
        SpeakFragment fragment = new SpeakFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            startTraining();
        } else {
            stopPlaying();
        }
    }

    @Override
    public boolean isShow() {
        return isSupportVisible();
    }

    @Override
    public void commitSuccess() {
        if (com.blankj.utilcode.util.StringUtils.equals(type,"考试")){
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.TEST_NEXT_ACTIVITY));
        }else {
            waitText.setVisibility(View.GONE);
            successHintText.setVisibility(View.VISIBLE);
            finishBtn.setVisibility(View.VISIBLE);
            successHintText.setText("恭喜您该试卷已经顺利完成～\n" +
                    "祝您取得理想的成绩！");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = getArguments().getString("type");

        //在此创建但是 不显示
        switch (pageModel.getType()) {
            //欢迎界面
            case PageModel.huanying:
                view = View.inflate(getActivity(), R.layout.fragment_test_layout, null);
                base_layout = getView(view, R.id.base_layout);
                startTraining();
                break;
            case PageModel.SPEAK_huidawenti:
            case PageModel.SPEAK_langduduanwen:
                view = View.inflate(getActivity(), R.layout.fragment_test_layout, null);
                base_layout = getView(view, R.id.base_layout);
                break;

            case PageModel.jieshu:
                if (com.blankj.utilcode.util.StringUtils.equals(type,"考试")){
                    view = View.inflate(getActivity(), R.layout.fragment_exam_finish, null);
                }else {
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

        if (pageModel.getType().equals(PageModel.jieshu)) {
            return;
        }

        if (pageModel.getType().equals(PageModel.huanying) && isWelcome) {
            isWelcome = false;
            return;
        }

        //第一次遍历
        if (isfirst) {

            for (int k = 0; k < pageModel.getBasePageModels().size(); k++) {
                BasePageModel basePageModelk = pageModel.getBasePageModels().get(k);
                if (basePageModelk.getType().equals(BasePageModel.TEXT) && !StringUtils.isEmpty(basePageModelk.getContent())) {
                    TextView textView = new TextView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    base_layout.addView(textView, layoutParams);
                    textView.setText(basePageModelk.getContent());
                    //textView.setPadding(80,0,80,0);
                    String title = null;

                    try {
                        title = basePageModelk.getQuestionid().split("_")[1] + "." + "  " + basePageModelk.getContent();
                    } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
                        title = basePageModelk.getContent();
                    }
                    textView.setText(title);
                    basePageModelk.setShow(true);
                }
                if (!basePageModelk.getSuffix().equals("png") && basePageModelk.getType().equals(BasePageModel.RICHTEXT) && !StringUtils.isEmpty(basePageModelk.getSrc())) {
                    if (base_layout != null) {
                        TextView textView = new TextView(getContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        base_layout.addView(textView, layoutParams);
                        //textView.setPadding(80,40,80,0);
                        try {
                            FileInputStream fileInputStream = new FileInputStream(basePageModelk.getSrc());
                            String title = StringUtils.toConvertString(fileInputStream);
                            textView.setText(title);
                        } catch (FileNotFoundException ex) {
                            LogUtils.d(TAG, "没找到文件" + basePageModelk.getSrc());
                        } catch (UnsupportedEncodingException ex) {
                            LogUtils.d(TAG, "编码错误" + basePageModelk.getSrc());
                        }
                    }
                }
            }
            isfirst = false;
        }


        for (int i = 0; i < pageModel.getBasePageModels().size(); i++) {
            BasePageModel basePageModel = pageModel.getBasePageModels().get(i);
            if (!basePageModel.isShow()) {
                //根据不同类型 去执行
                basePageModel.setShow(true);
                doTraining(basePageModel);
                return;
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
                }
                startTraining();

                break;

            case BasePageModel.AUDIO:
                playAudio(basePageModel.getSrc());
                break;
            case BasePageModel.WAIT:
                Message message = new Message();
                message.what = NEXT_TRAIN;
                getHandler().sendMessageDelayed(message, CommonUtils.delayTime(basePageModel.getTimeout()));

                BaseEvents baseEvents = new BaseEvents(BaseEvents.NOTICE, EventsConfig.SHOW_DETAL_VIEW);
                baseEvents.setData(CommonUtils.delayTime(basePageModel.getTimeout()) / 1000);
                RxBus.getDefault().post(baseEvents);

                break;
            case BasePageModel.QUESTION:
                startTraining();
                break;
            case BasePageModel.RECORD:

                String str = basePageModel.getQuestionid();
                String[] strs = str.split("_");
                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.START_RECORD, CommonUtils.delayTime(basePageModel.getTimeout()) / 1000, strs[strs.length-1]));

                Message recordmessage = new Message();
                recordmessage.what = RECORD_SUCCESS;
                getHandler().sendMessageDelayed(recordmessage, CommonUtils.delayTime(basePageModel.getTimeout()));

                break;
        }
    }

    /**
     * 播放
     */
    private void playAudio(final String audioname) {
        if (_audioManager == null) {
            _audioManager = AudioPlayManager.getManager();
        }
        if (FileUtil.isFileExist(audioname)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _audioManager.startPlay(audioname, audioname, audioPlayListen, getView());
                }
            }, 1000); // 這裏加上延遲，因爲如果是綜合測試， TestListenActivity中 onStop方法 執行的稍慢，會中斷播放
        } else {
            LogUtils.d(TAG, "没找到音频文件  " + audioname);
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
                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.TEST_NEXT_PAGE));
                break;
            case NEXT_PART:
                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.TEST_NEXT_PART));
                break;
            case PLAY_NEXT:
                break;
            case RECORD_SUCCESS:

                RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.END_RECORD));
                startTraining();

                break;
            case WAIT_FINISH:
                break;
            case NEXT_TRAIN:
                startTraining();
                break;
        }
    }

    public void stopPlaying() {
        if (_audioManager != null) {
            _audioManager.stop();
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
    protected void initEventAndData() {

    }

    @Override
    public void responeError(String errorMsg) {

    }

}