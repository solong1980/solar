package com.lszyhb.basicclass;


import java.io.Serializable;

/**
 * Created by kkk8199 on 11/20/17.
 */

public abstract class ShowAbt implements Serializable {

    // 业务响应码
    private int retCode;
    // 提示信息
    private String msg;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
