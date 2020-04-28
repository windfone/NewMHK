package com.hlxyedu.mhk.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserVO implements Parcelable {


    /**
     * id : f2037dda-0665-430f-a24a-8eb697c3ed3b
     * mobile : 13026551111
     * idCard : null
     * password : 96E79218965EB72C92A549DD5A330112
     * createTime : 1572224977000
     * updateTime : null
     * state : 0
     * saasUserId : 00000000000000044067
     * sex : 0
     * userName : 学生C
     * nationId : 1
     * nationName : 维吾尔族
     * classId : 6
     * studentNumber : 50
     * dormNumber : 
     * friend : 
     * remark : 
     * token : null
     * tokentime : null
     */

    private String id;
    private String mobile;
    private String idCard;
    private String password;
    private long createTime;
    private String updateTime;
    private String state;
    private String saasUserId;
    private String sex;
    private String userName;
    private String nationId;
    private String nationName;
    private String classId;
    private String studentNumber;
    private String dormNumber;
    private String friend;
    private String remark;
    private String token;
    private String tokentime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSaasUserId() {
        return saasUserId;
    }

    public void setSaasUserId(String saasUserId) {
        this.saasUserId = saasUserId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNationId() {
        return nationId;
    }

    public void setNationId(String nationId) {
        this.nationId = nationId;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDormNumber() {
        return dormNumber;
    }

    public void setDormNumber(String dormNumber) {
        this.dormNumber = dormNumber;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokentime() {
        return tokentime;
    }

    public void setTokentime(String tokentime) {
        this.tokentime = tokentime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mobile);
        dest.writeString(this.idCard);
        dest.writeString(this.password);
        dest.writeLong(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.state);
        dest.writeString(this.saasUserId);
        dest.writeString(this.sex);
        dest.writeString(this.userName);
        dest.writeString(this.nationId);
        dest.writeString(this.nationName);
        dest.writeString(this.classId);
        dest.writeString(this.studentNumber);
        dest.writeString(this.dormNumber);
        dest.writeString(this.friend);
        dest.writeString(this.remark);
        dest.writeString(this.token);
        dest.writeString(this.tokentime);
    }

    public UserVO() {
    }

    protected UserVO(Parcel in) {
        this.id = in.readString();
        this.mobile = in.readString();
        this.idCard = in.readString();
        this.password = in.readString();
        this.createTime = in.readLong();
        this.updateTime = in.readString();
        this.state = in.readString();
        this.saasUserId = in.readString();
        this.sex = in.readString();
        this.userName = in.readString();
        this.nationId = in.readString();
        this.nationName = in.readString();
        this.classId = in.readString();
        this.studentNumber = in.readString();
        this.dormNumber = in.readString();
        this.friend = in.readString();
        this.remark = in.readString();
        this.token = in.readString();
        this.tokentime = in.readString();
    }

    public static final Parcelable.Creator<UserVO> CREATOR = new Parcelable.Creator<UserVO>() {
        @Override
        public UserVO createFromParcel(Parcel source) {
            return new UserVO(source);
        }

        @Override
        public UserVO[] newArray(int size) {
            return new UserVO[size];
        }
    };
}
