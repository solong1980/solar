package com.lszyhb.basicclass;

/**
 * Created by kkk8199 on 2/7/18.
 */

public enum AuditResult {
    WAIT_FOR_AUDIT(10), AGREE(50), REJECT(60);

    private int type;

    private AuditResult(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }
}
