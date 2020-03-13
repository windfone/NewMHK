package com.hlxyedu.mhk.model.bean;

public class OperationVO {

        /**
         * id : 5bc8e6d16e175a1172D36129EB1AC15E1AE81CD66E2EA8270F（homeworkid 上传作业结果的时候要用到的）
         * teacherId : null
         * teacherName : 张姗姗
         * homeworkName : 口语8
         * examType : KY
         * termType : 2
         * termValue : 99
         * examId : /resources/mhk/mobile/MHKMN3010LD(1).zip   // 试卷下载路径
         * createTime : 1572349809000
         * doneTime : 1572451200000
         * userCount : 138
         * state : 2
         */

        private String id;// homeworkId
        private Object teacherId;
        private String teacherName;
        private String homeworkName;
        private String examType;
        private String zipPath;
        private String termType;
        private String termValue;
        private String examId;
        private long createTime;
        private long doneTime;
        private int userCount;
        private String state;//1,可以做作业;2,作业未完成（时间到了未完成）;3,作业已完成；4，作业已完成；

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(Object teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getHomeworkName() {
            return homeworkName;
        }

        public void setHomeworkName(String homeworkName) {
            this.homeworkName = homeworkName;
        }

        public String getExamType() {
            return examType;
        }

        public void setExamType(String examType) {
            this.examType = examType;
        }

        public String getTermType() {
            return termType;
        }

        public void setTermType(String termType) {
            this.termType = termType;
        }

        public String getTermValue() {
            return termValue;
        }

        public void setTermValue(String termValue) {
            this.termValue = termValue;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getDoneTime() {
            return doneTime;
        }

        public void setDoneTime(long doneTime) {
            this.doneTime = doneTime;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZipPath() {
            return zipPath;
        }

        public void setZipPath(String zipPath) {
            this.zipPath = zipPath;
        }
}
