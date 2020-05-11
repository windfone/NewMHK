package com.hlxyedu.mhk.ui.ecomposition.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileUtils;
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
import com.hlxyedu.mhk.model.models.PageModel;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.contract.TestTxtContract;
import com.hlxyedu.mhk.ui.ecomposition.fragment.TxtFragment;
import com.hlxyedu.mhk.ui.ecomposition.presenter.TestTxtPresenter;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.hlxyedu.mhk.ui.exam.activity.ExamFinishActivity;
import com.hlxyedu.mhk.utils.MyFragmentPagerAdapter;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBarImp;
import com.hlxyedu.mhk.weight.viewpager.NoTouchViewPager;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.orhanobut.logger.Logger;
import com.skyworth.rxqwelibrary.app.AppConstants;
import com.skyworth.rxqwelibrary.utils.RxCountDown;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import butterknife.BindView;

/**
 * Created by zhangguihua
 * 作文 练习
 */
public class TestTxtActivity extends RootFragmentActivity<TestTxtPresenter> implements TestTxtContract.View, XBaseTopBarImp {

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
    private List<TxtFragment> txtFragments;
    private int currentItem = 0;
    private String zipPath;// 压缩包路径
    private String fileName;// (压缩包名字 TLXXX.zip)也是解压后的文件夹名字 TLXXX.zip
    private String examId; // 试卷id
    private String homeworkId; // 作业id
    private String testId; // 考试id
    private String testType;

    private String from;

    private int currentPos; // 当前是第几个答题包

    private String final_answer = "";// 最终提交的答案 字符串

    //--------------------------------------------------------------------------------------------------//
    private HomeKeyBroadCastReceiver homeKeyBroadCastReceiver;
    private ScreenOFFKeyBroadCastReceiver screenOFFKeyBroadCastReceiver;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, String from) {
        Intent intent = new Intent(context, TestTxtActivity.class);
        intent.putExtra("from", from);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId) {
        Intent intent = new Intent(context, TestTxtActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        return intent;
    }

    public static Intent newInstance(Context context, String from, String zipPath, String fileName, String examId, String homeworkId, String testType) {
        Intent intent = new Intent(context, TestTxtActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("zipPath", zipPath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("examId", examId);
        intent.putExtra("homeworkId", homeworkId);
        intent.putExtra("testType", testType);
        return intent;
    }

    // ****************************************************************** //

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        Logger.d("进入作文页面");
        // 保持屏幕唤醒状态
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        registerKeyReceiver();

        stateLoading();
        xbaseTopbar.setxBaseTopBarImp(this);

        pageModels = new ArrayList<PageModel>();
        txtFragments = new ArrayList<TxtFragment>();

        viewPager.setNoScroll(true);
        loadDataAndRefreshView();

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
//        if (fileName.contains("ZW")) {
//        questionTypeTv.setText("作文模拟大礼包");
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

        }
        Logger.d("解压作文试卷");
        mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文开始解压");
        UnZipAsyncTask unZipAsyncTask = new UnZipAsyncTask();
        unZipAsyncTask.execute();
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

    @Override
    public void onMainEvent(BaseEvents event) {
        switch (event.getValue()) {
            case EventsConfig.TEST_NEXT_PAGE:
                clearTimeProgress();
                viewPager.setCurrentItem(++currentItem);
                // TODO 新增
                AppContext.getInstance().setCurrentItem(currentItem);
                Logger.d("作文准备切换下一页，总共："+txtFragments.size()+"页;"+"当前是第："+currentItem+1+"页");

                // 结束的页面
                if (currentItem == txtFragments.size() - 1) {
                    Logger.d("作文准备提交答案");
                    mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文-准备提交答案");
                    final_answer = (String) event.getData();
                    RxBus.getDefault().post(new CommitEvent(CommitEvent.COMMIT, zipPath, AppConstants.UNFILE_DOWNLOAD_PATH + fileName, final_answer, examId, homeworkId, testId, testType));
                }

                break;
            case EventsConfig.SHOW_DETAL_VIEW:
                clearTimeProgress();
                int time = (int) event.getData();
                if (time > 1) {
                    startTimeProgress(time);
                }
                break;
            case EventsConfig.TEST_NEXT_ACTIVITY:
                // 考试 模块是多个答题压缩包，答完一个接下一个
                if (currentPos == AppContext.getInstance().getExamProgressVOS().size() - 1) {
                    //TODO 如果是最后一个，则跳转到一个专门的 考试模块的结束页面
                    Logger.d("跳转到整体考试结束页面");
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
                        Logger.d("跳转到作文页面");
                        mContext.startActivity(TestReadActivity.newInstance(mContext, "考试"));
                    } else if (names.contains("SM")) {
                        Logger.d("跳转到书面页面");
                        mContext.startActivity(TestBookActivity.newInstance(mContext, "考试"));
                    } else if (names.contains("ZW")) {
                        Logger.d("跳转到作文页面");
                        mContext.startActivity(TestTxtActivity.newInstance(mContext, "考试"));
                    }
                }
                finish();
                break;
            case EventsConfig.KILL_ACTIVITY:
//                if(event.getData().equals("end")){
//                    submitState();
//                }

                break;
            //获取数据 并且 解析成功
            case EventsConfig.SUCCESS_WRITE:
                // TODO 新增
                Logger.d("作文解析成功");
                AppContext.getInstance().setAllItem(pageModels.size());
                if (from.equals("考试")) {
                    for (int i = 0; i < pageModels.size(); i++) {
                        TxtFragment txtFragment = TxtFragment.newInstance("考试");
                        txtFragment.setPageModel(pageModels.get(i));
                        txtFragments.add(txtFragment);
                    }
                } else {
                    for (int i = 0; i < pageModels.size(); i++) {
                        TxtFragment txtFragment = TxtFragment.newInstance();
                        txtFragment.setPageModel(pageModels.get(i));
                        txtFragments.add(txtFragment);
                    }
                }

                viewPager.setAdapter(new MyFragmentPagerAdapter(
                        getSupportFragmentManager(), txtFragments));
                stateMain();
                break;
        }
    }

    //开始解压文件
    public void unZip(String zipFileName, String outputDirectory) {
        try {
            ZipFile zipFile = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Logger.d("Android N以上解压方法");
                zipFile = new ZipFile(zipFileName, Charset.forName("GBK"));
            } else {
                Logger.d("Android N以下解压方法");
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
            Logger.d("解压作文试卷异常" + e.toString());
            mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文解压异常");
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
            Logger.d("创建作文文件夹出现异常");
            Toast.makeText(this, "创建文件夹出现异常", Toast.LENGTH_LONG).show();
            return;
        }
    }

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

            String filename = AppConstants.UNFILE_DOWNLOAD_PATH + fileName + File.separator + "TestPaper.xml";

            SAXReader saxReader = new SAXReader();
            //URL url = new URL("http://192.168.31.114:8080/mhk/TestPaper.xml");
            //Document document = saxReader.read(stringReader);

            Document document = saxReader.read(new InputStreamReader(new FileInputStream(filename), "gb2312"));

            // 获取根元素
            Element root = document.getRootElement();
            //System.out.println("Root: " + root.getName());

            // 获取所有子元素
            List<Element> childList = root.elements();
            //System.out.println("total child count: " + childList.size());

            List<Element> parts = null;
//            if (AppContext.getInstance().getProperty("examType").equals("ZH")){
//                parts = root.elements("part").subList(9,12);
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
                    switch (partName) {
                        case PageModel.huanying:
                        case PageModel.jieshu:
                            Logger.d("解析欢迎、结束部分");
                            AnalyticXMLUtils.encodeWelcomeOrEndPageModel(pageModels, fileName, page, partName, sectionName);
                            break;
                        case PageModel.WRITE_zuowen:
                            Logger.d("解析作文部分");
                            encodeWritePageModel(page, partName);
                            break;
                    }
                }

            }


        } catch (DocumentException e) {
            Logger.d("DocumentException异常" + e.toString());
            stateError();

        } catch (UnsupportedEncodingException ex) {
            Logger.d("UnsupportedEncodingException异常" + ex.toString());
            stateError();
        } catch (FileNotFoundException e) {
            Logger.d("FileNotFoundException异常" + e.toString());
            stateError();
        }
    }

    /**
     * 写作题
     *
     * @param page
     * @param type
     */
    private void encodeWritePageModel(Element page, String type) {

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

                            case BasePageModel.RICHTEXT:
                                questionModel.setSrc(AnalyticXMLUtils.getFileUrl(fileName, question.attributeValue("src"), true));
                                questionModel.setSuffix("png");
                                pageModel.addVO(questionModel);
                                break;
                            case BasePageModel.WAIT:
                                questionModel.setTimeout(question.attributeValue("timeout"));
                                pageModel.addVO(questionModel);
                                break;
                        }
                    }
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
        return R.layout.activity_test_txt;
    }

    @Override
    public void left() {
        setBackHint();
    }

    //    private DialogPlus mMaterialDialog;
    private void setBackHint() {
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DialogPlus mMaterialDialog = DialogPlus.newDialog(this)
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
                            Logger.d("弹出是否提前交卷弹窗并点击取消按钮");
                            dialog.dismiss();
                            break;
                        case R.id.btn_pos:
                            Logger.d("弹出是否提前交卷弹窗并点击提交按钮");

                            mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文解压异常");

                            RxBus.getDefault().post(new ExitCommitEvent(ExitCommitEvent.EXIT_COMMIT, zipPath, AppConstants.UNFILE_DOWNLOAD_PATH + fileName, examId, homeworkId, testId, testType));
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d("onRestart");
        RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ReExamEvent.COMPOSITION));
        finish();
    }

    @Override
    public void onPause() {
        Logger.d("onPause");
        mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文页面退出、切屏或锁屏");
        RxTimerUtil.cancel();
        RxCountDown.cancel();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        if (homeKeyBroadCastReceiver != null) {
            // 解除广播
            unregisterReceiver(homeKeyBroadCastReceiver);
        }
        if (screenOFFKeyBroadCastReceiver != null) {
            // 解除广播
            unregisterReceiver(screenOFFKeyBroadCastReceiver);
        }
    }

    @Override
    public void onBackPressedSupport() {
//        setBackHint();
    }
    // ****************************************************************** //

    private void registerKeyReceiver() {
        homeKeyBroadCastReceiver = new HomeKeyBroadCastReceiver();
        registerReceiver(homeKeyBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        screenOFFKeyBroadCastReceiver = new ScreenOFFKeyBroadCastReceiver();
        registerReceiver(screenOFFKeyBroadCastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    private class UnZipAsyncTask extends AsyncTask<Void, Integer, Void> {

        public UnZipAsyncTask() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // 解压完成
            //加载数据
            Logger.d("解压作文试卷完成");
            mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文解压成功");
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
            RxBus.getDefault().post(new BaseEvents(BaseEvents.NOTICE, EventsConfig.SUCCESS_WRITE));
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }
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
                    Logger.d("在作文页面接收到Home键广播");
                    mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文页面点击Home键");
                    RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ReExamEvent.COMPOSITION));
                    finish();
                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    Logger.d("在作文页面接收到菜单键/多任务键广播");
                    mPresenter.saveLog(mPresenter.getUserId(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), "作文页面点击菜单键");
                    RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ""));
                    finish();
                }
            }
        }
    }

    class ScreenOFFKeyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("在作文页面接收到锁屏广播");
            //点锁屏按钮，直接回列表页，也不重新考了，手动点击再继续考试
            RxBus.getDefault().post(new ReExamEvent(ReExamEvent.RE_EXAM, ""));
            finish();
        }
    }
}