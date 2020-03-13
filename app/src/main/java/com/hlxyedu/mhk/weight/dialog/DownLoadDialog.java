package com.hlxyedu.mhk.weight.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.base.SimpleFragment;

import java.lang.ref.WeakReference;

public class DownLoadDialog {

    private static volatile DownLoadDialog instance;

    private TextView loadingProgressTv;

    //构造函数修饰为private防止在其他地方创建该实例
    private DownLoadDialog(){

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
    public static DownLoadDialog getInstance(){
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null){
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (DownLoadDialog.class){
                if (instance == null){
                    instance = new DownLoadDialog();
                }
            }
        }
        return instance;
    }

    //显示弹框
    public void showDownloadDialog(Context context) {
        DialogPlus downloadDialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.download_file_dialog))
                .setGravity(Gravity.CENTER)
                .setContentWidth(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
//                .setContentBackgroundResource(R.drawable.toast_bg)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                    }
                })
                .create();
        loadingProgressTv = (TextView) downloadDialog.findViewById(R.id.loading_progress_tv);
        downloadDialog.show();
    }

}
