package com.hlxyedu.mhk.model.bean;

public class ExamProgressVO {

    private String id; // 考试id 也就是testId
    private String type; // 练习时为空 ， homeWork/作业，exam/考试
    private String examId; // 试卷id
    private String zipPath; //试卷下载路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
