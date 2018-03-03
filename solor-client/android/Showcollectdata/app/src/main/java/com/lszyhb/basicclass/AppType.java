package com.lszyhb.basicclass;

/**
 * Created by kkk8199 on 11/27/17.
 */

public enum AppType {
    APK(10), IPA(50), RPM(100);

    private int type;

    private AppType(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }
}
