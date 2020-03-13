package com.skyworth.rxqwelibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 作者：skyworth on 2017/7/13 09:27
 * 邮箱：dqwei@iflytek.com
 * 无MVP的Fragment基类
 */

public abstract class SimpleFragment extends SupportFragment {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    protected boolean isInited = false;

    private ActivityHandler mHandler;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new ActivityHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }
    public <T extends View> T getView(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

    /**
     * 处理消息的Handler，静态内部类，防止内存泄露
     *
     * @author hhsun@iflytek.com
     * @version Create Time：Jul 6, 2015 3:07:32 PM
     */
    public static class ActivityHandler extends Handler {
        WeakReference<SimpleFragment> reference;

        public ActivityHandler(SimpleFragment simpleFragment) {
            reference = new WeakReference<SimpleFragment>(simpleFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            SimpleFragment bf = reference.get();
            //强制退出时 已回收 所以需要判断
            if(bf != null)
            {
                bf.handleMessage(msg);
            }
        }
    }
    /**
     * 处理getHandler()发送的Message
     *
     * @param msg
     */
    public void handleMessage(Message msg) {

    }

    /**
     * 获得Handler对象
     *
     * @return
     */
    public Handler getHandler() {
        return mHandler;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        isInited = true;
        initEventAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    protected abstract int getLayoutId();
    protected abstract void initEventAndData();
}
