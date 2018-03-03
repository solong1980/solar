package com.lszyhb.basicclass;

/**
 * Created by kkk8199 on 12/6/17.
 */

public class ShowVCode extends ShowAbt {
    private int type; // 10,注册校验码,20找回校验码
    private String phone;
    private String vcode;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

}
