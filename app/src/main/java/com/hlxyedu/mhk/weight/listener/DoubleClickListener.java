package com.hlxyedu.mhk.weight.listener;

import android.view.View;

import java.util.Calendar;

public abstract class DoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 600;//这里设置不能超过多长时间
    private long lastClickTime = 0;

    protected abstract void onDoubleClick(View v);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME) {
            onDoubleClick(v);
        } else {
            lastClickTime = currentTime;
        }

    }


}
