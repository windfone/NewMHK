package com.hlxyedu.mhk.ui.eread.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.base.RootFragmentActivity;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.event.BaseEvents;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.event.EventsConfig;
import com.hlxyedu.mhk.model.models.AnalyticXMLUtils;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.ListenQOptionModel;
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.contract.TestReadContract;
import com.hlxyedu.mhk.ui.eread.fragment.ReadFragment;
import com.hlxyedu.mhk.ui.eread.presenter.TestReadPresenter;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.hlxyedu.mhk.ui.exam.activity.ExamFinishActivity;
import com.hlxyedu.mhk.utils.MyFragmentPagerAdapter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.hlxyedu.mhk.weight.viewpager.NoTouchViewPager;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import butterknife.BindView;

/**
 * Created by zhangguihua
 * 阅读理解（和书面表达 是一样的）
 */
public class TestReadActivity extends RootFragmentActivity<TestReadPresenter> implements TestReadContract.View, XBaseTopBarImp {

    private static final String TAG = TestReadActivity.class.getSimpleName();
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
    private List<ReadFragment> readFragments;
    private int currentItem = 0;
    private String zipPath;// 压缩包路径
    private String fileName;// (压缩包名字 TLXXX.zip)也是解压后的文件夹名字 TLXXX.zip
    private String examId; // 试卷id
    private String homeworkId; // 作业id
    private String testId; // 考试id
    private String testType;

    // 倒计时
    private RxTimerUtil rxTimer;
    private int TIMER;

    private String from;

    private int currentPos; // 当前是第几个答题包

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from) {
        Intent intent = new Intent(context, TestReadActivity.class);
        intent.putExtra("from", from);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId) {
        Intent intent = new Intent(context, TestReadActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId, String homeworkId, String testType) {
        Intent intent = new Intent(context, TestReadActivity.class);
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
        questionTypeTv.setText("阅读理解模拟大礼包");

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
        stateLoading();
        xbaseTopbar.setxBaseTopBarImp(this);

        rxTimer = new RxTimerUtil();

        pageModels = new ArrayList<PageModel>();
        readFragments = new ArrayList<ReadFragment>();

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

                if (currentItem == readFragments.size() - 1) {
                    String final_answer = "";
                    if (!StringUtils.equals(answer, "")) {
//                        final_answer = answer.substring(0, answer.length() - 1) + "finished";
                        final_answer = answer + "finished";
                    }
                    RxBus.getDefault().post(new CommitEvent(CommitEvent.COMMIT, final_answer, examId, homeworkId, testId, testType));
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
            case EventsConfig.SUCCESS_READ:
                AppContext.getInstance().setAllItem(pageModels.size());
                if (from.equals("考试")) {
                    for (int i = 0; i < pageModels.size(); i++) {
                        ReadFragment readFragment = ReadFragment.newInstance("考试");
                        readFragment.setPageModel(pageModels.get(i));
                        readFragments.add(readFragment);
                    }
                } else {
                    for (int i = 0; i < pageModels.size(); i++) {
                        ReadFragment readFragment = ReadFragment.newInstance();
                        readFragment.setPageModel(pageModels.get(i));
                        readFragments.add(readFragment);
                    }
                }
                viewPager.setAdapter(new MyFragmentPagerAdapter(
                        getSupportFragmentManager(), readFragments));

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

    //开始解压文件
    public void unZip(String zipFileName, String outputDirectory) {
        try {
            ZipFile zipFile = new ZipFile(zipFileName);
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
                    switch (sectionName) {
                        case PageModel.huanying:
                        case PageModel.jieshu:
                            AnalyticXMLUtils.encodeWelcomeOrEndPageModel(pageModels, fileName, page, partName, sectionName);
                            break;
                        case PageModel.READ_yuedulijie:
                            encodeReadPageModel(page, partName, sectionName, j);
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
     * 阅读题
     *
     * @param page
     * @param type
     */
    private void encodeReadPageModel(Element page, String type, String section_name, int pagenumber) {
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
    protected void onDestroy() {
        rxTimer.cancel();
        super.onDestroy();
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
    public void onBackPressedSupport() {
        mMaterialDialog.show();
    }

    @Override
    public void left() {
        mMaterialDialog.show();
    }

    @Override
    public void right() {
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
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.SUCCESS_READ));
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }
    }

}