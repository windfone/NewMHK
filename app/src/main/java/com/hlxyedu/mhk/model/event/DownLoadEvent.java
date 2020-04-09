package com.hlxyedu.mhk.model.event;

public class DownLoadEvent {

    public static final String DOWNLOAD_PAPER_EXERCISE = "DOWNLOAD_PAPER_EXERCISE";

    public static final String DOWNLOAD_PAPER_OPERATION = "DOWNLOAD_PAPER_OPERATION";

    public static final String DOWNLOAD_PAPER_EXAM = "DOWNLOAD_PAPER_EXAM";

    public static final String DOWNLOAD_COMPLETE = "DOWNLOAD_COMPLETE";

    private String type;

    private Object downloadPath;

    private String examName;

    private String examId;

    private String homeworkId;

    private String examType;

    private int pos;

    public DownLoadEvent(String type) {
        this.type = type;
    }

    public DownLoadEvent(String type, int pos, String examType, Object downloadPath, String examName, String examId, String homeworkId) {
        this.type = type;
        this.pos = pos;
        this.examType = examType;
        this.downloadPath = downloadPath;
        this.examName = examName;
        this.examId = examId;
        this.homeworkId = homeworkId;
    }

    public String getType() {
        return type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Object getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(Object downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

}
