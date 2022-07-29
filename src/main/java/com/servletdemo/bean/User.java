package com.servletdemo.bean;

public class User {
    private String uName;
    private int uAge;
    private String uTel;
    private int id;

    public User() {
        super();
    }

    public User(int id, String uName, int uAge, String uTel) {
        this.id = id;
        this.uName = uName;
        this.uAge = uAge;
        this.uTel = uTel;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getuAge() {
        return uAge;
    }

    public void setuAge(int uAge) {
        this.uAge = uAge;
    }

    public String getuTel() {
        return uTel;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + id + '\'' +
                ", userName='" + uName + '\'' +
                ", userAge='" + uAge + '\'' +
                ", userTel=" + uTel +
                '}';
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
