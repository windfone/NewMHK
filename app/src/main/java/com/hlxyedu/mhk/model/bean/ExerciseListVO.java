package com.hlxyedu.mhk.model.bean;

import java.util.List;

public class ExerciseListVO {


    /**
     * total : 220
     * exam : [{"zip_path":"/resources/mhk/mobile/MHKMN3021TL(5).zip","times":32,"id":"266","examname":"【三级】MHK模拟试卷3021听力练习(5)","price":0},{"zip_path":"/resources/mhk/mobile/MHKMN3019TL(5).zip","times":12,"id":"265","examname":"【三级】MHK模拟试卷3019听力练习(5)","price":0},{"zip_path":"/resources/mhk/mobile/MHKMN3017TL(5).zip","times":9,"id":"264","examname":"【三级】MHK模拟试卷3017听力练习(5)","price":0},{"zip_path":"/resources/mhk/mobile/MHKMN3015TL(5).zip","times":6,"id":"263","examname":"【三级】MHK模拟试卷3015听力练习(5)","price":0},{"zip_path":"/resources/mhk/mobile/MHKMN3014YD(5).zip","times":12,"id":"262","examname":"【三级】MHK模拟试卷3014阅读理解练习(5)","price":0},{"zip_path":"/resources/mhk/mobile/MHKMN3013YD(5).zip","times":2,"id":"261","examname":"【三级】MHK模拟试卷3013阅读理解练习(5)","price":0},{"zip_path":"/resources/mhk/mobile/MHKMN3013TL(5).zip","times":2,"id":"260","examname":"【三级】MHK模拟试卷3013听力练习(5)","price":0}]
     */

    private int total;
    private List<ExerciseVO> exam;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ExerciseVO> getExam() {
        return exam;
    }

    public void setExam(List<ExerciseVO> exam) {
        this.exam = exam;
    }



}
