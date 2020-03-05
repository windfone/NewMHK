package com.hlxyedu.mhk.model.bean;

public class ExerciseVO {

    /**
     * zip_path : /resources/mhk/mobile/MHKMN3021TL(5).zip
     * times : 32
     * id : 266
     * examname : 【三级】MHK模拟试卷3021听力练习(5)
     * price : 0
     */

    private String zip_path;
    private int times;
    private String id;
    private String examname;
    private int price;

    public String getZip_path() {
        return zip_path;
    }

    public void setZip_path(String zip_path) {
        this.zip_path = zip_path;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
