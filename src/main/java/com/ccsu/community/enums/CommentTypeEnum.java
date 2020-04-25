package com.ccsu.community.enums;


/**
 * @author 华华
 */

public enum CommentTypeEnum {
    //代表评论类型为问题
    QUESTION(1),
    COMMENT(2);

    private Integer type;

    CommentTypeEnum(Integer type){
        this.type=type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }return false;
    }
}
