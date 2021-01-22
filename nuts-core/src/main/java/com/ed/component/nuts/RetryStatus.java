package com.ed.component.nuts;

/**
 * @author : Edward
 * @date : 2021/1/19 下午5:52
 */
public enum RetryStatus {

    /**
     * 初始化
     */
    INIT(0),

    /**
     * 失败
     */
    FAIL(1),

    /**
     * 成功
     */
    SUCCESS(2),
    ;

    private int status;

    RetryStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
