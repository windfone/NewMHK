package com.hlxyedu.mhk.model.event;


public class SelectEvent {

    public static final String EXERCISE_SEL = "EXERCISE_SEL";

    public static final String EXAM_SEL = "EXAM_SEL";

    public static final String OPERATION_SEL = "OPERATION_SEL";

    public static final String SELECT = "SELECT";

    private String type;

    private String stateSelect;

    private String questionType;

    private String exerciseSelect;


    public SelectEvent(String type,String stateSelect,String questionType,String exerciseSelect){
        this.type = type;
        this.stateSelect = stateSelect;
        this.stateSelect = stateSelect;
        this.questionType = questionType;
        this.exerciseSelect = exerciseSelect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStateSelect() {
        return stateSelect;
    }

    public void setStateSelect(String stateSelect) {
        this.stateSelect = stateSelect;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getExerciseSelect() {
        return exerciseSelect;
    }

    public void setExerciseSelect(String exerciseSelect) {
        this.exerciseSelect = exerciseSelect;
    }

}
