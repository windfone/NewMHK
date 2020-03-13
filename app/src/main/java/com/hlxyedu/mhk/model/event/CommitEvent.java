package com.hlxyedu.mhk.model.event;

public class CommitEvent {

    public static final String COMMIT = "commit_answer";

    private String type;

    private Object answer;

    private String paperId;

    private String homeworkId;

    public CommitEvent(String type, Object answer,String paperId,String homeworkId) {
        this.type = type;
        this.answer = answer;
        this.paperId = paperId;
        this.homeworkId = homeworkId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }
}
