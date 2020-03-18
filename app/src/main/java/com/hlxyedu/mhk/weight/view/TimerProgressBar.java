package com.hlxyedu.mhk.weight.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hlxyedu.mhk.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerProgressBar extends LinearLayout {
    public static final int WAIT = 0;
    public static final int RECORD = 1;
    private Context mContext;
    private int WAIT_TIME;
    private int restTime = 0;
    private View view;
    private TimerTask timerTask;
    private ProgressBar timeProgressBar;
    private int type = 0;
    private OnClickCallBack mCallback;
    private Timer timer;
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            restTime += 1;
            timeProgressBar.setProgress(restTime);
            if (restTime == WAIT_TIME) {
                clearTimer();
            }

        }

        ;

    };


    public TimerProgressBar(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public TimerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.layout_time_progress, null);
        timeProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addView(view);
    }

    public void startProgress(int WAIT_TIME, int type, int proccess) {
        if (timer == null) {
            timer = new Timer();
        }
        this.WAIT_TIME = WAIT_TIME;
        this.type = type;
        timeProgressBar.setMax(WAIT_TIME);
        if (timerTask == null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            };

        }
        timer.schedule(timerTask, 0, proccess);
    }

    public void setOnClickCallBackListener(OnClickCallBack c) {
        mCallback = c;
    }

    public void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }


        restTime = 0;
        timeProgressBar.setProgress(0);
        this.setVisibility(GONE);

    }


    public static interface OnClickCallBack {

        public void onTimerClick(int type);
    }

}
