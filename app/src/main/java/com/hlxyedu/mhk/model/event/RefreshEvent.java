package com.hlxyedu.mhk.model.event;

public class RefreshEvent {

    public static final String REFRESH_EVENT = "REFRESH_EVENT";

    private String type;

    public RefreshEvent(String type) {
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
