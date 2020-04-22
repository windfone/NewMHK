package com.hlxyedu.mhk.model.event;

/*中途退出提交答案*/
public class ExitCommitEvent {

    public static final String EXIT_COMMIT = "exit_commit_answer";

    private String type;

    private Object answer;

    private String examId;

    private String homeworkId;

    private String testId;

    private String testType;

  /*  public CommitEvent(String type, Object answer,String examId,String homeworkId) {
        this.type = type;
        this.answer = answer;
        this.examId = examId;
        this.homeworkId = homeworkId;
    }*/

    /*public CommitEvent(String type, Object answer,String examId,String homeworkId,String testId) {
        this.type = type;
        this.answer = answer;
        this.examId = examId;
        this.homeworkId = homeworkId;
        this.testId = testId;
    }*/

    public ExitCommitEvent(String type,String examId, String homeworkId, String testId, String testType) {
        this.type = type;
        this.examId = examId;
        this.homeworkId = homeworkId;
        this.testId = testId;
        this.testType = testType;
    }

    public ExitCommitEvent(String type, Object answer, String examId, String homeworkId, String testId, String testType) {
        this.type = type;
        this.answer = answer;
        this.examId = examId;
        this.homeworkId = homeworkId;
        this.testId = testId;
        this.testType = testType;
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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String paperId) {
        this.examId = paperId;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}