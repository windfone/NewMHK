package com.hlxyedu.mhk.model.event;

public class ReExamEvent {

    public static final String RE_EXAM = "RE_EXAM";
    public static final String SAVE_COMPOSITION = "SAVE_COMPOSITION";

    public static final String LISTENING = "LISTENING";
    public static final String READ = "READ";
    public static final String BOOK = "BOOK";
    public static final String COMPOSITION = "COMPOSITION";

    private String type;

    private String questionType;

    public ReExamEvent(String type) {
        super();
        this.type = type;
    }

    public ReExamEvent(String type, String questionType) {
        super();
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
