package com.hlxyedu.mhk.ui.main.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.blankj.utilcode.util.StringUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragmentActivity;
import com.hlxyedu.mhk.model.bean.VersionVO;
import com.hlxyedu.mhk.ui.main.contract.MainContract;
import com.hlxyedu.mhk.ui.main.fragment.ExamFragment;
import com.hlxyedu.mhk.ui.main.presenter.MainPresenter;
import com.hlxyedu.mhk.utils.PermissionSettingUtil;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.weight.bottombar.BottomBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zhangguihua
 */
public class MainActivity extends RootFragmentActivity<MainPresenter> implements MainContract.View {

    public static final int FIRST = 0;
    //    public static final int SECOND = 1;
//    public static final int THIRD = 2;
    //    public static final int FOURTH = 3;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
//    List<String> navigations = Arrays.asList("练习", "作业", "考试", "我的");
//    List<Integer> bottomIcons = Arrays.asList(R.drawable.icon_bar_selector_exercise, R.drawable.icon_bar_selector_operation,
//            R.drawable.icon_bar_selector_exam, R.drawable.icon_bar_selector_mine);
//    private SupportFragment[] mFragments = new SupportFragment[4];

    List<String> navigations = Arrays.asList("考试");
    List<Integer> bottomIcons = Arrays.asList(R.drawable.icon_bar_selector_exam);
    private SupportFragment[] mFragments = new SupportFragment[1];

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        // 保持屏幕唤醒状态
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //设置宽高
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        mBottomBar.initBottomBar(screenWidth, MainActivity.this, navigations, bottomIcons);

//        SupportFragment firstFragment = findFragment(ExerciseFragment.class);
        SupportFragment thirdFragment = findFragment(ExamFragment.class);
        if (thirdFragment == null) {
//            mFragments[FIRST] = ExerciseFragment.newInstance();
//            mFragments[SECOND] = OperationFragment.newInstance();
            mFragments[FIRST] = ExamFragment.newInstance();
//            mFragments[FOURTH] = MineFragment.newInstance();
//
//            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
//                    mFragments[FIRST], mFragments[SECOND], mFragments[THIRD], mFragments[FOURTH]);
            loadMultipleRootFragment(R.id.fl_tab_container, FIRST, mFragments[FIRST]);

        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.findFragmentByTag自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = thirdFragment;
//            mFragments[FIRST] = firstFragment;
//            mFragments[SECOND] = findFragment(OperationFragment.class);
//            mFragments[THIRD] = findFragment(ExamFragment.class);
//            mFragments[FOURTH] = findFragment(MineFragment.class);
        }

        initView();
        mBottomBar.setCurrentItem(0);

        mPresenter.checkNewVersion();

        checkPermissions();

        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separatorChar + "Android" + File.separatorChar + "data" + File.separatorChar + getPackageName() + File.separatorChar + "log" + File.separatorChar + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        List<File> files = RegUtils.listFilesInDirWithFilter(RegUtils.getFileByPath(folder), "log");
        mPresenter.uploadLogFileBatch(files);
    }

    private void initView() {
        mBottomBar.setOnTabSelectedListener((position, prePosition) -> {
            showHideFragment(mFragments[position], mFragments[prePosition]);
            switch (position) {
                case 0:
//                    main_topbar.setVisibility(View.VISIBLE);
//                    main_topbar.setMiddleText(getResources().getString(R.string.home_title));
                    break;
                case 1:
//                    main_topbar.setVisibility(View.VISIBLE);
//                    main_topbar.setMiddleText(getResources().getString(R.string.news_flash));
                    break;
                case 2:
//                    main_topbar.setVisibility(View.VISIBLE);
//                    main_topbar.setMiddleText(getResources().getString(R.string.bookshelf));
                    break;
                case 3:
//                    main_topbar.setVisibility(View.GONE);
                    break;
            }
        });
    }

    /**
     * 退出登录后 需要重新进入main时 调用， 在此需要清空数据
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mBottomBar.setCurrentItem(0);
    }

    //连续俩下 退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void responeError(String errorMsg) {

    }

    @SuppressLint("CheckResult")
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) this);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // 权限同意
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        // 禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        // 禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        // 需要到 设置里面 手动打开
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("权限申请");
                        builder.setMessage("需要同意录音、存储、获取手机状态信息权限才能正常使用哦");
                        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionSettingUtil.gotoPermission(MainActivity.this);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                });
    }

    @Override
    public void versionSuccess(VersionVO versionVO, String apkUrl) {
        // 0 选择更新  1 强制更新
        sendRequest(apkUrl, versionVO.getVersionDesc(),
                StringUtils.equalsIgnoreCase(versionVO.getUpdateType(), "1"));
    }

    //*********************************************************************************************//

    /**
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     * forceUpdate
     */
    public void sendRequest(final String url, final String contain, Boolean forceUpdate) {

        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
//                .setRequestUrl("http://test-1251233192.coscd.myqcloud.com/1_1.apk")
                .setRequestUrl(url)
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
//                        Toast.makeText(context, "request successful", Toast.LENGTH_SHORT)
//                                .show();
                        return (UIData) crateUIData(url, contain);
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
//                        Toast.makeText(context, "request failed", Toast.LENGTH_SHORT)
//                                .show();

                    }
                });
        if (forceUpdate) {
            builder.setForceUpdateListener(() -> {
//                Toast.makeText(this, "force update handle", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
        builder.setForceRedownload(true);
        builder.executeMission(this);
    }


    private UIData crateUIData(String url, String contain) {
        UIData uiData = UIData.create();
        uiData.setTitle("版本升级");
//        uiData.setDownloadUrl("http://test-1251233192.coscd.myqcloud.com/1_1.apk");
        uiData.setDownloadUrl(url);
        uiData.setContent(contain);
        return uiData;
    }
}