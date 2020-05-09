package com.hlxyedu.mhk.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;


import com.fifedu.record.recinbox.bl.record.RecorderManager;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.api.Constants;
import com.hlxyedu.mhk.di.component.AppComponent;
import com.hlxyedu.mhk.di.component.DaggerAppComponent;
import com.hlxyedu.mhk.di.module.AppModule;
import com.hlxyedu.mhk.di.module.HttpModule;
import com.hlxyedu.mhk.model.bean.ExamProgressVO;
import com.hlxyedu.mhk.weight.material.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.skyworth.rxqwelibrary.app.AppConfig;
import com.skyworth.rxqwelibrary.app.BaseApplication;
import com.skyworth.rxqwelibrary.app.CrashHandler;
import com.skyworth.rxqwelibrary.service.InitializeService;
import com.umeng.commonsdk.UMConfigure;

import java.util.List;


/**
 * 作者：skyworth on 2017/7/10 10:00
 * 邮箱：dqwei@iflytek.com
 */

public class AppContext extends BaseApplication {

    private static final String TAG = AppContext.class.getSimpleName();

    private static AppContext instance;

    public static AppComponent appComponent;

    private String uid;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private int allItem;

    public int getAllItem() {
        return allItem;
    }

    public void setAllItem(int allItem) {
        this.allItem = allItem;
    }

    private int currentItem;

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private int currentPos;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    // 考试流程需要的存取数据设置的数据
    private List<ExamProgressVO> examProgressVOS;

    public List<ExamProgressVO> getExamProgressVOS() {
        return examProgressVOS;
    }

    public void setExamProgressVOS(List<ExamProgressVO> examProgressVOS) {
        this.examProgressVOS = examProgressVOS;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override//初始化其他信息
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 音频播放
//        MusicManager.initMusicManager(this);
        init();

        //在子线程中完成其他初始化
        InitializeService.start(this);

        //友盟
        UMConfigure.init(this, Constants.YOUMENG_KEY,null,0,null);

        SharedPreferences mSPrefs = getSharedPreferences(Constants.BOSS, Context.MODE_PRIVATE);
//        uid = mSPrefs.getString(Constants.UID, "");

//        Glide.get(this).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpsUtils.getUnsafeOkHttpClient()));

        //初始化崩溃信息
        initCrash();
    }

    private void initCrash() {
        // 获取异常信息捕获类实例
        //        开发期间不要监听 稍后放开
        CrashHandler crashHandler = CrashHandler.getInstance(getApplicationContext());

        crashHandler.setICrashHandlerListener(this);
        // 初始化
        crashHandler.init(getApplicationContext());
    }

    /*public String getUid() {
        if (ObjectUtils.isEmpty(uid)) {
            SharedPreferences mSPrefs = getSharedPreferences(Constants.BOSS, Context.MODE_PRIVATE);
            uid = mSPrefs.getString(Constants.UID, "");
        }
        return uid;
    }*/

    //退出时清空
    /*public void clearUp() {
        uid = "";
        userVO = null;
    }*/

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }

    /**
     * 获得当前app运行的AppContext
     */
    public static AppContext getInstance() {
        return instance;
    }

    private String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void init() {


        // 初始化音频录制工具
        RecorderManager.createInstance(this);

        AppConfig.getAppConfig(this);

    }

    static {//使用static代码段可以防止内存泄漏

//        //设置全局默认配置（优先级最低，会被其他设置覆盖）
//        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
//            @Override
//            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
//                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
//                layout.setReboundDuration(1000);
//                layout.setReboundInterpolator(new DropBounceInterpolator());
//                layout.setFooterHeight(100);
//                layout.setDisableContentWhenLoading(false);
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
//            }
//        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                layout.setEnableHeaderTranslationContent(false);
                return new MaterialHeader(context).setColorSchemeResources(R.color.colorRed,R.color.colorGreen,R.color.colorBlue);
            }
        });
    }

}
