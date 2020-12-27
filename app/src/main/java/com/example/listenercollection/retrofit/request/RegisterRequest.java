package com.example.listenercollection.retrofit.request;

public class RegisterRequest {
    private String act;
    private String pwd;
    private String name;
    private int sex;
    private int age;
    private String phone;
    private String icon;

    public RegisterRequest(String act, String pwd, String name, int sex, int age, String phone, String icon) {
        this.act = act;
        this.pwd = pwd;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.phone = phone;
        this.icon = icon;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
