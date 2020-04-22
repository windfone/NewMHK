package com.skyworth.rxqwelibrary.widget;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.skyworth.rxqwelibrary.R;

public class NetErrorDialog {

    private static volatile NetErrorDialog instance;

    //构造函数修饰为private防止在其他地方创建该实例
    private NetErrorDialog() {

    }

    /**
     * 有的代码中会将同步锁synchronized写在方法中，例如：
     * public static synchronized PermissionRequestUtil getInstance(){.....}
     * 造成的弊端就是：多线程每次在调用getInstance()时都会产生一个同步，造成损耗。
     * 相应的只需要保持同步的代码块仅仅就是：
     * singletonMode = new SingletonMode();
     * 所以只要在该代码处添加同步锁就可以了
     *
     * @return
     */
    public static NetErrorDialog getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (NetErrorDialog.class) {
                if (instance == null) {
                    instance = new NetErrorDialog();
                }
            }
        }
        return instance;
    }

    public void showNetErrorDialog(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_net))
                .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                .setContentWidth((int) (display
                        .getWidth() * 0.8))
                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setOnClickListener((dialog, view) -> {
                    if (view.getId() == R.id.confirm_btn) {
                        dialog.dismiss();
                    }

                }).create();
        dialogPlus.show();
    }

}
