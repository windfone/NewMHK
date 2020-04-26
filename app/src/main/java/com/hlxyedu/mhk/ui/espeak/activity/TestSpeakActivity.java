package com.hlxyedu.mhk.ui.espeak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fifedu.record.media.record.AudioPlayManager;
import com.fifedu.record.media.record.RecordSettingAdapter;
import com.fifedu.record.recinbox.bl.dal.AacFileWriter;
import com.fifedu.record.recinbox.bl.dal.SpeexFileWriter;
import com.fifedu.record.recinbox.bl.record.IRecorderListener;
import com.fifedu.record.recinbox.bl.record.RecordParams;
import com.fifedu.record.recinbox.bl.record.RecorderManager;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.base.RootFragmentActivity;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.event.EventsConfig;
import com.hlxyedu.mhk.model.event.ExitCommitEvent;
import com.hlxyedu.mhk.model.models.AnalyticXMLUtils;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.contract.TestSpeakContract;
import com.hlxyedu.mhk.ui.espeak.fragment.SpeakFragment;
import com.hlxyedu.mhk.ui.espeak.presenter.TestSpeakPresenter;
import com.hlxyedu.mhk.ui.exam.activity.ExamFinishActivity;
import com.hlxyedu.mhk.utils.MyFragmentPagerAdapter;
import com.hlxyedu.mhk.utils.WeakReferenceHandler;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.hlxyedu.mhk.weight.view.WaveView;
import com.hlxyedu.mhk.weight.viewpager.NoTouchViewPager;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.app.AppConstants;
import com.skyworth.rxqwelibrary.utils.RxTimerUtil;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import butterknife.BindView;

/**
 * Created by zhangguihua
 * 口语练习
 */
public class TestSpeakActivity extends RootFragmentActivity<TestSpeakPresenter> implements
        TestSpeakContract.View, XBaseTopBarImp, IRecorderListener {

    private static final String TAG = TestSpeakActivity.class.getSimpleName();
    private static final int MSG_START_RECORD = 10;
    private static final int MSG_STOP_RECORD = 11;
    private static final int MSG_VOLUME = 12;
    private static final int MSG_START_OK = 13;
    private static final int MSG_START_ERROR = 14;
    private static final int MSG_FINISH = 15;
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
    @BindView(R.id.waveview)
    WaveView waveview;
    //解析到的数据中心
    private List<PageModel> pageModels;
    //fragment 数组
    private List<SpeakFragment> speakFragments;
    private int currentItem = 0;
    // 录音控件
    private RecorderManager mRecorder;
    private Handler mRecordHandler;
    // aac音频文件
    private AacFileWriter mAacFile;
    // speex 音频文件
    private SpeexFileWriter mSpeexFile;
    // 用户文件夹
    private String recordPath = "";
    private String recordUrl = "";
    private String mEvaluateFilePath;
    private double MAX_VOL = 20000;

    private long mBeginTime;// 录制开始时间

    private String from;

    private String examId; // 试卷id
    private String homeworkId; // 作业id
    private String testId; // 考试id，练习和作业时此字段为空
    //(接口实际字段为type，但是用type和CommitEvent中 RxBus类型冲突，所以改了名字，接口名字还是type)类型：homeWork/作业，exam/考试，练习时也传空
    private String testType;


    private String zipPath;// 压缩包路径
    private String fileName;// (压缩包名字 TLXXX.zip)也是解压后的文件夹名字 TLXXX.zip
    private String recordName;// 录音文件名字
    private String userDir; // 录音文件夹

    // 倒计时
    private RxTimerUtil rxTimer;
    private int TIMER;

    private int currentPos; // 当前是第几个答题包


    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from) {
        Intent intent = new Intent(context, TestSpeakActivity.class);
        intent.putExtra("from", from);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId) {
        Intent intent = new Intent(context, TestSpeakActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId, String homeworkId, String testType) {
        Intent intent = new Intent(context, TestSpeakActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        intent.putExtra("homeworkId", homeworkId);
        intent.putExtra("testType", testType);
        return intent;
    }

    private void loadDataAndRefreshView() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        xbaseTopbar.setMiddleText(from);
        zipPath = intent.getStringExtra("zipPath");
        fileName = intent.getStringExtra("fileName");
        examId = intent.getStringExtra("examId");
        homeworkId = intent.getStringExtra("homeworkId");
        testType = intent.getStringExtra("testType");
//        questionTypeTv.setText("口语模拟大礼包");
        questionTypeTv.setText("2020年MHK模拟考试");

        if (from.equals("考试")) {
            currentPos = AppContext.getInstance().getCurrentPos();
            examId = AppContext.getInstance().getExamProgressVOS().get(currentPos).getExamId();
            testId = AppContext.getInstance().getExamProgressVOS().get(currentPos).getId();
            testType = AppContext.getInstance().getExamProgressVOS().get(currentPos).getType();

            String names = AppContext.getInstance().getExamProgressVOS().get(currentPos).getZipPath();
            String[] strs = names.split("/");
            names = AppConstants.FILE_DOWNLOAD_PATH + strs[strs.length - 1];
            zipPath = names;
            fileName = strs[strs.length - 1];

        }

        // 解压文件
        UnZipAsyncTask unZipAsyncTask = new UnZipAsyncTask();
        unZipAsyncTask.execute();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        // 保持屏幕唤醒状态
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        stateLoading();
        xbaseTopbar.setxBaseTopBarImp(this);

        rxTimer = new RxTimerUtil();

        pageModels = new ArrayList<PageModel>();
        speakFragments = new ArrayList<SpeakFragment>();
        viewPager.setNoScroll(true);
        loadDataAndRefreshView();

        // 获取录音组件实例
        mRecorder = RecorderManager.getInstance();
        mRecordHandler = new RecordHandler(this);
        //1.需要创建这套题的答题包录音文件夹
        userDir = AppConstants.RECORD_PATH
                + fileName.substring(0, fileName.length() - 4) + File.separator;
        LogUtils.d(TAG, "创建录音文件夹");
        FileUtils.createOrExistsDir(userDir);
    }

    private void clearTimeProgress() {
        countdownRl.setVisibility(View.GONE);
        rxTimer.cancel();
    }

    private void startTimeProgress(int time) {
        TIMER = time;
        rxTimer.interval(1000, number -> {
            TIMER--;
            if (TIMER == 0) {
                countdownTv.setText("");
                countdownRl.setVisibility(View.GONE);
                rxTimer.cancel();
                // 下一题
            } else {
                countdownRl.setVisibility(View.VISIBLE);
                countdownTv.setText(TIMER + "S");
            }
        });
    }

    @Override
    public void onMainEvent(BaseEvents event) {
        switch (event.getValue()) {
            case EventsConfig.TEST_NEXT_PAGE:
                clearTimeProgress();
                viewPager.setCurrentItem(++currentItem);
                AppContext.getInstance().setCurrentItem(currentItem);

                // 练习和作业 结束的页面
                if (currentItem == speakFragments.size() - 1) {
                    RxBus.getDefault().post(new CommitEvent(CommitEvent.COMMIT,"","", userDir, examId, homeworkId, testId, testType));
                }
                break;
            case EventsConfig.SHOW_DETAL_VIEW:
                clearTimeProgress();
                int time = (int) event.getData();

                // 暂停时间太短 1秒不显示倒计时时间
                if (time > 1) {
//                    countdownRl.setVisibility(View.VISIBLE);
                    startTimeProgress(time);
                }
                break;
            case EventsConfig.TEST_NEXT_ACTIVITY:
                // 考试 模块是多个答题压缩包，答完一个接下一个
                if (currentPos == AppContext.getInstance().getExamProgressVOS().size() - 1) {
                    //TODO 如果是最后一个，则跳转到一个专门的 考试模块的结束页面
                    startActivity(ExamFinishActivity.newInstance(this));
                } else {
                    // TODO 如果不是最后一个答题包，则跳转到 下一套类型的试卷继续考试
                    AppContext.getInstance().setCurrentPos(++currentPos);
                    String names = AppContext.getInstance().getExamProgressVOS().get(currentPos).getZipPath();
                    if (names.contains("TL")) {
                        mContext.startActivity(TestListeningActivity.newInstance(mContext, "考试"));
                    } else if (names.contains("KY") || names.contains("LD")) {
                        mContext.startActivity(TestSpeakActivity.newInstance(mContext, "考试"));
                    } else if (names.contains("YD")) {
                        mContext.startActivity(TestReadActivity.newInstance(mContext, "考试"));
                    } else if (names.contains("SM")) {
                        mContext.startActivity(TestBookActivity.newInstance(mContext, "考试"));
                    } else if (names.contains("ZW")) {
                        mContext.startActivity(TestTxtActivity.newInstance(mContext, "考试"));
                    }
                }
                finish();
                break;
            case EventsConfig.START_RECORD:
                //waveformView.setVisibility(View.VISIBLE);
                //recordingLayout.setVisibility(View.VISIBLE);
                recordName = fileName.substring(0, fileName.length() - 4) + "_" + (String) event.getQuestionId() + AppConstants.AUDIO_FILE_SUFFIX;
                int recordtime = (int) event.getData() + 1;
                waveview.set_recordlength(recordtime);
                mRecordHandler.sendEmptyMessage(MSG_START_RECORD);
                waveview.setVisibility(View.VISIBLE);

                break;
            case EventsConfig.END_RECORD:
                mRecordHandler.sendEmptyMessage(MSG_STOP_RECORD);
                waveview.setVisibility(View.GONE);
                waveview.clear();
                break;
            case EventsConfig.KILL_ACTIVITY:
//                if(event.getData().equals("end")){
//                    submitState();
//                }
                break;
            //获取数据 并且 解析成功
            case EventsConfig.SUCCESS_SPEAK:
                AppContext.getInstance().setAllItem(pageModels.size());
                if (from.equals("考试")) {
                    for (int i = 0; i < pageModels.size(); i++) {
                        SpeakFragment speakFragment = SpeakFragment.newInstance("考试");
                        speakFragment.setPageModel(pageModels.get(i));
                        speakFragments.add(speakFragment);
                    }
                } else {
                    for (int i = 0; i < pageModels.size(); i++) {
                        SpeakFragment speakFragment = SpeakFragment.newInstance();
                        speakFragment.setPageModel(pageModels.get(i));
                        speakFragments.add(speakFragment);
                    }
                }

                viewPager.setAdapter(new MyFragmentPagerAdapter(
                        getSupportFragmentManager(), speakFragments));
                stateMain();
                break;
//            case EventsConfig.TEST_NEXT_PART:
//
//                openActivity(TestReadActivity_.class);
//                killActivity(this);
//                break;
        }
    }

    // ********************** 录音部分 ************************** //
//    @Override
    public void recordSecond() {
        //开启录音
        mRecordHandler.sendEmptyMessage(MSG_START_RECORD);
    }

    /**
     * 录音开启
     */
    private void onStartRecord() {
        // 创建Recorder
        if (!mRecorder.isRecording()) {
            RecordParams params = new RecordParams();
            mRecorder.startRecord(this, params, RecordSettingAdapter.getInstance());
//            record_tv.setVisibility(View.GONE);
//            record_iv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 停止录音
     *
     * @param msg
     * @param isClick
     */
    private void onStopRecord(String msg, boolean isClick) {
        if (isClick) {
            mRecorder.stopRecord(this);
//            record_iv.setVisibility(View.GONE);
//            record_tv.setVisibility(View.VISIBLE);
//            ToastUtils.showShort("录音完成");
        } else {
            mRecorder.stopRecord(null);
        }
    }

    /**
     * 录音结束
     */
    public void onMsgFinish() {
//        record_view.setVisibility(View.GONE);
//        waveview.clear();
//        ToastUtils.showShort("录音完成");
    }

    public void onMsgError(int arg1) {
        LogUtils.d(TAG, "onMsgError", "---->onMsgError");
        onStopRecord("开始录音出错", false);
        ToastUtils.showShort("录音发生错误");
    }

    public void onMsgStart(RecordParams params) {
//        mBeginTime = System.currentTimeMillis();
    }

    @Override
    public boolean onStart(RecordParams params) {
        Message msg = mRecordHandler.obtainMessage(MSG_START_OK);
        try {
            if (null != mAacFile) {
                mAacFile.close();
            }
            mAacFile = new AacFileWriter();
            String file = getAudioFile();
            mAacFile.open(file);
            mAacFile.init(params.getSampleRate());

            if (null != mSpeexFile) {
                mSpeexFile.close();
            }
            // 评测文件保存
            if (!TextUtils.isEmpty(mEvaluateFilePath)) {
                mSpeexFile = new SpeexFileWriter();
                mSpeexFile.open(mEvaluateFilePath);
                mSpeexFile.init(params.getSampleRate());
            }
            params.setFileId(file);
            msg.obj = params;
            mRecordHandler.sendMessage(msg);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private String getAudioFile() {
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.CHINESE);
//        recordName = "record_" + simpleDateFormat.format(date) + AppConstants.AUDIO_FILE_SUFFIX;
//        String ret = AppConstants.RECORD_PATH + recordName;
        String ret = userDir + recordName;
        recordUrl = ret;
        return ret;
    }

    @Override
    public void onError(RecordParams params, int errorCode) {
        if (null != mAacFile) {
            try {
                mAacFile.close();
            } catch (IOException e) {
                LogUtils.d("", "", e.toString());
            }
            mAacFile = null;
        }

        if (null != mSpeexFile) {
            try {
                mSpeexFile.close();
            } catch (IOException e) {
                LogUtils.d("", "", e.toString());
            }
            mSpeexFile = null;
        }
        Message msg = mRecordHandler.obtainMessage(MSG_START_ERROR);
        msg.arg1 = errorCode;
        mRecordHandler.sendMessage(msg);
    }

    @Override
    public int onRecordData(byte[] data) {
        if (null != mAacFile) {
            mAacFile.appendPcmData(data, data.length);
        }
        if (null != mSpeexFile) {
            mSpeexFile.appendPcmData(data, data.length, false);
        }

        //add weidingqiang 将byte[] 转成 short[]  类型
        short[] shorts = new short[data.length / 2];
        ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        //进行显示
//        waveformView.setSamples(shorts);
        Message msg = mRecordHandler.obtainMessage(MSG_VOLUME);
        msg.arg1 = getDataVolume(data);
        mRecordHandler.sendMessage(msg);
        return data.length;
    }

    private int getDataVolume(byte[] data) {
        // 计算最大值
        short tmpMax = 0;
        for (int i = 0; i < data.length - 1; i += 2) {
            short value = (short) ((data[i + 1] << 8) + data[i]);
            if (tmpMax < value) {
                tmpMax = value;
            }
        }
        // Log.d("Record","getDataVolume " + ret);
        return tmpMax;
    }

    @Override
    public void onRecordInterrupt() {

    }

    @Override
    public void onFinished(RecordParams params) {
        if (null != mAacFile) {
            try {
                mAacFile.close();
            } catch (IOException e) {
                LogUtils.d("", "", e.toString());
            }
            mAacFile = null;
        }
        if (null != mSpeexFile) {
            try {
                // 结束录音
                byte[] data = {};
                mSpeexFile.appendPcmData(data, 0, true);
                mSpeexFile.close();
            } catch (IOException e) {
                LogUtils.d("", "", e.toString());
            }
            mSpeexFile = null;
        }
        Message msg = mRecordHandler.obtainMessage(MSG_FINISH);
        mRecordHandler.sendMessage(msg);

    }

    // ********************** 录音部分 ************************** //

    //正式加载数据
    private void loadData() {

        try {

            Intent intent = getIntent();

//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                zipfilename = AppContext.getInstance().getZipfilename();
//                AppContext.getInstance().setCurrentItem(currentItem);
//            }else {
//                zipfilename = intent.getStringExtra("filename");
//            }

            if (intent.getStringExtra("answer") != null && intent.getStringExtra("name") != null) {

            }

            String filename = AppConstants.UNFILE_DOWNLOAD_PATH + fileName + File.separator + "TestPaper.xml";


            SAXReader saxReader = new SAXReader();

            Document document = saxReader.read(new InputStreamReader(new FileInputStream(filename), "gb2312"));

            // 获取根元素
            Element root = document.getRootElement();
            //System.out.println("Root: " + root.getName());

            // 获取所有子元素
            List<Element> childList = root.elements();
            //System.out.println("total child count: " + childList.size());

            List<Element> parts = null;
//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                parts = root.elements("part").subList(2,5);
//            }else {
            // 获取特定名称的子元素
            parts = root.elements("part");
//            }

            for (int i = 0; i < parts.size(); i++) {
                //解析part 部分

                Element part = parts.get(i);
                String partName = part.attributeValue("name");

                List<Element> sections = part.elements("section");
                Element section = sections.get(0);
                List<Element> pages = section.elements("page");

                for (int j = 0; j < pages.size(); j++) {
                    Element page = pages.get(j);

                    //一起解析比较难分辨，所以拆分解析
                    //1.解析欢迎数据
                    switch (partName) {
                        case PageModel.huanying:
                        case PageModel.jieshu:
                            if (j == 0) {
                                AnalyticXMLUtils.encodeWelcomeOrEndPageModel(pageModels, fileName, page, partName, "");
                            }
                            break;
                        case PageModel.SPEAK_langduduanwen:
                            encodeMiddlePageModel(page, partName);
                            break;
                        case PageModel.SPEAK_huidawenti:
                            encodeMiddlePageModel(page, partName);
                            break;
                    }
                }

            }


        } catch (DocumentException e) {
//            contentPage.mState = ContentPage.PageState.STATE_ERROR;
//            contentPage.showPage();

        } catch (UnsupportedEncodingException ex) {
//            contentPage.mState = ContentPage.PageState.STATE_ERROR;
//            contentPage.showPage();
        } catch (FileNotFoundException ex) {
//            contentPage.mState = ContentPage.PageState.STATE_ERROR;
//            contentPage.showPage();
        }

    }

    //开始解压文件
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void unZip(String zipFileName, String outputDirectory) {
        try {
            ZipFile zipFile = new ZipFile(zipFileName, Charset.forName("GBK"));
            Enumeration e = zipFile.entries();
            ZipEntry zipEntry = null;
//            createDirectory(outputDirectory, "");
            FileUtils.createOrExistsDir(outputDirectory);
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0,
                                fileName.lastIndexOf("/")));

                        fileName = fileName.substring(
                                fileName.lastIndexOf("/") + 1,
                                fileName.length());
                    }

                    File f = new File(outputDirectory + File.separator
                            + zipEntry.getName());

                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);

                    byte[] by = new byte[1024];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    out.close();
                    in.close();
                }
            }
        } catch (Exception e) {
            // 解压出现异常
            Toast.makeText(this, "解压出现异常", Toast.LENGTH_LONG).show();
        }
    }

    private void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true)
                fl.mkdir();
            else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            // 创建文件夹出现异常
            Toast.makeText(this, "创建文件夹出现异常", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * 解析第二部分
     *
     * @param page
     * @param type
     */
    private void encodeMiddlePageModel(Element page, String type) {
        //以每个page 为一页

        int current = 0;

        //循环遍历
        List<Element> elements = page.elements();

        //初始化
        PageModel pageModel = new PageModel();
        pageModel.setType(type);

        //1.先解析开始部分，和欢迎部分是一样的
        for (int i = 0; i < elements.size(); i++) {

            Element element = elements.get(i);
            BasePageModel basePageModel = new BasePageModel();
            basePageModel.setCurrent(current++);
            basePageModel.setType(element.getName());

            switch (element.getName()) {
                case BasePageModel.TEXT:
                    basePageModel.setContent(element.getStringValue());
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.RICHTEXT:
                    basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.WAIT:
                    basePageModel.setTimeout(element.attributeValue("timeout"));
                    pageModel.addVO(basePageModel);
                    break;
                //2.如果有Question则为新一个page
                case BasePageModel.QUESTION:
                    //question 中会有其他参数
                    List<Element> questions = element.elements();

                    String qustionid = element.attributeValue("id");

                    pageModel.setHasquestion(true);

                    for (int j = 0; j < questions.size(); j++) {
                        Element question = questions.get(j);
                        BasePageModel questionModel = new BasePageModel();
                        questionModel.setCurrent(current++);
                        questionModel.setType(question.getName());

                        questionModel.setQuestionid(qustionid);

                        switch (question.getName()) {
                            case BasePageModel.TEXT:
                                questionModel.setContent(question.getStringValue());
                                pageModel.addVO(questionModel);
                                break;
                            case BasePageModel.RICHTEXT:
                                questionModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, question.attributeValue("src"), false));
                                pageModel.addVO(questionModel);
                                break;
                            case BasePageModel.AUDIO:
                                questionModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, question.attributeValue("src"), false));

                                pageModel.addVO(questionModel);
                                break;
                            case BasePageModel.WAIT:
                                questionModel.setTimeout(question.attributeValue("timeout"));
                                pageModel.addVO(questionModel);
                                break;
                            case BasePageModel.RECORD:
                                questionModel.setTimeout(question.attributeValue("timeout"));
                                pageModel.addVO(questionModel);
                                break;
                        }

                    }

                    break;
                case BasePageModel.AUDIO:
                    basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    pageModel.addVO(basePageModel);
                    break;
            }
        }

        pageModels.add(pageModel);
    }

    /**
     * 音量变化
     *
     * @param volume
     */
    private void onVolumeUi(int volume) {
        // 话筒高70dp
        double rate = volume / MAX_VOL;
        if (rate > 1) {
            rate = 1;
        }
//        LayoutParams lp = (LayoutParams) recordImgBg.getLayoutParams();
//        LayoutParams lpCopy = (LayoutParams) recordImgBgCopy.getLayoutParams();
//        lp.height = (int) (lpCopy.height * rate);
//        recordImgBg.setLayoutParams(lp);

        waveview.setHeightRect(rate);
    }

    @Override
    protected void onStop() {
        AudioPlayManager.getManager().stop();
        super.onStop();
    }

    //--------------------------------------------------------------------------------------------------//

    @Override
    protected void onDestroy() {
        mRecordHandler.removeCallbacksAndMessages(null);
//        mRecorder.destroy();
        rxTimer.cancel();
        super.onDestroy();
    }


    //--------------------------------------------------------------------------------------------------//

    @Override
    public void responeError(String errorMsg) {

    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test_speak;
    }

    @Override
    public void onBackPressedSupport() {
        setBackHint();
//        mMaterialDialog.show();
    }

    @Override
    public void left() {
//        mMaterialDialog.show();
        setBackHint();
    }

    private DialogPlus mMaterialDialog;
    private void setBackHint() {
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        mMaterialDialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_logout))
                .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                .setContentWidth((int) (display
                        .getWidth() * 0.8))
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)//设置不可取消   可以取消
                .setOnClickListener((dialog, view1) -> {
                    switch (view1.getId()) {
                        case R.id.btn_neg:
                            dialog.dismiss();
                            break;
                        case R.id.btn_pos:
                            dialog.dismiss();
                            finish();
                            break;
                    }
                }).create();
        TextView textView = (TextView) mMaterialDialog.findViewById(R.id.txt_msg);
        textView.setText("是否退出？");
        mMaterialDialog.show();
    }

    @Override
    public void right() {

    }

    public static class RecordHandler extends WeakReferenceHandler<TestSpeakActivity> {

        public RecordHandler(TestSpeakActivity activity) {
            super(activity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            TestSpeakActivity mForm;
            mForm = getRef();
            switch (msg.what) {
                case MSG_START_RECORD:
                    mForm.onStartRecord();
                    break;
                case MSG_STOP_RECORD:
                    mForm.onStopRecord("录音停止", true);
                    break;
                case MSG_VOLUME:
                    mForm.onVolumeUi(msg.arg1);
                    break;
                case MSG_FINISH:
                    mForm.onMsgFinish();
                    break;
                case MSG_START_ERROR:
                    mForm.onMsgError(msg.arg1);
                    break;
                case MSG_START_OK:
                    mForm.onMsgStart((RecordParams) msg.obj);
                    break;
            }
        }
    }

    private class UnZipAsyncTask extends AsyncTask<Void, Integer, Void> {

        public UnZipAsyncTask() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // 解压完成
            //加载数据
            DecodeAsyncTask unZipAsyncTask = new DecodeAsyncTask();
            unZipAsyncTask.execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //解压地址
            unZip(zipPath, AppConstants.UNFILE_DOWNLOAD_PATH + fileName);
            return null;
        }
    }

    private class DecodeAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.SUCCESS_SPEAK));
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }
    }

}