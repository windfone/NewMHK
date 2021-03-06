package com.hlxyedu.mhk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class PermissionRequestUtil {

    private static volatile PermissionRequestUtil instance;

    //构造函数修饰为private防止在其他地方创建该实例
    private PermissionRequestUtil(){

    }

    /**
     * 有的代码中会将同步锁synchronized写在方法中，例如：
     *    public static synchronized PermissionRequestUtil getInstance(){.....}
     * 造成的弊端就是：多线程每次在调用getInstance()时都会产生一个同步，造成损耗。
     * 相应的只需要保持同步的代码块仅仅就是：
     *      singletonMode = new SingletonMode();
     * 所以只要在该代码处添加同步锁就可以了
     * @return
     */
    public static PermissionRequestUtil getInstance(){
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null){
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (PermissionRequestUtil.class){
                if (instance == null){
                    instance = new PermissionRequestUtil();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public void checkPermissions(Context context){
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // 权限同意
//                        recordSecond();
//                        RxBus.getDefault().post(new RecordEvent(RecordEvent.START_RECORD));
                        ToastUtils.showShort("开始录音");

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        // 禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                        showRequestReason(context);
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        // 禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        // 需要到 设置里面 手动打开
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("权限申请");
                        builder.setMessage("需要同意录音、存储、获取手机状态信息权限才能正常使用哦");
                        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionSettingUtil.gotoPermission(context);
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

    public void showRequestReason(Context context){

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DialogPlus reasonDialog = DialogPlus.newDialog(context)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_logout))
//                        .setContentBackgroundResource(R.drawable.dialog_write_corner_bg)
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
                            checkPermissions(context);
                            dialog.dismiss();
                            break;
                    }
                }).create();
        TextView textView = (TextView) reasonDialog.findViewById(R.id.txt_msg);
        textView.setText(context.getResources().getString(R.string.permission));
        reasonDialog.show();
    }

}
