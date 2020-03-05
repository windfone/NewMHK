package com.hlxyedu.mhk.model.event;


public class SelectEvent {

    public static final String EXERCISE_SEL = "EXERCISE_SEL";

    public static final String EXAM_SEL = "EXAM_SEL";

    public static final String OPERATION_SEL = "OPERATION_SEL";

    private String type;

    private String questionType;

    public SelectEvent(String type,String questionType){
        this.type = type;
        this.questionType = questionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

}
