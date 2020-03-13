package com.hlxyedu.mhk.model.models;

import com.hlxyedu.mhk.model.models.ListenQOptionModel;

import java.util.List;

/**
 * Created by weidingqiang on 16/4/26.
 */
public class BasePageModel {

    public static final String TEXT = "text";

    public static final String RICHTEXT = "richtext";

    public static final String WAIT = "wait";

    public static final String AUDIO = "audio";

    public static final String QUESTION = "question";

    public static final String QUESTIONS = "questions";

    public static final String SELECT = "select";

    public static final String RECORD = "record";

    private String questionid;

    //当前节点
    private int current;
    //类型
    private String type;
    //文本内容
    private String content;
    //源文件
    private String src;
    //等待时间
    private String timeout;
    //选择题
    private List<ListenQOptionModel> listenQOptionModels;

    //后缀名  richtext 类型  png 或者 txt
    private String suffix = "txt";

    //是否显示或者播放
    private boolean isShow = false;

    public BasePageModel(){

    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public List<ListenQOptionModel> getListenQOptionModels() {
        return listenQOptionModels;
    }

    public void setListenQOptionModels(List<ListenQOptionModel> listenQOptionModels) {
        this.listenQOptionModels = listenQOptionModels;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
