package com.hlxyedu.mhk.ui.splash.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.login.activity.LoginActivity;
import com.hlxyedu.mhk.ui.splash.contract.GuideContract;
import com.hlxyedu.mhk.ui.splash.presenter.GuidePresenter;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * Created by zhangguihua
 */
public class GuideActivity extends RootActivity<GuidePresenter> implements GuideContract.View {

    @BindView(R.id.banner_guide)
    RelativeLayout banner_guide;

    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;

    @BindView(R.id.banner_guide_foreground)
    BGABanner mForegroundBanner;

    /**
     * 打开新Activity
     *
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initEventAndData() {
        setListener();
        processLogic();
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                mPresenter.setIsFirst(false);
                startActivity(LoginActivity.newInstance(getBaseContext()));
                finish();
            }
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mBackgroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.icon_guide_background1,
                R.drawable.icon_guide_background2,
                R.drawable.icon_guide_background3);

        mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.icon_guide_foreground1,
                R.drawable.icon_guide_foreground2,
                R.drawable.icon_guide_foreground3);
    }


    @Override
    public void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }

    @Override
    public void responeError(String errorMsg) {

    }
}