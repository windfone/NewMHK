package com.hlxyedu.mhk.model.event;

public class DownLoadEvent {

    public static final String DOWNLOAD_PAPER = "DOWNLOAD_PAPER";

    private String type;

    private String downloadPath;

    private String examName;

    private int pos;

    public DownLoadEvent(String type,int pos,String downloadPath,String examName){
        this.type = type;
        this.pos = pos;
        this.downloadPath = downloadPath;
        this.examName = examName;
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

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }
}
