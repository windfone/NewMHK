package com.hlxyedu.mhk.model.http.response;

/**
 * 作者：skyworth on 2017/9/7 09:56
 * 邮箱：dqwei@iflytek.com
 */

public class HttpResponse<T> {

    private String msg;
    private String message;
    private int code;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
