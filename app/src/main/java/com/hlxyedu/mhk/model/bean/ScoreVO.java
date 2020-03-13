package com.hlxyedu.mhk.model.bean;

public class ScoreVO {


    /**
     * score : 1
     * rightcount : 1
     * wrongcount : 7
     * rightTitle : 2
     * wrongTitle : 1、3、4、5、6、7、8
     */

    private String score;
    private String rightcount;
    private String wrongcount;
    private String rightTitle;
    private String wrongTitle;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRightcount() {
        return rightcount;
    }

    public void setRightcount(String rightcount) {
        this.rightcount = rightcount;
    }

    public String getWrongcount() {
        return wrongcount;
    }

    public void setWrongcount(String wrongcount) {
        this.wrongcount = wrongcount;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public void setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
    }

    public String getWrongTitle() {
        return wrongTitle;
    }

    public void setWrongTitle(String wrongTitle) {
        this.wrongTitle = wrongTitle;
    }
}
