package com.hlxyedu.mhk.model.event;

public class SaveLogEvent {

    public static final String SAVE_LOG = "save_log";

    private String type;

    private String log;

    public SaveLogEvent(String type, String log) {
        super();
        this.type = type;
        this.log = log;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
