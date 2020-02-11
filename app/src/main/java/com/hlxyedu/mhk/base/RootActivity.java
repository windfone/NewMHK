package com.hlxyedu.mhk.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.hlxyedu.mhk.R;
import com.skyworth.rxqwelibrary.base.BasePresenter;
import com.skyworth.rxqwelibrary.widget.ProgressImageView;

/**
 * @author: skyworth
 * @date: 2017/4/21
 * @desciption:
 */

public abstract class RootActivity<T extends BasePresenter> extends BaseActivity<T>{

    private static final int STATE_MAIN = 0x00;
    private static final int STATE_LOADING = 0x01;
    private static final int STATE_ERROR = 0x02;
    private static final int STATE_EMPTY = 0x03;

    private ProgressImageView ivLoading;
    private View viewError;
    private View  viewEmpty;
    private View viewLoading;
    private ViewGroup viewMain;
    private ViewGroup mParent;

    private int mErrorResource = R.layout.view_error;
    private int mEmptyResource= R.layout.view_empty;
    private int currentState = STATE_MAIN;
    private boolean isErrorViewAdded = false;

    @Override
    protected void initEventAndData() {
        viewMain = (ViewGroup) findViewById(R.id.view_main);
        if (viewMain == null) {
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain drawable-xxhdpi View named 'view_main'.");
        }
        if (!(viewMain.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "view_main's ParentView should be drawable-xxhdpi ViewGroup.");
        }
        mParent = (ViewGroup) viewMain.getParent();
        View.inflate(mContext, R.layout.view_progress, mParent);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            View.inflate(mContext, R.layout.view_progress, mParent);
//        }
//        else {
//            View.inflate(mContext, R.layout.view_progress_19, mParent);
//        }
        viewLoading = mParent.findViewById(R.id.view_loading);
        ivLoading = (ProgressImageView) viewLoading.findViewById(R.id.iv_progress);
        viewLoading.setVisibility(View.GONE);
        viewMain.setVisibility(View.VISIBLE);

    }

    @Override
    public void stateError() {
        if (currentState == STATE_ERROR)
            return;
        if (!isErrorViewAdded) {
            isErrorViewAdded = true;
            View.inflate(mContext, mErrorResource, mParent);
            viewError = mParent.findViewById(R.id.view_error);
            if (viewError == null) {
                throw new IllegalStateException(
                        "A View should be named 'view_error' in ErrorLayoutResource.");
            }
        }
        hideCurrentView();
        currentState = STATE_ERROR;
        viewError.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateEmpty(String msg) {
        if (currentState == STATE_EMPTY)
            return;
        if (!isErrorViewAdded) {
            isErrorViewAdded = true;
            View.inflate(mContext, mEmptyResource, mParent);
            viewEmpty = mParent.findViewById(R.id.view_empty);
//            mParent.findViewById(R.id.btn_back_mian).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    RxBus.getDefault().post(new DoExciseEvent(DoExciseEvent.DOEXCISE));
//                }
//            });

            if(!ObjectUtils.isEmpty(msg)){
                TextView error_tv = (TextView) mParent.findViewById(R.id.empty_tv);
                error_tv.setText(msg);
            }

            if (viewEmpty == null) {
                throw new IllegalStateException(
                        "A View should be named 'view_error' in ErrorLayoutResource.");
            }
        }
        hideCurrentView();
        currentState = STATE_EMPTY;
        viewEmpty.setVisibility(View.VISIBLE);
    }



    @Override
    public void stateLoading() {
        if (currentState == STATE_LOADING)
            return;
        hideCurrentView();
        currentState = STATE_LOADING;
        viewLoading.setVisibility(View.VISIBLE);
        ivLoading.start();
    }

    @Override
    public void stateMain() {
        if (currentState == STATE_MAIN)
            return;
        hideCurrentView();
        currentState = STATE_MAIN;
        viewMain.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
                viewMain.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                ivLoading.stop();
                viewLoading.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                if (viewError != null) {
                    viewError.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setErrorResource(int errorLayoutResource) {
        this.mErrorResource = errorLayoutResource;
    }
}