package com.example.listenercollection.bean;

public class PayBean implements Comparable<PayBean> {
    private String type;
    private String time;
    private String pay;

    public PayBean() {
    }

    public PayBean(String type, String time, String pay) {
        this.type = type;
        this.time = time;
        this.pay = pay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    @Override
    public int compareTo(PayBean o) {
        return o.getTime().compareTo(getTime());
    }
}
