package com.ccsu.community.enums;


/**
 * @author 华华
 */

public enum NotificationTypeEnum {
    //回复了问题
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论"),
    LIKE_QUESTION(3,"点赞了问题"),
    LIKE_COMMENT(4,"点赞了评论"),
    //点赞二级评论
    LIKE_TWO_COMMENT(5,"点赞了二级评论");


    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
