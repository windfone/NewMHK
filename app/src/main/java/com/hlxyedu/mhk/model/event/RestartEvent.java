package com.hlxyedu.mhk.model.event;

public class RestartEvent {

    public static final String RESTART = "RESTART";

    private String type;

    public RestartEvent(String type) {
        super();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
