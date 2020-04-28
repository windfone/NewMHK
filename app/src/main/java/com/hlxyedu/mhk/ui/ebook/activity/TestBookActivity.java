package com.hlxyedu.mhk.ui.ebook.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.base.RootFragmentActivity;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.event.EventsConfig;
import com.hlxyedu.mhk.model.event.ExitCommitEvent;
import com.hlxyedu.mhk.model.event.ReExamEvent;
import com.hlxyedu.mhk.model.models.AnalyticXMLUtils;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.ListenQOptionModel;
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.ebook.contract.TestBookContract;
import com.hlxyedu.mhk.ui.ebook.fragment.BookFragment;
import com.hlxyedu.mhk.ui.ebook.presenter.TestBookPresenter;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.hlxyedu.mhk.ui.exam.activity.ExamFinishActivity;
import com.hlxyedu.mhk.utils.MyFragmentPagerAdapter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.hlxyedu.mhk.weight.viewpager.NoTouchViewPager;
import com.lansosdk.videoeditor.LanSoEditor;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.libyuv.LibyuvUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.app.AppConstants;
import com.skyworth.rxqwelibrary.utils.RxCountDown;
import com.skyworth.rxqwelibrary.utils.RxTimerUtil;
import com.zhaoss.weixinrecorded.util.CameraHelp;
import com.zhaoss.weixinrecorded.util.MyVideoEditor;
import com.zhaoss.weixinrecorded.util.RecordUtil;
import com.zhaoss.weixinrecorded.util.RxJavaUtil;
import com.zhaoss.weixinrecorded.util.Utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import butterknife.BindView;

/**
 * Created by zhangguihua
 * 书面表达（和阅读理解 一样）
 */
public class TestBookActivity extends RootFragmentActivity<TestBookPresenter> implements TestBookContract.View, XBaseTopBarImp {

    private static final String TAG = TestBookActivity.class.getSimpleName();
    public String answer = "";
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
    //解析到的数据中心
    private List<PageModel> pageModels;
    //fragment 数组
    private List<BookFragment> bookFragments;
    private int currentItem = 0;
    private String zipPath;// 压缩包路径
    private String fileName;// (压缩包名字 TLXXX.zip)也是解压后的文件夹名字 TLXXX.zip
    private String examId; // 试卷id
    private String homeworkId; // 作业id
    private String testId; // 考试id
    private String testType;

    private String from;

    private int currentPos; // 当前是第几个答题包

    private int num; // 这套试卷总共有几道题

    private DialogPlus mMaterialDialog;


    private SurfaceView surfaceView;
    private ArrayList<String> segmentList = new ArrayList<>();//分段视频地址
    private ArrayList<String> aacList = new ArrayList<>();//分段音频地址
    private ArrayList<Long> timeList = new ArrayList<>();//分段录制时间
    //是否在录制视频
    private AtomicBoolean isRecordVideo = new AtomicBoolean(false);
    //拍照
    private CameraHelp mCameraHelp = new CameraHelp();
    private SurfaceHolder mSurfaceHolder;
    private MyVideoEditor mVideoEditor = new MyVideoEditor();
    private RecordUtil recordUtil;
    private String audioPath;

    private RecordUtil.OnPreviewFrameListener mOnPreviewFrameListener;
    private String videos;
    private long videoDuration;
    private long recordTime;
    private String videoPath;
    // 倒计时到随机数的时候开始录制视频
    private int COUNT = 0;
    private HomeKeyBroadCastReceiver homeKeyBroadCastReceiver;
    private ScreenOFFKeyBroadCastReceiver screenOFFKeyBroadCastReceiver;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from) {
        Intent intent = new Intent(context, TestBookActivity.class);
        intent.putExtra("from", from);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId) {
        Intent intent = new Intent(context, TestBookActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        return intent;
    }


    // ****************************************************************** //

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId, String homeworkId, String testType) {
        Intent intent = new Intent(context, TestBookActivity.class);
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
        // item.getId() = homeworkId
        homeworkId = intent.getStringExtra("homeworkId");
        testType = intent.getStringExtra("testType");
//        if (fileName.contains("SM")) {
//        questionTypeTv.setText("书面表达模拟大礼包");
        questionTypeTv.setText("2020年5月MHK三级校内测试（移动版）");
//        }

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

            initVideo();
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
        registerKeyReceiver();

        stateLoading();
        xbaseTopbar.setxBaseTopBarImp(this);

        pageModels = new ArrayList<PageModel>();
        bookFragments = new ArrayList<BookFragment>();

        viewPager.setNoScroll(true);
        loadDataAndRefreshView();

    }

    @Override
    public void onMainEvent(BaseEvents event) {
        switch (event.getValue()) {
            case EventsConfig.TEST_NEXT_PAGE:
                answer += event.getData();
                clearTimeProgress();
                viewPager.setCurrentItem(++currentItem);
                AppContext.getInstance().setCurrentItem(currentItem);

                // 练习和作业 结束的页面
                if (currentItem == bookFragments.size() - 1) {
                    String final_answer = "";
                    if (!StringUtils.equals(answer, "")) {
//                        final_answer = answer.substring(0, answer.length() - 1) + "finished";
                        final_answer = answer + "finished";
                    }
                    RxBus.getDefault().post(new CommitEvent(CommitEvent.COMMIT, zipPath, AppConstants.UNFILE_DOWNLOAD_PATH + fileName, final_answer, examId, homeworkId, testId, testType));
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
//            case EventsConfig.KILL_ACTIVITY:
//                if (event.getData().equals("end")) {
//                    submitState();
//                }
//                Bundle bundle = new Bundle();
//                String final_answer = answer.substring(0, answer.length() - 1) + "finished";
//                bundle.putString("answer", final_answer);
//                bundle.putString("name",getString(R.string.test_read));
//                openActivity(FinalScoreActivity_.class, bundle);
//                killActivity(this);
//                break;
            case EventsConfig.SUCCESS_BOOK:
                AppContext.getInstance().setAllItem(pageModels.size());
                if (from.equals("考试")) {
                    for (int i = 0; i < pageModels.size(); i++) {
                        BookFragment bookFragment = BookFragment.newInstance("考试");
                        bookFragment.setPageModel(pageModels.get(i));
                        bookFragments.add(bookFragment);
                    }
                } else {
                    for (int i = 0; i < pageModels.size(); i++) {
                        BookFragment bookFragment = BookFragment.newInstance();
                        bookFragment.setPageModel(pageModels.get(i));
                        bookFragments.add(bookFragment);
                    }
                }

                viewPager.setAdapter(new MyFragmentPagerAdapter(
                        getSupportFragmentManager(), bookFragments));

                stateMain();
                break;
//            case EventsConfig.TEST_NEXT_PART:
//                answer += event.getData();
//                String as = AppContext.getInstance().getAnswer();
//                String name = AppContext.getInstance().getName();
//                AppContext.getInstance().setAnswer( as + "&"+ answer.substring(0, answer.length() - 1));
//                AppContext.getInstance().setName(name + "&" + getString(R.string.test_read));
//
//                openActivity(TestBookActivity_.class);
//                killActivity(this);
//                break;
        }
    }

    private void clearTimeProgress() {
        countdownRl.setVisibility(View.GONE);
        RxCountDown.cancel();
    }

    private void startTimeProgress(int time) {
        RxCountDown.countdown(time, new RxCountDown.IRxExecute() {
            @Override
            public void doNext(long number) {
                countdownRl.setVisibility(View.VISIBLE);
                countdownTv.setText(number + "S");
            }

            @Override
            public void doComplete() {
                countdownTv.setText("");
                countdownRl.setVisibility(View.GONE);
            }
        });
    }

    //开始解压文件
    public void unZip(String zipFileName, String outputDirectory) {
        try {
            ZipFile zipFile = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                zipFile = new ZipFile(zipFileName, Charset.forName("GBK"));
            } else {
                zipFile = new ZipFile(zipFileName);
            }
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

    //正式加载数据
    private void loadData() {
        //读取本地数据
        try {

            Intent intent = getIntent();

//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                zipfilename = AppContext.getInstance().getZipfilename();
//                AppContext.getInstance().setCurrentItem(currentItem);
//            }else {
//                zipfilename = intent.getStringExtra("filename");
//            }

            String filename = AppConstants.UNFILE_DOWNLOAD_PATH + fileName + File.separator + "TestPaper.xml";

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputStreamReader(new FileInputStream(filename), "gb2312"));

            // 获取根元素
            Element root = document.getRootElement();

            // 获取此套试卷有多少道题
            try {
                num = Integer.parseInt(root.attributeValue("examnum"));
            } catch (NumberFormatException e) {
                ToastUtils.showShort("加载试卷失败");
                return;
            }

            //System.out.println("Root: " + root.getName());
            // 获取所有子元素
            List<Element> childList = root.elements();
            //System.out.println("total child count: " + childList.size());

            List<Element> parts = null;
            List<Element> listeners;
//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                parts = root.elements("part").subList(5,7);
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
                String sectionName;
                if (partName.equals(PageModel.jieshu)) {
                    sectionName = PageModel.jieshu;
                } else {
                    sectionName = section.attributeValue("name");
                }
                for (int j = 0; j < pages.size(); j++) {
                    Element page = pages.get(j);
                    //1.解析欢迎数据
                    switch (sectionName) {
                        case PageModel.huanying:
                        case PageModel.jieshu:
                            AnalyticXMLUtils.encodeWelcomeOrEndPageModel(pageModels, fileName, page, partName, sectionName);
                            break;
                        case PageModel.BOOK_shumianbiaoda:
                            encodepageModel(page, partName, sectionName, j);
                            break;

                    }
                }
            }


        } catch (UnsupportedEncodingException ex) {
            stateError();
        } catch (DocumentException e) {
            stateError();
        } catch (FileNotFoundException e) {
            stateError();
        }

        //getHandler().sendEmptyMessage(SUCEESS);

    }

    /**
     * 书面表达
     *
     * @param page
     * @param type
     */
    private void encodepageModel(Element page, String type, String section_name, int pagenumber) {
        //第二部分的解析和第一部分有区别
        //以每个page 为一页

        int current = 0;

        //循环遍历
        List<Element> elements = page.elements();

        //初始化
        PageModel pageModel = new PageModel();
        pageModel.setType(type);
        pageModel.setSection(section_name);

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
                    if (pagenumber == 0) {
                        basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), true));
                        basePageModel.setSuffix("png");
                    } else {
                        basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    }

                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.WAIT:
                    basePageModel.setTimeout(element.attributeValue("timeout"));
                    pageModel.addVO(basePageModel);
                    break;
                //2.如果有Question则为新一个page

                case BasePageModel.QUESTIONS:
                    List<Element> questions = element.elements();
                    for (int j = 0; j < questions.size(); j++) {
                        Element question = questions.get(j);
                        BasePageModel questionsModel = new BasePageModel();
                        questionsModel.setCurrent(current++);
                        questionsModel.setType(question.getName());

                        switch (question.getName()) {
                            case BasePageModel.TEXT:
                                questionsModel.setContent(question.getStringValue());
                                pageModel.addVO(questionsModel);
                                break;
                            case BasePageModel.RICHTEXT:
                                questionsModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, question.attributeValue("src"), false));
                                pageModel.addVO(questionsModel);
                                break;
                            case BasePageModel.QUESTION:
                                List<Element> questions_1 = question.elements();

                                String qustionid = question.attributeValue("id");

                                for (int j_1 = 0; j_1 < questions_1.size(); j_1++) {
                                    Element question_1 = questions_1.get(j_1);
                                    BasePageModel questionModel = new BasePageModel();
                                    questionModel.setCurrent(current++);
                                    questionModel.setType(question_1.getName());

                                    questionModel.setQuestionid(qustionid);

                                    switch (question_1.getName()) {
                                        case BasePageModel.TEXT:
                                            questionModel.setContent(question_1.getStringValue());
                                            pageModel.addVO(questionModel);
                                            break;
                                        case BasePageModel.RICHTEXT:
                                            questionModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, question_1.attributeValue("src"), false));
                                            pageModel.addVO(questionModel);
                                            break;
                                        case BasePageModel.AUDIO:
                                            questionModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, question_1.attributeValue("src"), false));
                                            pageModel.addVO(questionModel);
                                            break;
                                        case BasePageModel.WAIT:
                                            questionModel.setTimeout(question_1.attributeValue("timeout"));
                                            pageModel.addVO(questionModel);
                                            break;
                                        case BasePageModel.SELECT:

                                            List<Element> options = question_1.elements("option");

                                            List<ListenQOptionModel> listenQOptionModels = new ArrayList<ListenQOptionModel>();
                                            for (int k = 0; k < options.size(); k++) {
                                                ListenQOptionModel listenQOptionModel = new ListenQOptionModel();

                                                Element option = options.get(k);
                                                listenQOptionModel.setOptionvalue(option.attributeValue("value"));
                                                listenQOptionModel.setOptiontext(option.getStringValue());
                                                listenQOptionModels.add(listenQOptionModel);
                                            }
                                            questionModel.setListenQOptionModels(listenQOptionModels);
                                            pageModel.addVO(questionModel);
                                            pageModel.addListenBasePageModel(questionModel);
                                            break;
                                    }

                                }


                                break;
                        }
                    }

                    break;
                case BasePageModel.QUESTION:
                    //question 中会有其他参数
                    break;
                case BasePageModel.AUDIO:
                    basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    pageModel.addVO(basePageModel);
                    break;
            }
        }

        pageModels.add(pageModel);
    }

    @Override
    public void responeError(String errorMsg) {

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
    public void left() {
        setBackHint();
    }

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
                            // 如果答案为空，则说明好在 欢迎界面 还没答题就点了退出键，answer就全部为拼为空
                            if (StringUtils.isEmpty(answer)) {
                                for (int i = 1; i <= num; i++) {
                                    if (i == num) {
                                        answer += (i < 10 ? ("0" + i) : i) + "=|" + "finished";
                                    } else {
                                        answer += (i < 10 ? ("0" + i) : i) + "=|";
                                    }
                                }
                            } else {
                                String[] strs = answer.split("\\|");
                                for (int i = strs.length + 1; i <= num; i++) {
                                    if (i == num) {
                                        answer += (i < 10 ? ("0" + i) : i) + "=|" + "finished";
                                    } else {
                                        answer += (i < 10 ? ("0" + i) : i) + "=|";
                                    }
                                }
                            }

                            RxBus.getDefault().post(new ExitCommitEvent(ExitCommitEvent.EXIT_COMMIT, zipPath, AppConstants.UNFILE_DOWNLOAD_PATH + fileName, answer, examId, homeworkId, testId, testType));
                            dialog.dismiss();
                            break;
                    }
                }).create();
        TextView textView = (TextView) mMaterialDialog.findViewById(R.id.txt_msg);
        textView.setText("是否要提前交卷？");
        mMaterialDialog.show();
    }

    @Override
    public void right() {
    }

    private void initVideo() {

        LanSoEditor.initSDK(this, null);
        videos = AppConstants.VIDEO_RECORDING_PATH + System.currentTimeMillis() + "/";
        LanSongFileUtil.setFileDir(videos);
        LibyuvUtil.loadLibrary();

        initUI();
        initMediaRecorder();

        // 录视频
        initVideoRecord();
    }

    private void initUI() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                int width = surfaceView.getWidth();
                int height = surfaceView.getHeight();
                float viewRatio = width * 1f / height;
                float videoRatio = 9f / 16f;
                ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
                if (viewRatio > videoRatio) {
                    layoutParams.width = width;
                    layoutParams.height = (int) (width / viewRatio);
                } else {
                    layoutParams.width = (int) (height * viewRatio);
                    layoutParams.height = height;
                }
                surfaceView.setLayoutParams(layoutParams);
            }
        });
    }

    private void initMediaRecorder() {
        mCameraHelp.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (isRecordVideo.get() && mOnPreviewFrameListener != null) {
                    mOnPreviewFrameListener.onPreviewFrame(data);
                }
            }
        });

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceHolder = holder;
                // TODO 1 ,改为默认前置
//                mCameraHelp.openCamera(mContext, Camera.CameraInfo.CAMERA_FACING_BACK, mSurfaceHolder);
                mCameraHelp.openCamera(mContext, Camera.CameraInfo.CAMERA_FACING_FRONT, mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraHelp.release();
            }
        });

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraHelp.callFocusMode();
            }
        });

    }

    public void finishVideo() {
        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<String>() {
            @Override
            public String doInBackground() throws Exception {
                //合并h264
                String h264Path = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".h264";
                Utils.mergeFile(segmentList.toArray(new String[]{}), h264Path);
                //h264转mp4
                String mp4Path = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".mp4";
                mVideoEditor.h264ToMp4(h264Path, mp4Path);
                //合成音频
//                String aacPath = mVideoEditor.executePcmEncodeAac(syntPcm(), RecordUtil.sampleRateInHz, RecordUtil.channelCount);
                //音视频混合
//                mp4Path = mVideoEditor.executeVideoMergeAudio(mp4Path, aacPath);
                mp4Path = mVideoEditor.executeVideoMergeAudio(mp4Path, "");
                return mp4Path;
            }

            @Override
            public void onFinish(String result) {
                // TODO 完成直接返回不裁剪,result 就是图片存储路径
                List<File> s = FileUtils.listFilesInDir(videos);
                for (int i = 0; i < s.size(); i++) {
                    if (StringUtils.equals(s.get(i).getPath(), result)) {
                        mPresenter.uploadVideo(s.get(i), examId, testId, testType);
                        break;
//                        FileUtils.delete(s.get(i));
//                    }else {
//                        // 录制完就上传视频
//                        mPresenter.uploadVideo(s.get(i), examId, testId, testType);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtils.i("视频编辑失败");
            }
        });
    }

    private String syntPcm() throws Exception {
        String pcmPath = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".pcm";
        File file = new File(pcmPath);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        for (int x = 0; x < aacList.size(); x++) {
            FileInputStream in = new FileInputStream(aacList.get(x));
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                out.flush();
            }
            in.close();
        }
        out.close();
        return pcmPath;
    }

    private void startRecord() {

        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<Boolean>() {
            @Override
            public Boolean doInBackground() throws Throwable {
                videoPath = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".h264";
                audioPath = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".pcm";
                final boolean isFrontCamera = mCameraHelp.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT;
                final int rotation;
                if (isFrontCamera) {
                    rotation = 270;
                } else {
                    rotation = 90;
                }
                recordUtil = new RecordUtil(videoPath, audioPath, mCameraHelp.getWidth(), mCameraHelp.getHeight(), rotation, isFrontCamera);
                return true;
            }

            @Override
            public void onFinish(Boolean result) {
                mOnPreviewFrameListener = recordUtil.start();
                videoDuration = 0;
                recordTime = System.currentTimeMillis();
                runLoopPro();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void runLoopPro() {

        RxJavaUtil.loop(20, new RxJavaUtil.OnRxLoopListener() {
            @Override
            public Boolean takeWhile() {
                return recordUtil != null && recordUtil.isRecording();
            }

            @Override
            public void onExecute() {
                long currentTime = System.currentTimeMillis();
                videoDuration += currentTime - recordTime;
                recordTime = currentTime;
                long countTime = videoDuration;
                for (long time : timeList) {
                    countTime += time;
                }
            }

            @Override
            public void onFinish() {
                segmentList.add(videoPath);
                aacList.add(audioPath);
                timeList.add(videoDuration);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void upEvent() {
        if (recordUtil != null) {
            recordUtil.stop();
            recordUtil = null;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishVideo();
                }
            }, 1000);
        }
    }

    /**
     * 清除录制信息
     */
    private void cleanRecord() {

        segmentList.clear();
        aacList.clear();
        timeList.clear();

    }

    @Override
    public void onPause() {
        RxTimerUtil.cancel();
        RxCountDown.cancel();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearVideo();
        if (homeKeyBroadCastReceiver != null) {
            // 解除广播
            unregisterReceiver(homeKeyBroadCastReceiver);
        }
        if (screenOFFKeyBroadCastReceiver != null) {
            // 解除广播
            unregisterReceiver(screenOFFKeyBroadCastReceiver);
        }
    }

    private void clearVideo() {
        cleanRecord();
        if (mCameraHelp != null) {
            mCameraHelp.release();
        }
        if (recordUtil != null) {
            recordUtil.stop();
        }
    }

    // TODO 3 添加完成自动结束录制视频（相当于 Rxbus 通知，免去手动点击）

    @Override
    public void onBackPressedSupport() {
        setBackHint();
    }
    // ****************************************************************** //

    /**
     * 到随机的时间点 录制视频
     */
    private void initVideoRecord() {
        // *************** 随机数录视频 ***************//
        // 假如在 0-100，分为三个平均时间段,每段随机一个数，从该数开始录5秒视频
        long total = 600;

        long oneMin = 5;
        long oneMax = (long) (Math.floor(total / 3) - 10);

        long twoMin = (long) Math.floor(total / 3);
        long twoMax = (long) (Math.floor(total * 2 / 3) - 10);

        long threeMin = (long) Math.floor(total * 2 / 3);
        long threeMax = total - 10;

        long videoRecordOne = oneMin + (int) (Math.random() * ((oneMax - oneMin) + 1));
        long videoRecordTwo = twoMin + (int) (Math.random() * ((twoMax - twoMin) + 1));
        long videoRecordThree = threeMin + (int) (Math.random() * ((threeMax - threeMin) + 1));

        RxTimerUtil.interval(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                COUNT++;
                if (COUNT == videoRecordOne) {
                    // 到随机的数了开始录像
                    isRecordVideo.set(true);
                    startRecord();
                } else if (COUNT == videoRecordOne + 6) {
                    if (isRecordVideo.get()) {
                        isRecordVideo.set(false);
                        upEvent();
                    }
                }

                if (COUNT == videoRecordTwo) {
                    // 到随机的数了开始录像
                    isRecordVideo.set(true);
                    startRecord();
                } else if (COUNT == videoRecordTwo + 5) {
                    if (isRecordVideo.get()) {
                        isRecordVideo.set(false);
                        upEvent();
                    }
                }

                if (COUNT == videoRecordThree) {
                    // 到随机的数了开始录像
                    isRecordVideo.set(true);
                    startRecord();
                } else if (COUNT == videoRecordThree + 5) {
                    if (isRecordVideo.get()) {
                        isRecordVideo.set(false);
                        upEvent();
                    }
                }
            }
        });
        // *******************************************//
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
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.SUCCESS_BOOK));
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }
    }

    private void registerKeyReceiver() {
        homeKeyBroadCastReceiver = new HomeKeyBroadCastReceiver();
        registerReceiver(homeKeyBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        screenOFFKeyBroadCastReceiver = new ScreenOFFKeyBroadCastReceiver();
        registerReceiver(screenOFFKeyBroadCastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    class HomeKeyBroadCastReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        @Override
        public void onReceive(Context context, Intent intent) {

            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    //点Home 键退出，添加续考功能
                    RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ReExamEvent.BOOK));
                    finish();
                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ""));
                    finish();
                }
            }
        }
    }

    class ScreenOFFKeyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //点锁屏按钮，直接回列表页，也不重新考了，手动点击再继续考试
            RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ""));
            finish();
        }
    }

}