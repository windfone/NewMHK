package com.hlxyedu.mhk.model.prefs;

/**
 * @author: skyworth
 * @date: 2017/4/21
 * @description:
 */

public interface PreferencesHelper {

    boolean getNightModeState();

    void setNightModeState(boolean state);

    /**
     * Set login status
     *
     * @param isLogin IsLogin
     */
    void setLoginStatus(boolean isLogin);

    /**
     * Get login status
     *
     * @return login status
     */
    boolean getLoginStatus();

    //uid
    void setUid(String uid);


    //第一次登录
    void setIsFrist(boolean isFrist);

    boolean getIsFrist();

    //登录信息
    void setUserInfo(String userInfo);

    String getSpUserInfo();

    void clearLoginInfo();

    // 保存考试过程中退出 考试进程数据
    void saveOfflineData(String dataVO);

    String getOfflineData();

    // 保存按home 键退出的时候写的作文题，续考时自动填上
    void saveReExamComposition(String txt);

    String getReExamComposition();

}
