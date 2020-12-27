package com.example.listenercollection.retrofit.response;

public class BaseResponse {

    public final static String SUCCESS = "SUCCESS";
    public final static String FAIL = "FAIL";

    private String Result;
    private String Err_msg;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getErr_msg() {
        return Err_msg;
    }

    public void setErr_msg(String err_msg) {
        Err_msg = err_msg;
    }
}
