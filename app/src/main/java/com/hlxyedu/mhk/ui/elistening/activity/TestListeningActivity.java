package com.hlxyedu.mhk.ui.elistening.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.fifedu.record.media.record.AudioPlayManager;
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
import com.hlxyedu.mhk.ui.exercise.contract.TestListeningContract;
import com.hlxyedu.mhk.ui.elistening.fragment.ListeningFragment;
import com.hlxyedu.mhk.ui.exercise.presenter.TestListeningPresenter;
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
import butterknife.OnClick;

/**
 * Created by zhangguihua
 * 听力练习
 */
public class TestListeningActivity extends RootFragmentActivity<TestListeningPresenter> implements TestListeningContract.View, XBaseTopBarImp {

    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;
    @BindView(R.id.question_type_tv)
    TextView questionTypeTv;
    @BindView(R.id.notouch_vp)
    NoTouchViewPager notouchVp;
    @BindView(R.id.countdown_tv)
    TextView countdownTv;
    @BindView(R.id.countdown_rl)
    RelativeLayout countdownRl;

    //解析到的数据中心
    private List<PageModel> pageModels;
    //fragment 数组
    private List<ListeningFragment> testListenFragments;

    private int currentItem = 0;

    private String zipPath;// 压缩包路径
    private String fileName;// (压缩包名字 TLXXX.zip)也是解压后的文件夹名字 TLXXX.zip
    private String examId; // 试卷id
    private String homeworkId; // 作业id

    // 倒计时
    private int TIMER;

    private String from;

    public String answer = "";


    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from,String zipPath, String fileName,String examId) {
        Intent intent = new Intent(context, TestListeningActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        return intent;
    }

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from,String zipPath, String fileName,String examId,String homeworkId) {
        Intent intent = new Intent(context, TestListeningActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        intent.putExtra("homeworkId", homeworkId);
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
        if (fileName.contains("TL")){
            questionTypeTv.setText("听力模拟大礼包");
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
        pageModels = new ArrayList<PageModel>();
        testListenFragments = new ArrayList<ListeningFragment>();

        notouchVp.setNoScroll(true);
        loadDataAndRefreshView();
    }

    private void clearTimeProgress() {
        countdownRl.setVisibility(View.GONE);
        RxTimerUtil.cancel();
    }

    private void startTimeProgress(int time) {
        TIMER = time;
        RxTimerUtil.interval(1000, number -> {
            TIMER--;
            if (TIMER == 0 ){
                countdownTv.setText("");
                countdownRl.setVisibility(View.GONE);
                RxTimerUtil.cancel();
                // 下一题
            }else {
                countdownRl.setVisibility(View.VISIBLE);
                countdownTv.setText(TIMER + "S");
            }
        });
    }

    @Override
    public void onMainEvent(BaseEvents event) {
        switch (event.getValue()) {
            //切换下一页
            case EventsConfig.TEST_NEXT_PAGE:
                clearTimeProgress();
                answer += event.getData();
                notouchVp.setCurrentItem(++currentItem);
                AppContext.getInstance().setCurrentItem(currentItem);

                // 结束的页面
                if (currentItem == testListenFragments.size() -1){
                    String final_answer = answer.substring(0, answer.length() - 1) + "finished";
                    RxBus.getDefault().post(new CommitEvent(CommitEvent.COMMIT,final_answer,examId,homeworkId));
                }
                break;
            //显示倒计时
            case EventsConfig.SHOW_DETAL_VIEW:
                clearTimeProgress();
                int time = (int) event.getData();
                // 暂停时间太短 1秒不显示倒计时时间
                if (time > 1){
//                    countdownRl.setVisibility(View.VISIBLE);
                    startTimeProgress(time);
                }
                break;
//            case EventsConfig.SHOW_AUDIO_VIEW:
//                clearTimeProgress();
//                countdownRl.setVisibility(View.VISIBLE);
//                int time1 = (int) event.getData();
//                startTimeProgress(time1);
//                break;
            case EventsConfig.KILL_ACTIVITY:
//                if (event.getData().equals("end")) {
//                    submitState();
//                }
//                Bundle bundle = new Bundle();
//                String final_answer = answer.substring(0, answer.length() - 1) + "finished";
//                bundle.putString("answer", final_answer);
//                bundle.putString("name", getString(R.string.test_hearing));
//                openActivity(FinalScoreActivity_.class, bundle);
//                killActivity(this);
                break;
             //听力题解析成功
            case EventsConfig.SUCCESS_LISTEN:
                AppContext.getInstance().setAllItem(pageModels.size());
                for (int i = 0; i < pageModels.size(); i++) {
                    ListeningFragment testListenFragment =ListeningFragment.newInstance();
                    testListenFragment.setPageModel(pageModels.get(i));
                    testListenFragments.add(testListenFragment);
                }
                notouchVp.setAdapter(new MyFragmentPagerAdapter(
                        getSupportFragmentManager(), testListenFragments));
                stateMain();
                break;
//            case EventsConfig.TEST_NEXT_PART:
//                answer += event.getData();
//                AppContext.getInstance().setAnswer(answer.substring(0, answer.length() - 1));
//                AppContext.getInstance().setName(getString(R.string.test_hearing));
//
//                openActivity(TestSpeakActivity_.class);
//                killActivity(this);
//                break;
        }
    }

    @Override
    public void responeError(String errorMsg) {

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

    private class DecodeAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.SUCCESS_LISTEN));
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }
    }

    //正式加载数据
    private void loadData() {

        try {

//            Intent intent = getIntent();
//
//            zipfilename = intent.getStringExtra("filename");
//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                AppContext.getInstance().setZipfilename(zipfilename);
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
//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                parts = root.elements("part").subList(0,2);
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
                String sectionName;
                if (partName.equals(PageModel.jieshu)) {
                    sectionName = PageModel.jieshu;
                } else {
                    sectionName = section.attributeValue("name");
                }

                List<Element> pages = section.elements("page");

                for (int j = 0; j < pages.size(); j++) {
                    Element page = pages.get(j);

                    //一起解析比较难分辨，所以拆分解析
                    //1.解析欢迎数据
                    switch (sectionName) {
                        case PageModel.huanying:
                        case PageModel.jieshu:
                            AnalyticXMLUtils.encodeWelcomeOrEndPageModel(pageModels, fileName, page, partName, sectionName);
                            break;
                        case PageModel.LISTEN_section_1:
                            encodefirstPageModel(page, partName, sectionName, j);
                            break;
                        case PageModel.LISTEN_section_2:
                            encodeSecondPageModel(page, partName, sectionName, j);
                            break;

                    }
                }

            }


        } catch (DocumentException e) {
            stateError();

        } catch (UnsupportedEncodingException ex) {
            stateError();
        } catch (FileNotFoundException e) {
            stateError();
        }

    }

    /**
     * 解析第一部分
     *
     * @param page
     * @param type
     * @param section_name
     */
    private void encodefirstPageModel(Element page, String type, String section_name, int pagenumber) {
        int current = 0;
        boolean isquestion = false;

        //循环遍历
        List<Element> elements = page.elements();

        //初始化
        PageModel pageModel = new PageModel();
        pageModel.setType(type);
        pageModel.setSection(section_name);

        //此部分为两部分组成，有question，和开始部分
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
                case BasePageModel.QUESTION:
                    //question 中会有其他参数
                    isquestion = true;
                    encodefirstQuestionPageModel(element, type, section_name);
                    break;
                case BasePageModel.AUDIO:
                    //暂时不处理 滴 声
                    basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    pageModel.addVO(basePageModel);
                    break;
            }
        }

        //如果不是question则直接添加，否则已经加入
        if (!isquestion) {
            pageModels.add(pageModel);
        }
    }

    /**
     * 解析第一部分中包含question部分
     *
     * @param question
     * @param type
     * @param section_name
     */
    private void encodefirstQuestionPageModel(Element question, String type, String section_name) {
        int current = 0;

        //循环遍历
        List<Element> elements = question.elements();

        //初始化
        PageModel pageModel = new PageModel();
        pageModel.setType(type);
        pageModel.setSection(section_name);

        //此部分为两部分组成，有question，和开始部分
        //1.先解析开始部分，和欢迎部分是一样的
        for (int i = 0; i < elements.size(); i++) {

            Element element = elements.get(i);
            BasePageModel basePageModel = new BasePageModel();
            basePageModel.setCurrent(current++);
            basePageModel.setType(element.getName());

            //应该放在父类中 以便获取
            basePageModel.setQuestionid(question.attributeValue("id"));

            switch (element.getName()) {
                case BasePageModel.TEXT:
                    basePageModel.setContent(element.getStringValue());
                    pageModel.addVO(basePageModel);
                    break;

                case BasePageModel.AUDIO:
                    basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.WAIT:
                    basePageModel.setTimeout(element.attributeValue("timeout"));
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.SELECT:

                    List<Element> options = element.elements("option");

                    List<ListenQOptionModel> listenQOptionModels = new ArrayList<ListenQOptionModel>();
                    for (int k = 0; k < options.size(); k++) {
                        ListenQOptionModel listenQOptionModel = new ListenQOptionModel();

                        Element option = options.get(k);
                        listenQOptionModel.setOptionvalue(option.attributeValue("value"));
                        listenQOptionModel.setOptiontext(option.getStringValue());
                        listenQOptionModels.add(listenQOptionModel);
                    }
                    basePageModel.setListenQOptionModels(listenQOptionModels);
                    pageModel.addVO(basePageModel);
                    pageModel.addListenBasePageModel(basePageModel);
                    break;
            }

        }

        //单独添加di
        BasePageModel dibasePageModel = new BasePageModel();
        dibasePageModel.setCurrent(current++);
        dibasePageModel.setType(BasePageModel.AUDIO);
        dibasePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, "Di.mp3", false));
        pageModel.addVO(dibasePageModel);

        pageModels.add(pageModel);

    }

    /**
     * 解析第二部分
     *
     * @param page
     * @param type
     * @param section_name
     */
    private void encodeSecondPageModel(Element page, String type, String section_name, int pagenumber) {
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
                case BasePageModel.QUESTION:
                    //question 中会有其他参数
                    List<Element> questions = element.elements();

                    String qustionid = element.attributeValue("id");

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
                                questionModel.setSrc(question.attributeValue("src"));
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
                            case BasePageModel.SELECT:

                                List<Element> options = question.elements("option");

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
                case BasePageModel.AUDIO:
                    basePageModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, element.attributeValue("src"), false));
                    pageModel.addVO(basePageModel);
                    break;
            }
        }

        pageModels.add(pageModel);

    }

    @Override
    protected void onStop() {
        AudioPlayManager.getManager().stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RxTimerUtil.cancel();
        super.onDestroy();
    }

    @Override
    public void left() {
        finish();
    }

    @Override
    public void right() {

    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test_listening;
    }

    @OnClick(R.id.next_btn)
    public void onViewClicked() {
        finish();
    }
}