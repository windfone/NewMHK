package com.hlxyedu.mhk.model.event;

public class ExamEvent {

    public static final String EXAM_FINISH = "EXAM_FINISH";

    private String type;

    public ExamEvent(String type) {
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
