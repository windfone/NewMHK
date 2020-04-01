package com.hlxyedu.mhk.model.event;

public class ClickEvent {

    public static final String CLICK_POS = "CLICK_POS";

    private String type;

    private int pos;

    public ClickEvent(String type,int pos) {
        super();
        this.type = type;
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
