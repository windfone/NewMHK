package com.hlxyedu.mhk.model.event;

import com.hlxyedu.mhk.model.bean.OperationVO;

import java.util.ArrayList;

public class LoginEvent {

    public static final String LOGIN = "login";

    private String type;

    private int pos;

    private ArrayList<OperationVO> lists;

    private String title;

    private String conTitle;

    public LoginEvent(String type) {
        this.type = type;
    }

    public LoginEvent(String type, int pos, ArrayList<OperationVO> lists, String title, String conTitle) {
        this.type = type;
        this.pos = pos;
        this.lists = lists;
        this.title = title;
        this.conTitle = conTitle;
    }

    public String getType() {
        return type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public ArrayList<OperationVO> getLists() {
        return lists;
    }

    public void setLists(ArrayList<OperationVO> lists) {
        this.lists = lists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConTitle() {
        return conTitle;
    }

    public void setConTitle(String conTitle) {
        this.conTitle = conTitle;
    }
}
