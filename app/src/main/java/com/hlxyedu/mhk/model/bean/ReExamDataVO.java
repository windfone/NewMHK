package com.hlxyedu.mhk.model.bean;

public class ReExamDataVO {

    // 当前考试到第几套试卷了
    private int current;
    // 听力
    private String learningData;
    // 阅读
    private String readData;
    // 书面
    private String bookData;
    // 作文
    private String txtData;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getLearningData() {
        return learningData;
    }

    public void setLearningData(String learningData) {
        this.learningData = learningData;
    }

    public String getReadData() {
        return readData;
    }

    public void setReadData(String readData) {
        this.readData = readData;
    }

    public String getBookData() {
        return bookData;
    }

    public void setBookData(String bookData) {
        this.bookData = bookData;
    }

    public String getTxtData() {
        return txtData;
    }

    public void setTxtData(String txtData) {
        this.txtData = txtData;
    }
}
