package com.hlxyedu.mhk.model.event;

/**
 * 事件集中定义
 *
 * @author RXXU
 */
public class EventsConfig {

    //退出登录
    public static final int LOGOUT_SUCCESS = 0x001;

    //注册成功
    public static final int REGISTER_SUCCESS = 0x002;

    //去除Activity
    public static final int KILL_ACTIVITY = 0x003;

    //测试中事件
    //下一页
    public static final int TEST_NEXT_PAGE = 0x201;

    //下一模块
    public static final int TEST_NEXT_PART = 0x203;

    //音频播放完成
    public static final int FINISH_PLAY = 0x906;

    //显示倒计时
    public static final int SHOW_DETAL_VIEW = 0x907;

    //开始录音
    public static final int START_RECORD = 0x908;

    //结束录音
    public static final int END_RECORD = 0x909;

    //关闭所有ACTIVITY
    public static final int KILL_ALL_ACTIVITY = 0x1001;
    //更新我的列表
    public static final int UPDATE_LIST = 0x1002;
    //关闭支付页面
    public static final int KILL_PAY_ACTIVITY = 0x1003;

    //关闭支付成功页面
    public static final int KILL_SUCCESS_ACTIVITY = 0x2001;


    //解析成功
//    public static final int SUCCESS = 0x2002;
    public static final int SUCCESS_LISTEN = 0x2004;
    public static final int SUCCESS_SPEAK = 0x2005;
    public static final int SUCCESS_READ = 0x2006;
    public static final int SUCCESS_BOOK = 0x2007;
    public static final int SUCCESS_WRITE = 0x2008;

    //关闭练习题页面
    public static final int KILL_TEST_ACTIVITY = 0x2003;
    //激活按键
    public static final int ACTIVATE_BUTTON = 0x01;
   //语音播放进度条
    public static final int SHOW_AUDIO_VIEW=0x02;

}
