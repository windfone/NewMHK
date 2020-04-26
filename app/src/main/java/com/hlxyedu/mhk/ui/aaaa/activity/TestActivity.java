package com.hlxyedu.mhk.ui.aaaa.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.base.RootActivity;
import com.hlxyedu.mhk.ui.aaaa.contract.TestContract;
import com.hlxyedu.mhk.ui.aaaa.presenter.TestPresenter;
import com.lansosdk.videoeditor.LanSoEditor;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.libyuv.LibyuvUtil;
import com.skyworth.rxqwelibrary.app.AppConstants;
import com.skyworth.rxqwelibrary.utils.RxTimerUtil;
import com.zhaoss.weixinrecorded.util.CameraHelp;
import com.zhaoss.weixinrecorded.util.MyVideoEditor;
import com.zhaoss.weixinrecorded.util.RecordUtil;
import com.zhaoss.weixinrecorded.util.RxJavaUtil;
import com.zhaoss.weixinrecorded.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ceshi
 */
public class TestActivity extends RootActivity<TestPresenter> implements TestContract.View {

    private SurfaceView surfaceView;
    private ArrayList<String> segmentList = new ArrayList<>();//分段视频地址
    private ArrayList<String> aacList = new ArrayList<>();//分段音频地址
    private ArrayList<Long> timeList = new ArrayList<>();//分段录制时间
    //是否在录制视频
    private AtomicBoolean isRecordVideo = new AtomicBoolean(false);
    //拍照
    private CameraHelp mCameraHelp = new CameraHelp();
    private SurfaceHolder mSurfaceHolder;
    private MyVideoEditor mVideoEditor = new MyVideoEditor();
    private RecordUtil recordUtil;
    private String audioPath;
    private RecordUtil.OnPreviewFrameListener mOnPreviewFrameListener;
    private String videos;
    private long videoDuration;
    private long recordTime;
    private String videoPath;

    // 倒计时到随机数的时候开始录制视频
    private int COUNT = 0;
    private RxTimerUtil timerUtil;


    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        return intent;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEventAndData() {
        LanSoEditor.initSDK(this, null);
        videos = AppConstants.VIDEO_RECORDING_PATH + System.currentTimeMillis() + "/";
        LanSongFileUtil.setFileDir(videos);
        LibyuvUtil.loadLibrary();

        initUI();
        initMediaRecorder();

        // 录视频
        initVideoRecord();
    }

    /**
     * 到随机的时间点 录制视频
     */
    private void initVideoRecord() {
        // *************** 随机数录视频 ***************//
        timerUtil = new RxTimerUtil();
        // 假如在 0-100，分为三个平均时间段,每段随机一个数，从该数开始录5秒视频
        long total = 150;

        long oneMin = 5;
        long oneMax = (long) (Math.floor(total / 3) - 10);

        long twoMin = (long) Math.floor(total / 3);
        long twoMax = (long) (Math.floor(total * 2 / 3) - 10);

        long threeMin = (long) Math.floor(total * 2 / 3);
        long threeMax = total - 10;

        long videoRecordOne = oneMin + (int) (Math.random() * ((oneMax - oneMin) + 1));
        long videoRecordTwo = twoMin + (int) (Math.random() * ((twoMax - twoMin) + 1));
        long videoRecordThree = threeMin + (int) (Math.random() * ((threeMax - threeMin) + 1));
        Log.e("==========111===", videoRecordOne + "");
        Log.e("==========222===", videoRecordTwo + "");
        Log.e("==========333===", videoRecordThree + "");

        timerUtil.interval(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                COUNT++;
                Log.e("================", COUNT + "");
                if (COUNT == videoRecordOne) {
                    // 到随机的数了开始录像
                    isRecordVideo.set(true);
                    startRecord();
                } else if (COUNT == videoRecordOne + 6) {
                    if (isRecordVideo.get()) {
                        isRecordVideo.set(false);
                        upEvent();
                    }
                }

                if (COUNT == videoRecordTwo) {
                    // 到随机的数了开始录像
                    isRecordVideo.set(true);
                    startRecord();
                } else if (COUNT == videoRecordTwo + 5) {
                    if (isRecordVideo.get()) {
                        isRecordVideo.set(false);
                        upEvent();
                    }
                }

                if (COUNT == videoRecordThree) {
                    // 到随机的数了开始录像
                    isRecordVideo.set(true);
                    startRecord();
                } else if (COUNT == videoRecordThree + 5){
                    if (isRecordVideo.get()) {
                        isRecordVideo.set(false);
                        upEvent();
                    }
                }

            }
        });
        // *******************************************//
    }

    @Override
    public void responeError(String errorMsg) {
    }

    private void initUI() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                int width = surfaceView.getWidth();
                int height = surfaceView.getHeight();
                float viewRatio = width * 1f / height;
                float videoRatio = 9f / 16f;
                ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
                if (viewRatio > videoRatio) {
                    layoutParams.width = width;
                    layoutParams.height = (int) (width / viewRatio);
                } else {
                    layoutParams.width = (int) (height * viewRatio);
                    layoutParams.height = height;
                }
                surfaceView.setLayoutParams(layoutParams);
            }
        });
    }

    private void initMediaRecorder() {
        mCameraHelp.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (isRecordVideo.get() && mOnPreviewFrameListener != null) {
                    mOnPreviewFrameListener.onPreviewFrame(data);
                }
            }
        });

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceHolder = holder;
                // TODO 1 ,改为默认前置
//                mCameraHelp.openCamera(mContext, Camera.CameraInfo.CAMERA_FACING_BACK, mSurfaceHolder);
                mCameraHelp.openCamera(mContext, Camera.CameraInfo.CAMERA_FACING_FRONT, mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraHelp.release();
            }
        });

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraHelp.callFocusMode();
            }
        });
    }

    public void finishVideo() {
        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<String>() {
            @Override
            public String doInBackground() throws Exception {
                //合并h264
                String h264Path = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".h264";
                Utils.mergeFile(segmentList.toArray(new String[]{}), h264Path);
                //h264转mp4
                String mp4Path = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".mp4";
                mVideoEditor.h264ToMp4(h264Path, mp4Path);
                //合成音频
//                String aacPath = mVideoEditor.executePcmEncodeAac(syntPcm(), RecordUtil.sampleRateInHz, RecordUtil.channelCount);
                //音视频混合
//                mp4Path = mVideoEditor.executeVideoMergeAudio(mp4Path, aacPath);
                mp4Path = mVideoEditor.executeVideoMergeAudio(mp4Path, "");
                return mp4Path;
            }

            @Override
            public void onFinish(String result) {
                // TODO 完成直接返回不裁剪,result 就是图片存储路径
                List<File> s = FileUtils.listFilesInDir(videos);
                for (int i = 0; i < s.size(); i++) {
                    if (StringUtils.equals(s.get(i).getPath(), result)) {
                        ToastUtils.showShort("完成录制");
                        break;
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtils.i("视频编辑失败");
            }
        });
    }

    private String syntPcm() throws Exception {
        String pcmPath = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".pcm";
        File file = new File(pcmPath);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        for (int x = 0; x < aacList.size(); x++) {
            FileInputStream in = new FileInputStream(aacList.get(x));
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                out.flush();
            }
            in.close();
        }
        out.close();
        return pcmPath;
    }

    private void startRecord() {

        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<Boolean>() {
            @Override
            public Boolean doInBackground() throws Throwable {
                videoPath = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".h264";
                audioPath = LanSongFileUtil.DEFAULT_DIR + System.currentTimeMillis() + ".pcm";
                final boolean isFrontCamera = mCameraHelp.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT;
                final int rotation;
                if (isFrontCamera) {
                    rotation = 270;
                } else {
                    rotation = 90;
                }
                recordUtil = new RecordUtil(videoPath, audioPath, mCameraHelp.getWidth(), mCameraHelp.getHeight(), rotation, isFrontCamera);
                return true;
            }

            @Override
            public void onFinish(Boolean result) {
                mOnPreviewFrameListener = recordUtil.start();
                videoDuration = 0;
                recordTime = System.currentTimeMillis();
                runLoopPro();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void runLoopPro() {

        RxJavaUtil.loop(20, new RxJavaUtil.OnRxLoopListener() {
            @Override
            public Boolean takeWhile() {
                return recordUtil != null && recordUtil.isRecording();
            }

            @Override
            public void onExecute() {
                long currentTime = System.currentTimeMillis();
                videoDuration += currentTime - recordTime;
                recordTime = currentTime;
                long countTime = videoDuration;
                for (long time : timeList) {
                    countTime += time;
                }
            }

            @Override
            public void onFinish() {
                segmentList.add(videoPath);
                aacList.add(audioPath);
                timeList.add(videoDuration);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void upEvent() {
        if (recordUtil != null) {
            recordUtil.stop();
            recordUtil = null;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishVideo();
                }
            }, 1000);
        }

    }

    /**
     * 清除录制信息
     */
    private void cleanRecord() {

        segmentList.clear();
        aacList.clear();
        timeList.clear();

    }

    @Override
    protected void onDestroy() {
        timerUtil.cancel();
        super.onDestroy();

        cleanRecord();
        if (mCameraHelp != null) {
            mCameraHelp.release();
        }
        if (recordUtil != null) {
            recordUtil.stop();
        }
    }

}