package com.hlxyedu.mhk.model.bean;

import java.util.List;

public class ExamVO {


    /**
     * id : 00000000000000025001
     * teacherId : null
     * teacherName : null
     * testName : 听力+口语
     * totalPoints : null
     * testStartTime : null
     * startTime : 2020-03-23
     * testDuration : null
     * testEndTime : null
     * state : 0
     * createTime : null
     * examId : null
     * answerNum : null
     * passDay : null
     * examZips : [{"examId":"3","zipPath":"/resources/mhk/mobie/MHKMN3005TL(1).zip"}]
     */

    private String id;
    private String teacherId;
    private String teacherName;
    private String testName;
    private String totalPoints;
    private String testStartTime;
    private String startTime;
    private String testDuration;
    private String testEndTime;
    private String state;
    private String createTime;
    private String examId;
    private String answerNum;
    private String passDay;
    private List<ExamZipsBean> examZips;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(String testStartTime) {
        this.testStartTime = testStartTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(String testDuration) {
        this.testDuration = testDuration;
    }

    public String getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(String testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    public String getPassDay() {
        return passDay;
    }

    public void setPassDay(String passDay) {
        this.passDay = passDay;
    }

    public List<ExamZipsBean> getExamZips() {
        return examZips;
    }

    public void setExamZips(List<ExamZipsBean> examZips) {
        this.examZips = examZips;
    }

    public static class ExamZipsBean {
        /**
         * examId : 3
         * zipPath : /resources/mhk/mobie/MHKMN3005TL(1).zip
         */

        private String examId;
        private String zipPath;

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getZipPath() {
            return zipPath;
        }

        public void setZipPath(String zipPath) {
            this.zipPath = zipPath;
        }
    }
}
