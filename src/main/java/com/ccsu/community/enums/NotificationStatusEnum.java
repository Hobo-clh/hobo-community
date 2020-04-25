package com.ccsu.community.enums;


/**
 * @author 华华
 */

public enum NotificationStatusEnum {
    //0表示未读
    UNREAD(0),
    //1表示以读
    READ(1);

    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
