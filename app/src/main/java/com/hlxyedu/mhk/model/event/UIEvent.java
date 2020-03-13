package com.hlxyedu.mhk.model.event;

public class UIEvent {

    public static final String UI_CONTROL = "ui_control";

    private String type;
    private boolean isVisible;

    public UIEvent(String type,boolean isVisible) {
        this.type = type;
        this.isVisible = isVisible;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
