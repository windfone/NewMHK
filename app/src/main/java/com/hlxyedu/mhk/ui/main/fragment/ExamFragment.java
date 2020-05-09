package com.hlxyedu.mhk.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootFragment;
import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.event.ReExamEvent;
import com.hlxyedu.mhk.ui.main.adapter.ExamAdapter;
import com.hlxyedu.mhk.ui.main.contract.ExamContract;
import com.hlxyedu.mhk.ui.main.presenter.ExamPresenter;
import com.hlxyedu.mhk.weight.MyLinearLayoutManager;
import com.hlxyedu.mhk.weight.actionbar.XBaseTopBar;
import com.hlxyedu.mhk.weight.dialog.MoreTaskDLDialog;
import com.hlxyedu.mhk.weight.listener.DoubleClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.skyworth.rxqwelibrary.app.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangguihua
 */
public class ExamFragment extends RootFragment<ExamPresenter> implements ExamContract.View {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.xbase_topbar)
    XBaseTopBar xbaseTopbar;

    private ExamAdapter mAdapter;

    private List<ExamVO> dataVOList = new ArrayList<>();
    private int pageSize = 20;
    private int count = 1; // 当前页数;

    // 点home键退出 重启哪个activity
    private String restartWhat = "";

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;


    public static ExamFragment newInstance() {
        Bundle args = new Bundle();

        ExamFragment fragment = new ExamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exam;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        stateLoading();
        Logger.d("进入考试列表");

        mPresenter.saveLog(mPresenter.getID(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), restartWhat + "进入考试列表");

        showLongMessageDialog();

        mAdapter = new ExamAdapter(R.layout.item_exercise, dataVOList);
        rlv.setLayoutManager(new MyLinearLayoutManager(mActivity));
        rlv.setAdapter(mAdapter);

        count = 1;
        if (!dataVOList.isEmpty()) {
            dataVOList.clear();
        }
        mPresenter.getMockList(mPresenter.getID(), count, pageSize);

        mAdapter.setPreLoadNumber(1);
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.getMockList(mPresenter.getID(), ++count, pageSize);
        }, rlv);

        // 双击标题栏 返回列表顶部
        xbaseTopbar.setOnClickListener(new DoubleClickListener() {
            @Override
            protected void onDoubleClick(View v) {
                rlv.smoothScrollToPosition(0);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                count = 1;
                dataVOList.clear();
                mPresenter.getMockList(mPresenter.getID(), count, pageSize);
            }
        });

    }

    @Override
    public void onSuccess(List<ExamVO> examVOS) {
        refreshLayout.finishRefresh();

        if (!examVOS.isEmpty()) {
            dataVOList.addAll(examVOS);
            mAdapter.setNewData(dataVOList);
            if (examVOS.size() < pageSize) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
            stateMain();
        } else {
            if (count == 1) {
                stateEmpty("暂无内容");
            } else {
                mAdapter.loadMoreEnd();
            }
        }

    }

    @Override
    public void download() {
        Logger.d("准备下载试卷");

        MoreTaskDLDialog downloadDialog = new MoreTaskDLDialog(mActivity);
        downloadDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次进入考试列表页都删除
        FileUtils.deleteDir(AppConstants.VIDEO_RECORDING_PATH);
        FileUtils.deleteDir(AppConstants.FILE_DOWNLOAD_PATH);
        FileUtils.deleteDir(AppConstants.UNFILE_DOWNLOAD_PATH);

        refreshLayout.autoRefresh();

        if (!StringUtils.isEmpty(restartWhat)) {
            mPresenter.saveLog(mPresenter.getID(), DeviceUtils.getModel() + DeviceUtils.getSDKVersionCode(), restartWhat + "页面退出、切屏或锁屏");
            restartWhat = "";
        }
    }

    @Override
    public void reExamination(String questionType) {
        switch (questionType) {
            case ReExamEvent.LISTENING:
                restartWhat = ReExamEvent.LISTENING;

                break;
            case ReExamEvent.READ:
                restartWhat = ReExamEvent.READ;

                break;
            case ReExamEvent.BOOK:
                restartWhat = ReExamEvent.BOOK;

                break;
            case ReExamEvent.COMPOSITION:
                restartWhat = ReExamEvent.COMPOSITION;

                break;
        }
    }

    @Override
    public void responeError(String errorMsg) {
        stateError();
    }


    private void showLongMessageDialog() {
        WindowManager windowManager = (WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DialogPlus noticeDialog = DialogPlus.newDialog(mActivity)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_notice))
                .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                .setContentWidth((int) (display
                        .getWidth() * 0.8))
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)//设置不可取消   可以取消
                .setOnClickListener((dialog, view1) -> {
                    switch (view1.getId()) {
                        case R.id.txt_confirm:
                            dialog.dismiss();
                            break;
                    }
                }).create();
        TextView textView = (TextView) noticeDialog.findViewById(R.id.txt_msg);
        textView.setText("1、退出手机微信、QQ等其他一切通信软件的账号\n" +
                "2、开启手机飞行模式\n" +
                "3、打开手机wifi，连接无线网络，并保持网络畅通\n" +
                "4、考试期间禁止手动锁屏\n" +
                "5、考试期间禁止切换程序\n" +
                "6、考试期间保持手机电量充足\n" +
                "7、考试期间，手机前置摄像头必须对准本人，考试过程中将进行全程录像，如录像采集不到本人，则视为考试作弊，取消考试资格\n\n" +
                "如有发生以上情况，导致考试程序中止，造成考试成绩无法上传等问题，后果自负");
        noticeDialog.show();
        /*new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("考试须知")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setMessage("1、退出手机微信、QQ等其他一切通信软件的账号\n" +
                        "2、开启手机飞行模式\n" +
                        "3、打开手机wifi，连接无线网络，并保持网络畅通\n" +
                        "4、考试期间禁止手动锁屏\n" +
                        "5、考试期间禁止切换程序\n" +
                        "6、考试期间保持手机电量充足\n" +
                        "7、考试期间，手机前置摄像头必须对准本人，考试过程中将进行全程录像，如录像采集不到本人，则视为考试作弊，取消考试资格\n\n" +
                        "如有发生以上情况，导致考试程序中止，造成考试成绩无法上传等问题，后果自负")
                .addAction("我已认真阅读", (dialog, index) -> dialog.dismiss())
                .create(mCurrentDialogStyle).show();*/
    }

}